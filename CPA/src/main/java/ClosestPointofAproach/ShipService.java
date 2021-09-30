package ClosestPointofAproach;

import java.util.*;

public class ShipService {

    private String brokers;
    private String topic;


    private Map<Integer, Ship> shipsMap;

    Timer timer = new Timer();
    ShipProducerThread producerThread;

    public ShipService(String brokers, String topic) {

        this.brokers = brokers;
        this.topic = topic;

        shipsMap = new HashMap<>();
        int ship_count = Feature.AllShips.getShipCount();

        for (int i = 0; i < ship_count; i++) {
            Ship ship = new Ship(i + 1);
            shipsMap.put((i + 1), ship);
        }
        Feature.AllShips.Id = findAllShipsId();

        producerThread= new ShipProducerThread(this.brokers,this.topic,shipsMap);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SendMessageTask();
            }
        }, 0, 1000);
    }

    private void SendMessageTask()  {
        Thread t1 = new Thread(producerThread);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Integer> findAllShipsId() {
        return new ArrayList<Integer>(shipsMap.keySet());
    }

}

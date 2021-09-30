package ClosestPointofAproach;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.*;

public class ShipProducerThread  implements Runnable {

    private KafkaProducer<String,Vehicle> producer;
    private KafkaProducer<String,String > producer1;
    private final String topic;
    private Map<Integer, Ship> shipsMap;
    private boolean ship_active;

   // private boolean idClosed = false;

    public ShipProducerThread(String brokers, String topic, boolean ship_active) {
        this.topic = topic;
        this.ship_active = ship_active;
        Properties prop = createProducer(brokers);
        if(this.ship_active)
            producer = new KafkaProducer<>(prop);
        else
            producer1 = new KafkaProducer<>(prop);

    /*    Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                idClosed = true;
            }
        }, 5000); */
    }

    public ShipProducerThread(String brokers, String topic, Map Shipmap) {
        this(brokers, topic, true);
        shipsMap = new HashMap<>(Shipmap);
    }

    private Properties createProducer(String brokers) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("acks", "all");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        if (ship_active) {
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "ClosestPointofAproach.VehicleSerializer");
        }
        else{
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        }

        props.put("partitioner.class", "ClosestPointofAproach.KafkaShipCustomPatitioner");

        return props;
    }

    private List<Vehicle> getShipMsg() {
        ArrayList<Vehicle> msgList = new ArrayList<>();
        for (Vehicle v : shipsMap.values()) {
            msgList.add(v);
        }
        return msgList;
    }

    public void run() {

        if (ship_active) {
            for (Vehicle msg : getShipMsg()) {
               // if (!idClosed || msg.Id != 1) {//If the ship id is 1, the 1st ship will not be sent after 5 seconds.

                    producer.send(new ProducerRecord<>(topic, String.valueOf(msg.Id), msg), new Callback() {
                        public void onCompletion(RecordMetadata metadata, Exception e) {
                            if (e != null) {
                                e.printStackTrace();
                            }
                            System.out.println("Sent:" + msg + ", Ship: " + msg.Id + ", Partition: " + metadata.partition()
                                    + " ,ofsset: " + metadata.offset());
                        }
                    });
               // }
            }
        }
        else {
            String msg = CPA.getResultMessage();
            String msgId = CPA.getResult_msg_id();

            producer1.send(new ProducerRecord<String, String>(topic, msgId, msg), new Callback() {
                public void onCompletion(RecordMetadata metadata, Exception e) {
                    if (e != null) {
                        e.printStackTrace();
                    }
                    System.out.println("Sent:" + msg + ", resulId: " + msgId+ ", offset: " + metadata.offset());
                    int id = Integer.parseInt(msgId);
                    String index = Feature.AllShips.partitionIndex[id];
                    Feature.AllShips.ResultPartitionsMap.get(index).lastOffset = metadata.offset()+1;
                    }
                });
            }
            // producer.close();
        }
    }











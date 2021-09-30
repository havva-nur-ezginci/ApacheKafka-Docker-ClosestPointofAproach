package ClosestPointofAproach;
import java.text.DecimalFormat;
import java.util.*;

public class CPA {

    private static ArrayList<Boolean> UndeliveredList = new ArrayList<>(); //ship list without kinematic information

    private static String brokers;
    private static String groupId;
    private static String topic_write, topic_read;
    private static ShipProducerThread producerThread;
    private static int shipcount = Feature.AllShips.getShipCount();
    private Timer timer = new Timer();
    private static Map<Integer, Vehicle> shipsMsg;

    public CPA(String brokers, String groupId, String topic, String topic1) {
        this.brokers = brokers;
        this.groupId = groupId;
        this.topic_read = topic;  // topic to read
        this.topic_write = topic1; // Topic to write calculation results
        producerThread = new ShipProducerThread(brokers, topic_write, false);
        for (int i = 0; i < shipcount; i++)
            UndeliveredList.add(i, false);

        ConsumerStart();
    }

    private static void ConsumerStart() {
        ShipConsumerThread consumerThread = new ShipConsumerThread(brokers, groupId, topic_read);
        Thread t2 = new Thread(consumerThread);
        t2.start();
    }

    public static void setUndeliveredList() {
        for (int i = 0; i < shipcount; i++) {
            UndeliveredList.set(i, false);
        }
    }

    protected static void ExtractMessage(ArrayList<Vehicle> list) {
        shipsMsg = new HashMap<>(); // to hold messages from ships
        for (Vehicle v : list)
            shipsMsg.put(v.Id, v);

         /*Kinematic information of all ships has been received. Let's change the non-delivery
          information to false for ships that have not expired 10 seconds but
          have sent incomplete messages before. */
        if (shipsMsg.keySet().containsAll(Feature.AllShips.getId())) {
            setUndeliveredList();  // set all false
        } else {
            FindUndeliverables(Feature.AllShips.getId()); // find ships with no messages
        }

        for (int i = 1; i < shipcount; i++) { //cpa account for each binary ship
            if (!shipsMsg.keySet().contains(i))
                continue;
            for (int j = i + 1; j <= shipcount; j++) {
                if (shipsMsg.keySet().contains(j)) {
                    if (shipsMsg.get(i).time.equals(shipsMsg.get(j).time)) {
                        Calculate calculate = new Calculate(new Vehicle[]{shipsMsg.get(i), shipsMsg.get(j)});
                        boolean skip_to_next = WriteResult(calculate, "" + i + j);
                    }
                }
            }
        }
    }

    public static void FindUndeliverables(ArrayList<Integer> AllShipId) {
        for (Integer id : AllShipId) {
            if (!shipsMsg.keySet().contains(id)) { // if one of the ships does not have an id
                if (!UndeliveredList.get(id - 1).equals(true)) { //There is no need to start the timer again if it is already true
                    UndeliveredList.set(id - 1, true); // arraylist starts from 0 but id is from 1
                    Timer timer = new Timer();
                    TimerTask task = new TimerTask() {
                        @Override
                        public void run() {
                            if (!UndeliveredList.get(id - 1)) {
                                timer.cancel(); //Terminate the timer thread
                            } else {  /*If there is no message from this ship for 10 seconds,
                                    the calculations made for it are cancelled. */
                                CancelCalculations(id);
                                timer.cancel();
                            }
                        }
                    };
                    timer.schedule(task, 10000);
                }
            }
        }
    }

    private static String result_message = null;
    private static String result_msg_id = null;

    public static String getResultMessage() {
        return result_message;
    }

    public static String getResult_msg_id() {
        return result_msg_id;
    }

    public static Boolean WriteResult(Calculate calculate, String ids) {
        DecimalFormat formatter = new DecimalFormat("####.0000");

        result_message = calculate.warning + " Time: " + calculate.Tcpa + " Remaining seconds:" + calculate.second +
                " Distance: " + formatter.format(calculate.Dcpa) +
                " v1location:[" + calculate.v1Location.x + ","+calculate.v1Location.y + "]" +
                " v2location:[" + calculate.v2Location.x + ","+calculate.v2Location.y + "]" +
                " ids:" + ids + " note:" + calculate.result_note;
        result_msg_id = "" + Feature.AllShips.ResultPartitionsMap.get(ids).partitionIndex;

        Thread t1 = new Thread(producerThread);
        t1.start();
        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void CancelCalculations(int id) {
        System.out.println("10 seconds up for ship number " + id + "!! Calculations canceling....");

        new DeleteRecord(topic_write,id);
    }

}














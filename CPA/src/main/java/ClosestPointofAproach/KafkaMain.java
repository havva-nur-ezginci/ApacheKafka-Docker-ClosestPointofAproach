package ClosestPointofAproach;

import java.util.ArrayList;
import java.util.Arrays;

import static ClosestPointofAproach.Feature.AllShips;

//terminal -> docker-compose up --build -d
//terminal -> docker-compose up
public class KafkaMain {

    static int factorial(int n) {
        int fact = 1;
        int i = 1;
        while(i <= n) {
            fact *= i;
            i++;
        }
        return fact;
    }
    public static void main(String[] args) {

        AllShips.planar_field_width = 1000;
        AllShips.planar_field_height = 1000;
        AllShips.Count = 5;

        String brokers = "localhost:9092";
        String groupId = "group01";
        String topic = "ShipMessageTopic";
        String topic1 = "CpaCalculationResultsTopic";

        if (args != null && args.length == 4) {
            brokers = args[0];
            groupId = args[1];
            topic = args[2];
            topic1 = args[3];
        }

        if (AllShips.Count < 2) {
            System.out.println("The number of ships cannot be less than 2. Incorrect input!");
        }
        else {
            int resultPartitions = (factorial(AllShips.Count) / (factorial(2) * factorial(AllShips.Count - 2)));
            AllShips.partitionIndex = new String[resultPartitions];

            int i = 0, j = 1, k = j + 1;
            // partition index =0 in CpaCalculationResultsTopic Includes results for ships 1 and 2
            while (i < resultPartitions) {
                AllShips.ResultPartitionsMap.put("" + j + k,new DoubleResult(i,0
                        ,new ArrayList<Integer>(Arrays.asList(j,k))));
                AllShips.partitionIndex[i] = "" + j + k;
                k++;
                if (k > AllShips.Count) {
                    j++;
                    k = j + 1;
                }
                i++;
            }

            new TopicCreator(topic,AllShips.Count+1,brokers);
            new TopicCreator(topic1,resultPartitions,brokers);

            ShipService shipService = new ShipService(brokers,topic);

            CPA cpa_calculate = new CPA(brokers,groupId,topic,topic1);

        }
    }
}
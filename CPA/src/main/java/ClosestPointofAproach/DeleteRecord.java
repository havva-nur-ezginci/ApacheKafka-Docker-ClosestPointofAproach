package ClosestPointofAproach;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class DeleteRecord {
    private AdminClient client = null;

   public DeleteRecord(String topicName,int id){
        setup();
       for (DoubleResult d : Feature.AllShips.ResultPartitionsMap.values()) {
           if (d.ids.contains(id)) {
               DeleteRecordsResult result =  this.deleteMessages(topicName,d.partitionIndex,d.lastOffset);
               Map<TopicPartition, KafkaFuture<DeletedRecords>> lowWatermarks = result.lowWatermarks();
               try {
                   for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry : lowWatermarks.entrySet()) {
                       System.out.println("deleted: "+entry.getKey().topic() + " " + entry.getKey().partition()
                               + " " + entry.getValue().get().lowWatermark());
                   }
               } catch (InterruptedException | ExecutionException e) {
                   e.printStackTrace();
               }
           }
       }
    }

    public void setup() {
        Map<String, Object> conf = new HashMap<>();
        conf.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        conf.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, "5000");
        client = AdminClient.create(conf);
    }
    public DeleteRecordsResult  deleteMessages(String topicName, int partitionIndex, long beforeIndex) {
        TopicPartition topicPartition = new TopicPartition(topicName, partitionIndex);
        Map<TopicPartition, RecordsToDelete> deleteMap = new HashMap<>();
        deleteMap.put(topicPartition, RecordsToDelete.beforeOffset(beforeIndex));
        DeleteRecordsResult result = client.deleteRecords(deleteMap);
        return result;
    }


  /*public static void main(String[] args) {

       DeleteRecord p = new DeleteRecord();
       p.setup();
       DeleteRecordsResult result =  p.deleteMessages("CpaCalculationResultsTopic",0,10);
       Map<TopicPartition, KafkaFuture<DeletedRecords>> lowWatermarks = result.lowWatermarks();
       try {
           for (Map.Entry<TopicPartition, KafkaFuture<DeletedRecords>> entry : lowWatermarks.entrySet()) {
               System.out.println(entry.getKey().topic() + " " + entry.getKey().partition() + " " + entry.getValue().get().lowWatermark());
           }
       } catch (InterruptedException | ExecutionException e) {
           e.printStackTrace();
       }
       p.client.close();
    }*/
    }



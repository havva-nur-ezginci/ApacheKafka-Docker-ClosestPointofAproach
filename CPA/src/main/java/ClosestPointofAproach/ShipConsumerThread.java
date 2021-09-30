package ClosestPointofAproach;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.*;

public class ShipConsumerThread implements Runnable{

    private KafkaConsumer<String,Vehicle> consumer;
    private String topic;

    public ShipConsumerThread(String brokers, String groupId, String topic) {
        Properties prop = createConsumerConfig(brokers, groupId);
        this.consumer = new KafkaConsumer<>(prop,
                new StringDeserializer(),new VehicleDeserializer());
        this.topic = topic;
        this.consumer.subscribe(Collections.singletonList(this.topic));
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                MessagesPerSecond();
            }
        }, 970, 980);
    }

    public static Properties createConsumerConfig(String brokers, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", brokers);
        props.put("group.id", groupId);
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "VehicleDeSerializer");

        return props;
    }
    ArrayList<Vehicle> list = new ArrayList<>();
    Timer timer = new Timer();
    public void MessagesPerSecond(){ // Let's send incoming messages to CPA in the last 1 second
        CPA.ExtractMessage(list);
        list.clear();
    }
    @Override
    public void run() {
        while (true) {

            ConsumerRecords<String,Vehicle> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord record : records) {
              //  System.out.println("Receive message: " + record.value() + ", Partition: " + record.partition() + ", Offset: " + record.offset());
                list.add((Vehicle) record.value());
            }

        }
    }
}

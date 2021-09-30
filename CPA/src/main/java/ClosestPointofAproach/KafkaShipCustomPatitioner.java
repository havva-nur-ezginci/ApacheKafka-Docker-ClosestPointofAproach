package ClosestPointofAproach;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

public class KafkaShipCustomPatitioner implements Partitioner {

    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes,Cluster cluster) {
        int partition = 0; // If the shipId not found, default partition is 0

        Integer shipId = Integer.parseInt(String.valueOf(key));
        if (shipId != null) {
            partition = shipId;
        }
        return partition;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}

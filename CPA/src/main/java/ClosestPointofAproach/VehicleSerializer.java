package ClosestPointofAproach;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.ByteBuffer;
import java.util.Map;

public class VehicleSerializer implements Serializer<Vehicle> {
    private String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // nothing to configure
    }

    @Override
    public byte[] serialize(String topic, Vehicle vehicle) {

        int sizeOfTime;
        int sizeOfLocationPoint;
        int sizeOfVelocityPoint;
        byte[] serializedTime;
        byte[] serializedLocationPoint;
        byte[] serializedVelocityPoint;

        try {
            if (vehicle == null)
                return null;

            serializedTime = vehicle.getTime().getBytes(encoding);
            sizeOfTime = serializedTime.length;

            serializedLocationPoint = (vehicle.location.x+","+vehicle.location.y).getBytes(encoding);
            sizeOfLocationPoint = serializedLocationPoint.length;

            serializedVelocityPoint = (vehicle.velocity.x+","+vehicle.velocity.y).getBytes(encoding);
            sizeOfVelocityPoint = serializedVelocityPoint.length;

            ByteBuffer buffer = ByteBuffer.allocate(4 + 4 + sizeOfTime +
                    4 + sizeOfLocationPoint + 4+ sizeOfVelocityPoint);

            buffer.putInt(vehicle.Id);

            buffer.putInt(sizeOfTime);
            buffer.put(serializedTime);

            buffer.putInt(sizeOfLocationPoint);
            buffer.put(serializedLocationPoint);

            buffer.putInt(sizeOfVelocityPoint);
            buffer.put(serializedVelocityPoint);

            return buffer.array();

        } catch (Exception e) {
            throw new SerializationException("Error when serializing Vehicle to byte[]");
        }
    }

    @Override
    public void close() {
        // nothing to do
    }


}
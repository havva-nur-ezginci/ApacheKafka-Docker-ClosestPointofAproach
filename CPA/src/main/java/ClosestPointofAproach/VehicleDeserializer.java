package ClosestPointofAproach;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import java.awt.*;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Map;

public class VehicleDeserializer implements Deserializer<Vehicle> {
    private final String encoding = "UTF8";

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //Nothing to configure
    }

    @Override
    public Vehicle deserialize(String topic, byte[] data) {

        try {
            if (data == null){
                System.out.println("Null recieved at deserialize");
                return null;
            }
            ByteBuffer buffer = ByteBuffer.wrap(data);

            int id = buffer.getInt();

            int sizeOfTime = buffer.getInt();
            byte[] TimeBytes = new byte[sizeOfTime];
            buffer.get(TimeBytes);
            String timeString = new String(TimeBytes,encoding);

            int sizeOfLocationPoint = buffer.getInt();
            byte[] LocationPointBytes = new byte[sizeOfLocationPoint];
            buffer.get(LocationPointBytes);
            String deserializedPoint = new String(LocationPointBytes, encoding);
            int[] coordinate = Arrays.stream(deserializedPoint.split(",")).mapToInt(Integer::parseInt).toArray();
            Point location = new Point(coordinate[0],coordinate[1]);

            int sizeOfVelocityPoint = buffer.getInt();
            byte[] VelocityPointBytes = new byte[sizeOfVelocityPoint];
            buffer.get(VelocityPointBytes);
            deserializedPoint = new String(VelocityPointBytes, encoding);
            coordinate = Arrays.stream(deserializedPoint.split(",")).mapToInt(Integer::parseInt).toArray();
            Point velocity = new Point(coordinate[0],coordinate[1]);


            Vehicle vehicle = new Vehicle(id,location,velocity,timeString);
            return vehicle;

        } catch (Exception e) {
            throw new SerializationException("Error when deserialize byte[] to Vehicle");
        }

    }

    @Override
    public void close() {
        // nothing to do
    }
}

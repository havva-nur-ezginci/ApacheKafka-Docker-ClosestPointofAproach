package ClosestPointofAproach;

import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Date;

public class Vehicle {

    Point location = new Point();
    Point velocity = new Point();
    int Id ;
    String time;

    Vehicle(){}

    Vehicle(int l_x,int l_y,int v_x,int v_y){
        location.x = l_x;
        location.y = l_y;
        velocity.x = v_x;
        velocity.y = v_y;
        time = getNow();
    }

    Vehicle(int id,Point l,Point v,String t){
        this(l.x,l.y,v.x,v.y);
        time = t;
        Id = id;
    }
    public String getTime(){
        time = getNow();
        return time;
    }

    public String getNow(){  // Turkey Time
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        OffsetDateTime utcTime = OffsetDateTime.now(ZoneOffset.ofHours(3));
        Date d =null;
        try {
            d = df.parse(""+utcTime.getHour()+":"+utcTime.getMinute()+":"+
                    utcTime.getSecond());
           } catch (ParseException e) {
               e.printStackTrace();
           }
           return df.format(d);
       }

  /* public static void main(String[] args) {
        Vehicle v= new Vehicle();
        System.out.println(v.getNow());
 }*/
}

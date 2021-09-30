package ClosestPointofAproach;

import java.awt.*;
import java.awt.geom.Point2D;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

public class Calculate {

    Vehicle v1 ;
    Vehicle v2 ;
    String time;
    ArrayList <Integer> coefficients = new ArrayList<>();
    Double Dcpa;
    String Tcpa;
    float second;
    Point2D.Float v1Location , v2Location;
    String result_note = "",warning="";

    public Calculate(Vehicle [] shipsKinematics){
        v1 = new Vehicle();
        v2 = new Vehicle();
        v1 = shipsKinematics[0];
        v2 = shipsKinematics[1];
        this.time = v1.time;
        v1Location = new Point2D.Float();
        v2Location = new Point2D.Float();
        cpa();
    }
    public  void pow(int equation_coefficients[]){
         // (x+ y*t)^2 = a*t^2 + b*t + c
        coefficients.add((equation_coefficients[0]*equation_coefficients[0]));  //  a
        coefficients.add(2*equation_coefficients[0]*equation_coefficients[1]); // b
        coefficients.add((equation_coefficients[1]*equation_coefficients[1]));  // c
    }

    public int[] sum(){ // d = (a*t^2 + b*t + c)^(1/2)  // C[0] = c
        int C[] = new int[3];
        for(int i=0; i < 3;i++){
            C[i] = coefficients.get(i)+coefficients.get(i+3);
        }
        return C;
    }

    public static Point subtraction(Point u,Point v){
        return new Point((u.x-v.x),(u.y-v.y));
    }

    public static float find_seconds(int d[]) {  // -b/2a
        float t = 0;
        if(d[2]!=0)
            t = (-1.0f * d[1] / (2 * d[2]));
        return t;
    }

    public void distance(int a[],float second){  // distance to collision point
        Dcpa = Math.sqrt(Math.pow(second,2)*a[2] + second*a[1] + a[0]);
    }

    public String time_sum(String t1){

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = timeFormat.parse(time); // time when ship sends kinematic information
            d2 = timeFormat.parse(t1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long sum = (int) (d1.getTime() + d2.getTime());
        String date3 = timeFormat.format(new Date(sum));
        return  timeFormat.format(new Date(sum));
    }
    public void setLocation(float s){  // collision point
        v1Location.x = (v1.location.x+(s*v1.velocity.x));
        v1Location.y = (v1.location.y+(s*v1.velocity.y));
        v2Location.x = (v2.location.x+(s*v2.velocity.x));
        v2Location.y = (v2.location.y+(s*v2.velocity.y));
    }

    public  void cpa(){
        Point X = subtraction(new Point(v1.location.x,v1.velocity.x), // X = ( x1(t) - x2(t)))
                new Point(v2.location.x,v2.velocity.x));

        Point Y = subtraction(new Point(v1.location.y,v1.velocity.y), // Y = ( y1(t) - y2(t)))
                new Point(v2.location.y,v2.velocity.y));

        pow(new int[]{X.x,X.y});  // X^2 =  (x+ y*t)^2
        pow(new int[]{Y.x,Y.y}); //  Y^2 =  (x+ y*t)^2

        int a[] = sum();  //  ( X^2 + Y^2 ) =  (a*t^2 + b*t + c)
        second = find_seconds(a); // -b/2a for t
        String t = "00:00:"+second;
        Tcpa = time_sum(t);  // t = (-b/2a) ; tcpa => t+now_time
        distance(a,second);  //  d = (a*t^2 + b*t + c)^(1/2)
        System.out.println("tcpa: "+Tcpa+" dcpa: "+Dcpa+" s:"+second);
        setLocation(second);
        System.out.println("v1location: "+v1Location+"  v2Location:"+v2Location);
        if( second < 1 && second > -1)
            result_note = "Ships getting closer";
        if(second < 0 ) {
            result_note += "Opposite direction.";
        }
        if(v1Location.equals(v2Location) && second >= 0){
            result_note += "Ships will collide!!";
            warning = "HELP!!!!!";
        }
        if( Dcpa > 0 && Dcpa < 1 ) {
            result_note += "Less than 1 meter distance!";
        }
        if(String.valueOf(second).equals(0.0) || String.valueOf(second).equals(-0.0)) {
            warning += "-Closest Moment-";
        }
    }

   /*public static void main(String[] args) {

        Vehicle[] a = new Vehicle[2];
        a[0] = new Vehicle(248,108, 2, -10);
        a[1] = new Vehicle(884,892, -4, -2);
        Calculate c= new Calculate(a);
    }*/

}

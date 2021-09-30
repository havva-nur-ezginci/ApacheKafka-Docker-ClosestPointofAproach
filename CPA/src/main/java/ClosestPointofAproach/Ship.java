package ClosestPointofAproach;

import java.util.Timer;
import java.util.TimerTask;

class Ship extends Vehicle {

    private static int drawingWidth = Feature.AllShips.planar_field_width;
    private static int drawingHeight = Feature.AllShips.planar_field_height;

    public static int rand(int range, boolean negative) {  //generatRandomPositiveNegitiveValue
        if (negative)
            return (int) (Math.random() * (range * 2 + 1)) - range;

        return (int) (Math.random() * (range));
    }

    public Ship(int shipId) {

        super(rand(drawingWidth, false), rand(drawingHeight, false),
                   rand(10, true), rand(10, true));

        this.drawingWidth = drawingWidth;
        Id = shipId;
        Timer timer = new Timer();

        TimerTask task = new TimerTask() {
               @Override
               public void run() {  // ship move
                   if (location.x <= 0 || location.x >= drawingWidth) {
                       velocity.x = -velocity.x;
                   }
                   if (location.y <= 0 || location.y >= drawingWidth) {
                       velocity.y = -velocity.y;
                   }
                   testX:
                   {
                       if (location.x + velocity.x < 0) {
                           velocity.x = rand(10, true);
                           break testX;
                       }
                       if (location.x + velocity.x > drawingWidth) {
                           velocity.x = rand(10, true);
                           break testX;
                       }
                   }
                   testY:
                   {
                       if (location.y + velocity.y < 0) {
                           velocity.y = rand(10, true);
                           break testY;
                       }
                       if (location.y + velocity.y > drawingHeight) {
                           velocity.y = rand(10, true);
                           break testY;
                       }
                   }
                   location.x += velocity.x;
                   location.y += velocity.y;
               }
           };
        timer.schedule(task, 0, 1000);
       }

}


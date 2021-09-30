package ClosestPointofAproach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public enum Feature {   AllShips;

    int planar_field_width ;
    int planar_field_height ;
    int Count;
    ArrayList<Integer> Id;

    // The results of ship 1 are found in ship results 12,13,14,15.
    //The result of the calculation number 12 is kept
    // in the 0.partition in the topic. It is the calculation result of ships 1 and 2.
    Map<String, DoubleResult> ResultPartitionsMap = new HashMap<>();
    String []partitionIndex;

    public ArrayList<Integer> getId(){
        return Id;
    }

    public int getShipCount(){ return Count; }

}

class DoubleResult{
    int partitionIndex;
    long lastOffset;
    ArrayList<Integer>ids;
    DoubleResult(int pindex,long last,ArrayList<Integer> ids)
    {
        partitionIndex = pindex;
        lastOffset = last;
        this.ids = ids;
    }
}

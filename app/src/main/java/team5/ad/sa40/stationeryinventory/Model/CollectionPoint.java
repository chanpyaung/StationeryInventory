package team5.ad.sa40.stationeryinventory.Model;

import java.util.ArrayList;

/**
 * Created by student on 3/9/15.
 */
public class CollectionPoint {
    //id, name, address string, lat, long  float, id int
    private int colPt_id;
    private String colPt_name;
    private String colPt_address;
    private float colPt_lat;
    private float colPt_long;

    public int getColPt_id(){
        return colPt_id;
    }
    public String getColPt_name(){
        return colPt_name;
    }
    public String getColPt_address(){
        return colPt_address;
    }
    public float getColPt_lat(){
        return colPt_lat;
    }
    public float getColPt_long(){
        return colPt_long;
    }

    public void setColPt_id(int id){
        colPt_id = id;
    }
    public void setColPt_name(String name){
        colPt_name = name;
    }
    public void setColPt_address(String address){
        colPt_address = address;
    }
    public void setColPt_lat(float lat){
        colPt_lat = lat;
    }
    public void setColPt_long(float lon){
        colPt_long = lon;
    }

    static String[] names = {"ISS", "iCube", "Eusoff Hall", "NUH", "Stationary"};
    static Float[] lats = {1.292248F, 1.2925393F, 1.293354F, 1.294373F, 1.289117F};
    static Float[] longs = {103.776611F, 103.7756831F, 103.770356F, 103.783401F, 103.781034F};

    public static ArrayList<CollectionPoint> getAllCollectionPoints(){
        ArrayList<CollectionPoint> temp_colPt = new ArrayList<>();
        for(int i=0; i <5; i++){
            CollectionPoint col_pt = new CollectionPoint();
            col_pt.setColPt_id(i);
            col_pt.setColPt_name(names[i]);
            col_pt.setColPt_address("ColPt_Address " + i);
            col_pt.setColPt_lat(lats[i]);
            col_pt.setColPt_long(longs[i]);

            temp_colPt.add(col_pt);
        }
        return temp_colPt;
    }
}

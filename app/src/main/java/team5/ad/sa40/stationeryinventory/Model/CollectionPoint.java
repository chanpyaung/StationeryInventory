package team5.ad.sa40.stationeryinventory.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import team5.ad.sa40.stationeryinventory.JSONParser;
import team5.ad.sa40.stationeryinventory.Setup;

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

    public CollectionPoint(){}
    public CollectionPoint(int _colID, String _colName, String _colAddress, float _lat, float _long){
        colPt_id = _colID;
        colPt_name = _colName;
        colPt_address = _colAddress;
        colPt_lat = _lat;
        colPt_long = _long;
    }

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
        JSONArray temp = JSONParser.getJSONArrayFromUrl(Setup.baseurl + "/collectionAPI.svc/getCollectionPoint");
        try{
            for(int i = 0; i < temp.length(); i++){
                JSONObject obj = temp.getJSONObject(i);
                temp_colPt.add(new CollectionPoint(obj.getInt("CPID"), obj.getString("CPName"), obj.getString("CPAddress"), (float)obj.getDouble("CPLat"),(float)obj.getDouble("CPLgt")));
            }
        }catch (Exception e){
            Log.e("getAllCollectionPoints", "JSONError");
        }
        return temp_colPt;
    }

    public static CollectionPoint getCollectionByID(int _id){
        JSONObject temp = JSONParser.getJSONFromUrl(Setup.baseurl + "/collectionAPI.svc/getCollectionPointbyID/" +
                String.valueOf(_id));
        CollectionPoint col = null;
        try{
            col = new CollectionPoint(temp.getInt("CPID"), temp.getString("CPName"), temp.getString("CPAddress"),
                    (float)temp.getDouble("CPLat"), (float)temp.getDouble("CPLgt"));
        }
        catch (Exception e){
            Log.e("getCollectionByID", "JSONError");
        }
        return col;
    }
}

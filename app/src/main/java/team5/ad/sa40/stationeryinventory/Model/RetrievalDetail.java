package team5.ad.sa40.stationeryinventory.Model;


import java.util.HashMap;

public class RetrievalDetail extends HashMap<String,Object>{

    public RetrievalDetail(String itemID, String itemName, String Bin,
                           int RequestQty, int ActualQty) {
        put("itemID", itemID);
        put("itemName", itemName);
        put("Bin", Bin);
        put("RequestQty", RequestQty);
        put("ActualQty", ActualQty);
    }
}

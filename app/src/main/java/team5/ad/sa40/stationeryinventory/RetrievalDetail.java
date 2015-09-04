package team5.ad.sa40.stationeryinventory;


import java.util.HashMap;

public class RetrievalDetail extends HashMap<String,Object>{

    public RetrievalDetail(int RetSN, int RetID, String itemID, String itemName, String Bin,
                           int RequestQty, int ActualQty) {
        put("RetSN", RetSN);
        put("RetID", RetID);
        put("itemID", itemID);
        put("itemName", itemName);
        put("Bin", Bin);
        put("RequestQty", RequestQty);
        put("ActualQty", ActualQty);
    }
}

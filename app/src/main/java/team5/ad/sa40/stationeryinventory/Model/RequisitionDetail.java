package team5.ad.sa40.stationeryinventory.Model;

import java.util.HashMap;

/**
 * Created by johnmajor on 9/4/15.
 */
public class RequisitionDetail extends HashMap<String, Object> {

    public RequisitionDetail(int ReqSN, int ReqID, String itemID, String itemName,
                           int RequestQty, int ActualQty,String ItemStatus) {
        put("ReqSN", ReqSN);
        put("ReqID", ReqID);
        put("itemID", itemID);
        put("itemName", itemName);
        put("RequestQty", RequestQty);
        put("ActualQty", ActualQty);
        put("ItemStatus", ItemStatus);
    }

}

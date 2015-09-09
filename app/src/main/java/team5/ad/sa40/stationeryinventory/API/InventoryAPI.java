package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;

public interface InventoryAPI {

    @GET("/inventoryAPI.svc/getItem")
    void getList(Callback<List<JSONItem>> itemList);

}

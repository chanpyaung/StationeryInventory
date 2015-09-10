package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONItemPrice;
import team5.ad.sa40.stationeryinventory.Model.JSONStockCard;

public interface InventoryAPI {

    @GET("/inventoryAPI.svc/getItem")
    void getList(Callback<List<JSONItem>> itemList);

    @GET("/inventoryAPI.svc/getItemDetails/{ItemID}")
    void getItemDetails(@Path("ItemID") String ItemID, Callback<JSONItem> result);

    @GET("/inventoryAPI.svc/getItemPrice/{ItemID}")
    void getItemPrice(@Path("ItemID") String ItemID, Callback<List<JSONItemPrice>> result);

    @GET("/inventoryAPI.svc/getStockCard/{ItemID}")
    void getStockCard(@Path("ItemID") String ItemID, Callback<List<JSONStockCard>> result);

}

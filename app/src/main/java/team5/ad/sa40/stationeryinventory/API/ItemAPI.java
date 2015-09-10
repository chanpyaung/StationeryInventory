package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;

/**
 * Created by johnmajor on 9/9/15.
 */
public interface ItemAPI {

    @GET("/catalogAPI.svc/getItemCategory/{categoryName}")
    void getItemsByCategory(@Path("categoryName") String categoryName, Callback<List<JSONItem>> itemlist);

    @GET("/catalogAPI.svc/getitem")
    void getItems(Callback<List<JSONItem>> itemlist);

}

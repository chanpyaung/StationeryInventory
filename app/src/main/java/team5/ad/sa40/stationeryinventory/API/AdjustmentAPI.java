package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;

/**
 * Created by student on 11/9/15.
 */
public interface AdjustmentAPI {
    @POST("/adjustvoucherAPI.svc/getAdjVoucher")
    void getAdjVoucher(@Body JsonObject adj, Callback<List<JSONAdjustment>> adjustments);

    @POST("/adjustvoucherAPI.svc/createVoucherAdj")
    void createAdjVoucher(@Body JsonObject adj, Callback<String> result);

    @POST("/adjustvoucherAPI.svc/createVoucherAdjDetail")
    void createAdjVoucherDetail(@Body JsonObject adjDetail, Callback<String> result);

    @POST("/adjustvoucherAPI.svc/getAdjVoucherDetail")
    void getAdjVoucherDetail(@Body JsonObject adj, Callback<List<JSONAdjustmentDetail>> adjustments);
}

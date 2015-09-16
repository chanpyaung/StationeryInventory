package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;

/**
 * Created by student on 16/9/15.
 */
public interface UploadAPI {
    @POST("/Upload.svc/Upload")
    void uploadSignature(@Body JsonObject adj, Callback<String> result);
}

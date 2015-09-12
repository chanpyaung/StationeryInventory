package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONDelegate;

/**
 * Created by student on 12/9/15.
 */
public interface DelegateAPI {
    @GET("/delegateAPI.svc/getDelegate/{DeptID}")
    void getDelegate(@Path("DeptID") String DeptID, Callback<List<JSONDelegate>> delegates);

    @POST("/delegateAPI.svc/createDelegate")
    void createDelegate(@Body JsonObject delegate, Callback<Boolean> result);

    @POST("/delegateAPI.svc/editDelegate")
    void editDelegate(@Body JsonObject delegate, Callback<Boolean> result);

    @GET("/delegateAPI.svc/deleteDelegate/{DelegateSN}")
    void deleteDelegate(@Path("DelegateSN") int delegateSN, Callback<Boolean> result);
    /*
    @POST("/adjustvoucherAPI.svc/createVoucherAdj")
    void createAdjVoucher(@Body JsonObject adj, Callback<String> result);
     */
}

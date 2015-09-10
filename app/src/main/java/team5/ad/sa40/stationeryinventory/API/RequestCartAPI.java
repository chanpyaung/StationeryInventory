package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONRequestCart;

/**
 * Created by johnmajor on 9/10/15.
 */
public interface RequestCartAPI {

    @POST("/requestcartAPI.svc/addItem")
    void addtoCart(@Body JsonObject item, Callback<Boolean> booleanResult);

    @POST("/requestcartAPI.svc/updateItem")
    void updatetoCart(@Body JsonObject item, Callback<Boolean> booleanResult);

    @POST("/requestcartAPI.svc/deleteItem")
    void deletefromCart(@Body JsonObject item, Callback<Boolean> booleanResult);

    @GET("/requestcartAPI.svc/getItems/{EmpID}")
    void getItemsbyEmpID(@Path("EmpID")int empID, Callback<List<JSONRequestCart>> cartItemlist);

}

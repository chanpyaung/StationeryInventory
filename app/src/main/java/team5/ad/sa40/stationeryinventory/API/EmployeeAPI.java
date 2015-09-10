package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonObject;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;

/**
 * Created by johnmajor on 9/9/15.
 */
public interface EmployeeAPI {

    @POST("/employeeAPI.svc/login")
    void login( @Body JsonObject userinfo, Callback<JSONEmployee> emp);

    @GET("/employeeAPI.svc/getemployeebyId/{empID}")
    void getEmployeeById(@Path("empID") int empID, Callback<JSONEmployee> emp);

}

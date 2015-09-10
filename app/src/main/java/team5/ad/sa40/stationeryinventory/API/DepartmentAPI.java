package team5.ad.sa40.stationeryinventory.API;

import com.google.gson.JsonObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONDepartment;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursementDetail;

/**
 * Created by student on 10/9/15.
 */
public interface DepartmentAPI {

    @GET("/departmentAPI.svc/getAllDepartment")
    void getAllDepartments(Callback<List<JSONDepartment>> deList);

    @GET("/departmentAPI.svc/getDeptByID/{DeptID}")
    void getDepartmentByDeptID(@Path("DeptID") String DeptID, Callback<JSONDepartment> dept);

    @POST("/departmentAPI.svc/updateDept")
    void updateDepartment(@Body JsonObject dept, Callback<String> flag);

}

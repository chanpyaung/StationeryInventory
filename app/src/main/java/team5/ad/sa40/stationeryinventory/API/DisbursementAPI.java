package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;

/**
 * Created by student on 10/9/15.
 */
public interface DisbursementAPI {
    @GET("/disbursementAPI.svc/getDisbursement/{DeptID}/null/null/null/null")
    void getDisbursementByDeptID(@Path("DeptID") String DeptID, Callback<List<JSONDisbursement>> disbursementList);

    @GET("/disbursementAPI.svc/getDisbursement/{DeptID}/null/null/{StartDate}/{EndDate}")
    void getDisbursementByDates(@Path("DeptID") String DeptID, @Path("StartDate") String StartDate, @Path("EndDate") String EndDate, Callback<List<JSONDisbursement>> disbursementList);
}

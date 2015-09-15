package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursementDetail;

/**
 * Created by student on 10/9/15.
 */
public interface DisbursementAPI {
    @GET("/disbursementAPI.svc/getDisbursement/{DeptID}/null/null/null/null")
    void getDisbursementByDeptID(@Path("DeptID") String DeptID, Callback<List<JSONDisbursement>> disbursementList);

    @GET("/disbursementAPI.svc/getDisbursement/null/null/{DisID}/null/null")
    void getDisbursementByDisID(@Path("DisID") int DisID, Callback<List<JSONDisbursement>> disbursementList);

    @GET("/disbursementAPI.svc/getDisbursement/{DeptID}/null/null/{StartDate}/{EndDate}")
    void getDisbursementByDates(@Path("DeptID") String DeptID, @Path("StartDate") String StartDate, @Path("EndDate") String EndDate, Callback<List<JSONDisbursement>> disbursementList);

    @GET("/disbursementAPI.svc/getDisbursementDetail/{DisID}")
    void getDisbursementDetail(@Path("DisID") int DisID, Callback<List<JSONDisbursementDetail>> disList);

    @GET("/disbursementAPI.svc/getDisbursement/null/null/null/null/null")
    void getAllDisbursements(Callback<List<JSONDisbursement>> disbursementList);

    @GET("/disbursementAPI.svc/completeDisbursement/{DisID}")
    void completeDisbursement(@Path("DisID") int DisID, Callback<Boolean> result);
}

package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;

/**
 * Created by johnmajor on 9/12/15.
 */
public interface RequisitionAPI {
    @GET("/requisitionAPI.svc/getRequisition/null/null/{EmpID}")
    void getRequisition(@Path("EmpID") int empID, Callback<List<JSONRequisition>> requisitionList);

    @GET("/requisitionAPI.svc/getRequisition/null/null/null")
    void getRequisitionFromSC(Callback<List<JSONRequisition>> requisitionList);

    @GET("/requisitionAPI.svc/getStatus")
    void getStatus(Callback<List<JSONStatus>> statusList);

    @GET("/requisitionAPI.svc/getRequisitionDetail/{ReqID}")
    void getReqDetail(@Path("ReqID") int reqID, Callback<List<JSONReqDetail>> reqDetailList);

}

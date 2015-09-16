package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.Model.JSONReportItem;

/**
 * Created by johnmajor on 9/16/15.
 */
public interface ReportAPI {

    @GET("/AnalyticsAPI.svc/getReports")
    void getReports(Callback<List<JSONReport>> jsonreportList);

    @GET("/AnalyticsAPI.svc/generateExistingReportStyle2/{ReportID}")
    void getReportItems(@Path("ReportID") int reportID, Callback<List<JSONReportItem>> jsonreportitemList);
}

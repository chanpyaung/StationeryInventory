package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;

public interface NotificationAPI {

    @GET("/notificationAPI.svc/getNotification/{EmpID}")
    void getItemsByCategory(@Path("EmpID") String EmpID, Callback<List<JSONNotification>> notification);

}

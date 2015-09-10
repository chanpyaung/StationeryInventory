package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;

public interface NotificationAPI {

    @GET("/notificationAPI.svc/getNotification/{EmpID}")
    void getList(@Path("EmpID") String EmpID, Callback<List<JSONNotification>> notification);

    @GET("/notificationAPI.svc/changeStatusToRead/{NotifID}")
    void changeStatus(@Path("NotifID") String NotifID, Callback<String> result);

}

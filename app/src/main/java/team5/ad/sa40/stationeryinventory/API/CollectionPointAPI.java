package team5.ad.sa40.stationeryinventory.API;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;

/**
 * Created by student on 10/9/15.
 */
public interface CollectionPointAPI {
    @GET("/collectionAPI.svc/getCollectionPoint")
    void getAllCollectionPoints(Callback<List<JSONCollectionPoint>> collectionList);
}

package team5.ad.sa40.stationeryinventory.API;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import team5.ad.sa40.stationeryinventory.Model.JSONSupplier;

public interface SupplierAPI {

    @GET("/supplierAPI.svc/getBySupplierID/{SupplierID}")
    void getSupplierDetails(@Path("SupplierID") String SupplierID, Callback<JSONSupplier> result);

}

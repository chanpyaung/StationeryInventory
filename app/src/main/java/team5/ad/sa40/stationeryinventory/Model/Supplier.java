package team5.ad.sa40.stationeryinventory.Model;


import java.util.HashMap;

public class Supplier extends HashMap<String,Object>{

    public Supplier(String supplierID, int phone) {
        put("SupplierID",supplierID);
        put("Phone",phone);
    }
}

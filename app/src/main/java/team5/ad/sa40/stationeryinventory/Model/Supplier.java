package team5.ad.sa40.stationeryinventory.Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Supplier extends HashMap<String,Object>{

    public Supplier(String supplierID, int phone) {
        put("SupplierID",supplierID);
        put("Phone",phone);
    }

    public static List<Supplier> initializeData(){
        List<Supplier> supplierList = new ArrayList<Supplier>();
        Supplier s = new Supplier ("BANES",91013118);
        Supplier s2 = new Supplier ("CHEP",91013118);
        Supplier s3 = new Supplier ("ALPHA",91013118);
        supplierList.add(s);
        supplierList.add(s2);
        supplierList.add(s3);
        return supplierList;
    }
}

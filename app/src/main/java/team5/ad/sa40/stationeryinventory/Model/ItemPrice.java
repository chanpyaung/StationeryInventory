package team5.ad.sa40.stationeryinventory.Model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ItemPrice extends HashMap<String,Object> {

    public ItemPrice(String itemID, double price, String supplierID) {
        put("ItemID",itemID);
        put("Price",price);
        put("SupplierID",supplierID);
    }

    public static List<ItemPrice> getAllItemPrices(String itemID) {
        List<ItemPrice> itemPriceList = new ArrayList<ItemPrice>();
        //fake data:
        ItemPrice ip = new ItemPrice(itemID,1.20,"ALPHA");
        itemPriceList.add(ip);
        ItemPrice ip2 = new ItemPrice(itemID,1.35,"BANE");
        itemPriceList.add(ip2);
        ItemPrice ip3 = new ItemPrice(itemID,1.55,"OMEGA");
        itemPriceList.add(ip3);

        return itemPriceList;
    }
}

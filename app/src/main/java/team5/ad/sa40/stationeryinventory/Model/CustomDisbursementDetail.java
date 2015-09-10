package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;

/**
 * Created by student on 10/9/15.
 */
public class CustomDisbursementDetail implements Serializable{
    private String itemName;
    private int itemQty;

    public CustomDisbursementDetail(){}
    public CustomDisbursementDetail(String name, int qty){
        itemName = name; itemQty = qty;
    }

    public String getItemName(){return itemName;}
    public int getItemQty(){return itemQty;}

    public void setItemName(String name){itemName = name;}
    public void setItemQty(int qty){itemQty = qty;}
}

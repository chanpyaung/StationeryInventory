package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;

/**
 * Created by student on 6/9/15.
 */
public class AdjustmentDetail implements Serializable{

    private int adjSN;
    private String adjustmentID;
    private String itemID;
    private int quantity;
    private float price;
    private String reason;
    private String remark;

    public AdjustmentDetail(){

    }

    public AdjustmentDetail(int sn, String id, String item, int qty, float pri, String reas, String rem){
        adjSN = sn;
        adjustmentID = id;
        itemID = item;
        quantity = qty;
        price = pri;
        reason = reas;
        remark = rem;
    }

    public int getAdjSN(){
        return adjSN;
    }
    public String getAdjustmentID(){
        return adjustmentID;
    }
    public String getItemID(){
        return itemID;
    }
    public int getQuantity(){
        return quantity;
    }
    public float getPrice(){
        return price;
    }
    public String getReason(){
        return reason;
    }
    public String getRemark(){
        return remark;
    }

    public void setAdjSN(int sn){
        adjSN = sn;
    }
    public void setAdjustmentID(String id){
        adjustmentID = id;
    }
    public void setItemID(String id){
        itemID = id;
    }
    public void setQuantity(int qty){
        quantity = qty;
    }
    public void setPrice(float pri){
        price = pri;
    }
    public void setReason(String rea){
        reason = rea;
    }
    public void setRemark(String rem){
        remark = rem;
    }
}

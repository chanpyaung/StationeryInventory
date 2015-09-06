package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by student on 6/9/15.
 */
public class Adjustment implements Serializable{

    private String adjustmentID;
    private Date date;
    private int reportedBy;
    private int approvedBy;
    private String status;
    private float totalAmount;

    public Adjustment(){

    }

    public Adjustment(String id, Date _date, int report, int approve, String stat, float amt){

        adjustmentID = id;
        date = _date;
        reportedBy = report;
        approvedBy = approve;
        status = stat;
        totalAmount = amt;
    }

    public String getAdjustmentID(){
        return adjustmentID;
    }
    public Date getDate(){
        return date;
    }
    public int getReportedBy(){
        return reportedBy;
    }
    public int getApprovedBy(){
        return approvedBy;
    }
    public String getStatus(){
        return status;
    }
    public float getTotalAmount(){
        return totalAmount;
    }

    public void setAdjustmentID(String id){
        adjustmentID = id;
    }
    public void setDate(Date _date){
        date = _date;
    }
    public void setReportedBy(int report){
        reportedBy = report;
    }
    public void setApprovedBy(int approve){
        approvedBy = approve;
    }
    public void setStatus(String stat){
        status = stat;
    }
    public void setTotalAmount(float amt){
        totalAmount = amt;
    }

    public static ArrayList<Adjustment> getAllAdjustments(){
        ArrayList<Adjustment> adj_list = new ArrayList<>();

        for (int i=1; i<6; i++){
            Adjustment adj = new Adjustment("Adj" + i, new Date(), i+1, i+6, "Pending", (i*i));
            adj_list.add(adj);
        }

        return adj_list;
    }
}

package team5.ad.sa40.stationeryinventory.Model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johnmajor on 9/4/15.
 */
public class Requisition {

    private int reqID;
    private int disID;
    private int retID;
    private int empID;
    private String deptID;
    private Date date;
    private int priorityID;
    private String pRemark;
    private int statusID;
    private int handledBy;
    private String remark;
    private int receivedBy;

    public int getReqID() {
        return reqID;
    }

    public void setReqID(int reqID) {
        this.reqID = reqID;
    }

    public int getDisID() {
        return disID;
    }

    public void setDisID(int disID) {
        this.disID = disID;
    }

    public int getRetID() {
        return retID;
    }

    public void setRetID(int retID) {
        this.retID = retID;
    }

    public int getEmpID() {
        return empID;
    }

    public void setEmpID(int empID) {
        this.empID = empID;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPriorityID() {
        return priorityID;
    }

    public void setPriorityID(int priorityID) {
        this.priorityID = priorityID;
    }

    public String getpRemark() {
        return pRemark;
    }

    public void setpRemark(String pRemark) {
        this.pRemark = pRemark;
    }

    public int getStatusID() {
        return statusID;
    }

    public void setStatusID(int statusID) {
        this.statusID = statusID;
    }

    public int getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(int handledBy) {
        this.handledBy = handledBy;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(int receivedBy) {
        this.receivedBy = receivedBy;
    }

    public static List<Requisition> initializeData(){
        List<Requisition> requisitionList = new ArrayList<Requisition>();
        int i = 0;
        Requisition request = new Requisition();
        request.setReqID(i+1);
        request.setDate(new Date());
        request.setStatusID(1);
        request.setDeptID("   ENG");
        requisitionList.add(request);
        i++;
        do {
            Requisition rq = new Requisition();
            rq.setReqID(i+1);
            rq.setDate(new Date());
            if (i<=3){
                rq.setStatusID(1);
                rq.setDeptID("   SCIENCE");
            }
            else if (i>3 && i<=5){
                rq.setStatusID(2);
                rq.setDeptID("   RGR");
            }
            else if (i>5 && i<=8){
                rq.setStatusID(3);
                rq.setDeptID("   BIZ");
            }
            else if (i>8){

                rq.setStatusID(4);
                rq.setDeptID("   COM");
            }
            requisitionList.add(rq);
            i++;
        } while (i<10);

        return requisitionList;
    }

    //add in JSON-Obj methods
}

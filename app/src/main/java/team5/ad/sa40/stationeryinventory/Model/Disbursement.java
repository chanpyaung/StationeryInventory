package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by student on 2/9/15.
 */
public class Disbursement implements Serializable {

    private int disbursementId;
    private Date disbursementDate;
    private String disbursementStatus;
    private String deptID;
    private int empID;
    private int receivedby;
    private int disbursement_colID;

    public int getDisbursementId(){

        return disbursementId;
    }
    public Date getDisbursementDate(){

        return disbursementDate;
    }
    public String getDisbursementStatus(){

        return disbursementStatus;
    }
    public int getDisbursement_colID(){

        return disbursement_colID;
    }
    public String getDeptID(){

        return deptID;
    }
    public int getEmpID(){

        return empID;
    }
    public int getReceivedby(){

        return receivedby;
    }

    public void setDisbursementId(int id){

        disbursementId = id;
    }
    public void setDisbursementDate(Date date){

        disbursementDate = date;
    }
    public void setDisbursementStatus(String status){

        disbursementStatus = status;
    }
    public void setDisbursement_colID(int id){

        disbursement_colID = id;
    }
    public void setDeptID(String dept){

        deptID = dept;
    }
    public void setEmpID(int emp){

        empID = emp;
    }
    public void setReceivedby(int receID){

        receivedby = receID;
    }

    public static ArrayList<Disbursement> getAllDisbursement(){
        ArrayList<Disbursement> temp = new ArrayList<>();

        for (int i = 0; i < 5; i++){

            Disbursement dis = new Disbursement();
            dis.setDisbursementId(i+1);
            dis.setDisbursementDate(new Date());
            dis.setDisbursementStatus("Pending");
            dis.setDisbursement_colID(i);
            temp.add(dis);
        }
        return temp;
    }
}

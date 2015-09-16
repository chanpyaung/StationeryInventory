package team5.ad.sa40.stationeryinventory.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import team5.ad.sa40.stationeryinventory.Utilities.JSONParser;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

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

    public Disbursement(){}
    public Disbursement(int id, Date date, int emp, String dept, int col, int received, String status){
        disbursementId = id;
        disbursementDate = date;
        empID = emp;
        deptID = dept;
        disbursement_colID = col;
        receivedby = received;
        disbursementStatus = status;
    }

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

        ArrayList<Disbursement> list = new ArrayList<>();
        JSONArray temp = JSONParser.getJSONArrayFromUrl(Setup.baseurl + "/");
        try{
            for(int i = 0; i < temp.length(); i++){
                JSONObject dis = temp.getJSONObject(i);
                list.add(new Disbursement());
            }
        }catch (Exception e){
            Log.e("Disbursement", "JSONArray error");
        }
        return list;
    }
}

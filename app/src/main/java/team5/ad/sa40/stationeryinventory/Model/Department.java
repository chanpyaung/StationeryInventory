package team5.ad.sa40.stationeryinventory.Model;

import android.util.Log;

import org.json.JSONObject;

import java.io.Serializable;

import team5.ad.sa40.stationeryinventory.JSONParser;
import team5.ad.sa40.stationeryinventory.Setup;

/**
 * Created by student on 8/9/15.
 */
public class Department implements Serializable{

    private String deptID;
    private String deptName;
    private int cpID;
    private int contact;
    private int deptHead;
    private int deptRep;
    private int phone;
    private int fax;

    public Department(){}
    public Department(String _id, String _name, int _cp, int _contact, int _deptHead, int _deptRep, int _phone, int _fax){
        deptID = _id;
        deptName = _name;
        cpID = _cp;
        contact = _contact;
        deptHead = _deptHead;
        deptRep = _deptRep;
        phone = _phone;
        fax = _fax;
    }

    public String getDeptID(){return deptID;}
    public String getDeptName(){return deptName;}
    public int getCpID(){return cpID;}
    public int getContact(){return contact;}
    public int getDeptHead(){return deptHead;}
    public int getDeptRep(){return deptRep;}
    public int getPhone(){return phone;}
    public int getFax(){return fax;}

    public void setDeptID(String _id){deptID = _id;}
    public void setDeptName(String _name){deptName = _name;}
    public void setCpID(int _cp){cpID = _cp;}
    public void setContact(int _contact){contact = _contact;}
    public void setDeptHead(int _deptHead){deptHead = _deptHead;}
    public void setDeptRep(int _deptRep){deptRep = _deptRep;}
    public void setPhone(int _phone){phone = _phone;}
    public void setFax(int _fax){fax = _fax;}

    public static Department getDeptByID(String _deptID){
        JSONObject temp = JSONParser.getJSONFromUrl(Setup.baseurl + "/departmentAPI.svc/getDeptByID/" + _deptID);
        Log.i("Url is ", Setup.baseurl + "/departmentAPI.svc/getDeptByID/" + _deptID);
        Department dept = null;
        try{
            dept = new Department(temp.getString("DeptID"), temp.getString("DeptName"), temp.getInt("CPID"), temp.getInt("Contact"),
                    temp.getInt("DeptHead"), temp.getInt("DeptRep"), temp.getInt("Phone"), temp.getInt("Fax"));
        }
        catch (Exception e){
            Log.e("getDeptByID", "JSONError");
        }
        return dept;
    }

    public static String updateDepartment(Department dept){
        String temp = "";
        try{
            JSONObject obj = new JSONObject();
            obj.put("DeptID", dept.getDeptID());
            obj.put("DeptName", dept.getDeptName());
            obj.put("DeptRep", dept.getDeptRep());
            obj.put("DeptHead", dept.getDeptHead());
            obj.put("Contact", dept.getContact());
            obj.put("CPID", dept.getCpID());
            obj.put("Fax", dept.getFax());
            obj.put("Phone", dept.getPhone());

            Log.i("JSON String", obj.toString());
            String url = Setup.baseurl + "/departmentAPI.svc/updateDept";

            temp = JSONParser.getJSONFromUrlPOST2(url, obj.toString());
        }catch (Exception e){
            Log.e("updateDepartment", "JSONError");
        }
        return temp;
    }
    /*
     public static ArrayList<Disbursement> getAllDisbursement(){

        ArrayList<Disbursement> list = new ArrayList<>();
        JSONArray temp = JSONParser.getJSONArrayFromUrl(Setup.baseurl + "/disbursement.svc/");
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
     */
}

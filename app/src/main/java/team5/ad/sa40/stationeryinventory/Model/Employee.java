package team5.ad.sa40.stationeryinventory.Model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

import team5.ad.sa40.stationeryinventory.Utilities.JSONParser;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;


public class Employee implements Serializable {

    private int empID;
    private String empName;
    private String deptID;
    private String roleID;
    private int phone;
    private String email;
    private String password;


    public Employee(int _id, String _name, String _deptID, String _roleID, int _phone, String _email, String _password) {
        empID = _id;
        empName = _name;
        deptID = _deptID;
        roleID = _roleID;
        phone = _phone;
        email = _email;
        password = _password;
    }

    public Employee() {
    }

    public int getEmpID(){return empID;}
    public String getEmpName(){return empName;}
    public String getDeptID(){return deptID;}
    public String getRoleID(){return roleID;}
    public int getPhone(){return phone;}
    public String getEmail(){return email;}
    public String getPassword(){return password;}

    public void setEmpID(int id){empID = id;}
    public void setEmpName(String name){empName = name;}
    public void setDeptID(String dept){deptID = dept;}
    public void setRoleID(String role){roleID = role;}
    public void setPhone(int _phone){phone = _phone;}
    public void setEmail(String _email){email = _email;}
    public void setPassword(String _password){password = _password;}

    public static ArrayList<Employee> getEmployeeByDept(String deptID){
        ArrayList<Employee> employees = new ArrayList<>();
        JSONArray temp = JSONParser.getJSONArrayFromUrl(Setup.baseurl + "/employeeAPI.svc/getemployeeByDeptID/" + deptID);
        try{
            for(int i = 0; i < temp.length(); i++){
                JSONObject obj = temp.getJSONObject(i);
                employees.add(new Employee(obj.getInt("EmpID"), obj.getString("EmpName"), obj.getString("DeptID"), obj.getString("RoleID"),
                        obj.getInt("Phone"), obj.getString("Email"), obj.getString("Password")));
            }

        }catch (Exception e){
            Log.e("getEmployeeByDept", "JSONError");
        }
        return employees;
    }

    public static String updateEmployeeRole(String empID, String roleID){
        String temp = JSONParser.getJSONFromUrl2(Setup.baseurl + "/employeeAPI.svc/updateEmployeeRole/" + empID + "/" + roleID);
        String flag = "";
        Log.e("url", Setup.baseurl + "/employeeAPI.svc/updateEmployeeRole/" + empID + "/" + roleID);
        try{
            Log.e("String value in test", temp);
            flag = temp;
        }catch (Exception e){
            Log.e("updateEmployeeRole","JSONError");
        }
        return flag;
    }
    /*
      public static ArrayList<CollectionPoint> getAllCollectionPoints(){
        ArrayList<CollectionPoint> temp_colPt = new ArrayList<>();
        JSONArray temp = JSONParser.getJSONArrayFromUrl(Setup.baseurl + "/collectionAPI.svc/getCollectionPoint");
        try{
            for(int i = 0; i < temp.length(); i++){
                JSONObject obj = temp.getJSONObject(i);
                temp_colPt.add(new CollectionPoint(obj.getInt("CPID"), obj.getString("CPName"), obj.getString("CPAddress"), (float)obj.getDouble("CPLat"),(float)obj.getDouble("CPLgt")));
            }
        }catch (Exception e){
            Log.e("getAllCollectionPoints", "JSONError");
        }
        return temp_colPt;
    }
     */
}

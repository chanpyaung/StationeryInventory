package team5.ad.sa40.stationeryinventory;

import java.util.HashMap;


public class Employee extends HashMap<String, String> {

    public Employee(String EmpID, String EmpName, String DeptID, String RoleID, int Phone, String Email, String Password) {
        put("EmpID", EmpID);
        put("EmpName", EmpName);
        put("DeptID", DeptID);
        put("RoleID", RoleID);
        put("Phone", Integer.toString(Phone));
        put("Email", Email);
        put("Password", Password);
    }

}

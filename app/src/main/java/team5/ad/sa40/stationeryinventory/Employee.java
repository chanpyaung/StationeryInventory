package team5.ad.sa40.stationeryinventory;

import java.util.HashMap;


public class Employee extends HashMap<String, String> {

    public static String EmpID;
    public static String DeptID;
    public static String RoleID;


    public Employee(String EmpID, String EmpName, String DeptID, String RoleID, int Phone, String Email) {
        this.EmpID = EmpID;
        put("EmpName", EmpName);
        this.DeptID = DeptID;
        this.RoleID = RoleID;
        put("Phone", Integer.toString(Phone));
        put("Email", Email);
    }

    public Employee() {
    }

}

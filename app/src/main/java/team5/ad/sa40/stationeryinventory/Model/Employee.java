package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;
import java.util.HashMap;


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
}

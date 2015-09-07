package team5.ad.sa40.stationeryinventory.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by student on 7/9/15.
 */
public class Delegate implements Serializable{

    private int delegateSN;
    private int empID;
    private String deptID;
    private Date startDate;
    private Date endDate;
    private String status;
    private String empName;

    public Delegate(){}
    public Delegate(int sn, int id, String dept, Date start, Date end, String stat){
        delegateSN = sn; empID = id; deptID = dept; startDate = start; endDate = end; status = stat;
    }

    public int getDelegateSN(){return delegateSN;}
    public int getEmpID(){return empID;}
    public String getDeptID(){return deptID;}
    public Date getStartDate(){return startDate;}
    public Date getEndDate(){return endDate;}
    public String getStatus(){return status;}
    public String getEmpName(){return empName;}

    public void setDelegateSN(int sn){delegateSN = sn;}
    public void setEmpID(int id){empID = id;}
    public void setDeptID(String dept){deptID = dept;}
    public void setStartDate(Date start){startDate = start;}
    public void setEndDate(Date end){endDate = end;}
    public void setStatus(String stat){status = stat;}
    public void setEmpName(String emp){empName = emp;}

    public static ArrayList<Delegate> getAllDelegation(){
        ArrayList<Delegate> temp = new ArrayList<Delegate>();
        String[] EmpNames = {"Erick", "John", "David", "Wayne", "Ryan"};
        for(int i = 1; i < 6; i++){
            Delegate del = new Delegate(i, i, "Dept"+i, new Date(), new Date(), "Pending");
            del.setEmpName(EmpNames[i-1]);
            temp.add(del);
        }
        return temp;
    }

}

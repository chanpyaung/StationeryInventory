package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class JSONDelegate implements Serializable{

    @Expose
    private Integer DelegateSN;
    @Expose
    private String DeptID;
    @Expose
    private Integer EmpID;
    @Expose
    private String EndDate;
    @Expose
    private String StartDate;
    @Expose
    private String Status;

    /**
     *
     * @return
     * The DelegateSN
     */
    public Integer getDelegateSN() {
        return DelegateSN;
    }

    /**
     *
     * @param DelegateSN
     * The DelegateSN
     */
    public void setDelegateSN(Integer DelegateSN) {
        this.DelegateSN = DelegateSN;
    }

    /**
     *
     * @return
     * The DeptID
     */
    public String getDeptID() {
        return DeptID;
    }

    /**
     *
     * @param DeptID
     * The DeptID
     */
    public void setDeptID(String DeptID) {
        this.DeptID = DeptID;
    }

    /**
     *
     * @return
     * The EmpID
     */
    public Integer getEmpID() {
        return EmpID;
    }

    /**
     *
     * @param EmpID
     * The EmpID
     */
    public void setEmpID(Integer EmpID) {
        this.EmpID = EmpID;
    }

    /**
     *
     * @return
     * The EndDate
     */
    public String getEndDate() {
        return EndDate;
    }

    /**
     *
     * @param EndDate
     * The EndDate
     */
    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    /**
     *
     * @return
     * The StartDate
     */
    public String getStartDate() {
        return StartDate;
    }

    /**
     *
     * @param StartDate
     * The StartDate
     */
    public void setStartDate(String StartDate) {
        this.StartDate = StartDate;
    }

    /**
     *
     * @return
     * The Status
     */
    public String getStatus() {
        return Status;
    }

    /**
     *
     * @param Status
     * The Status
     */
    public void setStatus(String Status) {
        this.Status = Status;
    }
}

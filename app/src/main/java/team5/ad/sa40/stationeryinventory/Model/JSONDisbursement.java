package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class JSONDisbursement implements Serializable, Comparable{

    @Expose
    private Integer CPID;
    @Expose
    private String Date;
    @Expose
    private String DeptID;
    @Expose
    private Integer DisID;
    @Expose
    private Integer EmpID;
    @Expose
    private Integer ReceivedBy;
    @Expose
    private String Status;

    /**
     *
     * @return
     * The CPID
     */
    public Integer getCPID() {
        return CPID;
    }

    /**
     *
     * @param CPID
     * The CPID
     */
    public void setCPID(Integer CPID) {
        this.CPID = CPID;
    }

    /**
     *
     * @return
     * The Date
     */
    public String getDate() {
        return Date;
    }

    /**
     *
     * @param Date
     * The Date
     */
    public void setDate(String Date) {
        this.Date = Date;
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
     * The DisID
     */
    public Integer getDisID() {
        return DisID;
    }

    /**
     *
     * @param DisID
     * The DisID
     */
    public void setDisID(Integer DisID) {
        this.DisID = DisID;
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
     * The ReceivedBy
     */
    public Integer getReceivedBy() {
        return ReceivedBy;
    }

    /**
     *
     * @param ReceivedBy
     * The ReceivedBy
     */
    public void setReceivedBy(Integer ReceivedBy) {
        this.ReceivedBy = ReceivedBy;
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

    public int compareTo(Object o) {

        JSONDisbursement f = (JSONDisbursement) o;

        if (getDisID().compareTo(f.getDisID()) < 0) {
            return 1;
        }
        else if (getDisID().compareTo(f.getDisID()) > 0) {
            return -1;
        }
        else {
            return 0;
        }

    }
}
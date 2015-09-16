package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/12/15.
 */
public class JSONRequisition implements  Comparable {
    @Expose
    private String Date;
    @Expose
    private String DeptID;
    @Expose
    private Integer DisID;
    @Expose
    private Integer EmpID;
    @Expose
    private Integer HandledBy;
    @Expose
    private String PRemark;
    @Expose
    private Integer PriorityID;
    @Expose
    private Integer ReceivedBy;
    @Expose
    private String Remark;
    @Expose
    private Integer ReqID;
    @Expose
    private Integer RetID;
    @Expose
    private Integer StatusID;

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
     * The HandledBy
     */
    public Integer getHandledBy() {
        return HandledBy;
    }

    /**
     *
     * @param HandledBy
     * The HandledBy
     */
    public void setHandledBy(Integer HandledBy) {
        this.HandledBy = HandledBy;
    }

    /**
     *
     * @return
     * The PRemark
     */
    public String getPRemark() {
        return PRemark;
    }

    /**
     *
     * @param PRemark
     * The PRemark
     */
    public void setPRemark(String PRemark) {
        this.PRemark = PRemark;
    }

    /**
     *
     * @return
     * The PriorityID
     */
    public Integer getPriorityID() {
        return PriorityID;
    }

    /**
     *
     * @param PriorityID
     * The PriorityID
     */
    public void setPriorityID(Integer PriorityID) {
        this.PriorityID = PriorityID;
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
     * The Remark
     */
    public String getRemark() {
        return Remark;
    }

    /**
     *
     * @param Remark
     * The Remark
     */
    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    /**
     *
     * @return
     * The ReqID
     */
    public Integer getReqID() {
        return ReqID;
    }

    /**
     *
     * @param ReqID
     * The ReqID
     */
    public void setReqID(Integer ReqID) {
        this.ReqID = ReqID;
    }

    /**
     *
     * @return
     * The RetID
     */
    public Integer getRetID() {
        return RetID;
    }

    /**
     *
     * @param RetID
     * The RetID
     */
    public void setRetID(Integer RetID) {
        this.RetID = RetID;
    }

    /**
     *
     * @return
     * The StatusID
     */
    public Integer getStatusID() {
        return StatusID;
    }

    /**
     *
     * @param StatusID
     * The StatusID
     */
    public void setStatusID(Integer StatusID) {
        this.StatusID = StatusID;
    }

    public int compareTo(Object o) {

        JSONRequisition f = (JSONRequisition) o;

        if (getReqID().compareTo(f.getReqID()) < 0) {
            return 1;
        }
        else if (getReqID().compareTo(f.getReqID()) > 0) {
            return -1;
        }
        else {
            return 0;
        }

    }
}


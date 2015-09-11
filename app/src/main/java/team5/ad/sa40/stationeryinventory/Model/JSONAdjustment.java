package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class JSONAdjustment implements Serializable{

    @Expose
    private String AdjID;
    @Expose
    private Integer ApprovedBy;
    @Expose
    private String Date;
    @Expose
    private Integer ReportedBy;
    @Expose
    private String Status;
    @Expose
    private Double TotalAmt;

    /**
     *
     * @return
     * The AdjID
     */
    public String getAdjID() {
        return AdjID;
    }

    /**
     *
     * @param AdjID
     * The AdjID
     */
    public void setAdjID(String AdjID) {
        this.AdjID = AdjID;
    }

    /**
     *
     * @return
     * The ApprovedBy
     */
    public Integer getApprovedBy() {
        return ApprovedBy;
    }

    /**
     *
     * @param ApprovedBy
     * The ApprovedBy
     */
    public void setApprovedBy(Integer ApprovedBy) {
        this.ApprovedBy = ApprovedBy;
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
     * The ReportedBy
     */
    public Integer getReportedBy() {
        return ReportedBy;
    }

    /**
     *
     * @param ReportedBy
     * The ReportedBy
     */
    public void setReportedBy(Integer ReportedBy) {
        this.ReportedBy = ReportedBy;
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

    /**
     *
     * @return
     * The TotalAmt
     */
    public Double getTotalAmt() {
        return TotalAmt;
    }

    /**
     *
     * @param TotalAmt
     * The TotalAmt
     */
    public void setTotalAmt(Double TotalAmt) {
        this.TotalAmt = TotalAmt;
    }

}
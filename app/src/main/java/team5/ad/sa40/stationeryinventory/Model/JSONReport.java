package team5.ad.sa40.stationeryinventory.Model;

/**
 * Created by johnmajor on 9/16/15.
 */
import com.google.gson.annotations.Expose;

public class JSONReport implements Comparable {
    @Expose
    private String Criteria;
    @Expose
    private String Date;
    @Expose
    private Integer EmpID;
    @Expose
    private String EndD;
    @Expose
    private Integer Precriteria;
    @Expose
    private String Remark;
    @Expose
    private Integer ReportID;
    @Expose
    private String StartD;
    @Expose
    private String Title;
    @Expose
    private Integer Type;

    /**
     *
     * @return
     * The Criteria
     */
    public String getCriteria() {
        return Criteria;
    }

    /**
     *
     * @param Criteria
     * The Criteria
     */
    public void setCriteria(String Criteria) {
        this.Criteria = Criteria;
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
     * The EndD
     */
    public String getEndD() {
        return EndD;
    }

    /**
     *
     * @param EndD
     * The EndD
     */
    public void setEndD(String EndD) {
        this.EndD = EndD;
    }

    /**
     *
     * @return
     * The Precriteria
     */
    public Integer getPrecriteria() {
        return Precriteria;
    }

    /**
     *
     * @param Precriteria
     * The Precriteria
     */
    public void setPrecriteria(Integer Precriteria) {
        this.Precriteria = Precriteria;
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
     * The ReportID
     */
    public Integer getReportID() {
        return ReportID;
    }

    /**
     *
     * @param ReportID
     * The ReportID
     */
    public void setReportID(Integer ReportID) {
        this.ReportID = ReportID;
    }

    /**
     *
     * @return
     * The StartD
     */
    public String getStartD() {
        return StartD;
    }

    /**
     *
     * @param StartD
     * The StartD
     */
    public void setStartD(String StartD) {
        this.StartD = StartD;
    }

    /**
     *
     * @return
     * The Title
     */
    public String getTitle() {
        return Title;
    }

    /**
     *
     * @param Title
     * The Title
     */
    public void setTitle(String Title) {
        this.Title = Title;
    }

    /**
     *
     * @return
     * The Type
     */
    public Integer getType() {
        return Type;
    }

    /**
     *
     * @param Type
     * The Type
     */
    public void setType(Integer Type) {
        this.Type = Type;
    }

    public int compareTo(Object o) {

        JSONReport f = (JSONReport) o;

        if (getReportID().compareTo(f.getReportID()) < 0) {
            return 1;
        }
        else if (getReportID().compareTo(f.getReportID()) > 0) {
            return -1;
        }
        else {
            return 0;
        }

    }
}

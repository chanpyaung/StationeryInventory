package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONNotification {

    @Expose
    private String DateTime;
    @Expose
    private Integer EmpID;
    @Expose
    private String NotifDesc;
    @Expose
    private Integer NotifID;
    @Expose
    private String NotifName;
    @Expose
    private String Status;

    /**
     *
     * @return
     * The DateTime
     */
    public String getDateTime() {
        return DateTime;
    }

    /**
     *
     * @param DateTime
     * The DateTime
     */
    public void setDateTime(String DateTime) {
        this.DateTime = DateTime;
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
     * The NotifDesc
     */
    public String getNotifDesc() {
        return NotifDesc;
    }

    /**
     *
     * @param NotifDesc
     * The NotifDesc
     */
    public void setNotifDesc(String NotifDesc) {
        this.NotifDesc = NotifDesc;
    }

    /**
     *
     * @return
     * The NotifID
     */
    public Integer getNotifID() {
        return NotifID;
    }

    /**
     *
     * @param NotifID
     * The NotifID
     */
    public void setNotifID(Integer NotifID) {
        this.NotifID = NotifID;
    }

    /**
     *
     * @return
     * The NotifName
     */
    public String getNotifName() {
        return NotifName;
    }

    /**
     *
     * @param NotifName
     * The NotifName
     */
    public void setNotifName(String NotifName) {
        this.NotifName = NotifName;
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
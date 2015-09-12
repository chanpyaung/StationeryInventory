package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/12/15.
 */
public class JSONStatus {
    @Expose
    private Integer StatusID;
    @Expose
    private String StatusName;

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

    /**
     *
     * @return
     * The StatusName
     */
    public String getStatusName() {
        return StatusName;
    }

    /**
     *
     * @param StatusName
     * The StatusName
     */
    public void setStatusName(String StatusName) {
        this.StatusName = StatusName;
    }
}

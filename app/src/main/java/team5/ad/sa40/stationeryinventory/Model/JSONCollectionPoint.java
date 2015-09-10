package team5.ad.sa40.stationeryinventory.Model;

/**
 * Created by johnmajor on 9/9/15.
 */
import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class JSONCollectionPoint implements Serializable{

    @Expose
    private String CPAddress;
    @Expose
    private Integer CPID;
    @Expose
    private Double CPLat;
    @Expose
    private Double CPLgt;
    @Expose
    private String CPName;

    /**
     *
     * @return
     * The CPAddress
     */
    public String getCPAddress() {
        return CPAddress;
    }

    /**
     *
     * @param CPAddress
     * The CPAddress
     */
    public void setCPAddress(String CPAddress) {
        this.CPAddress = CPAddress;
    }

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
     * The CPLat
     */
    public Double getCPLat() {
        return CPLat;
    }

    /**
     *
     * @param CPLat
     * The CPLat
     */
    public void setCPLat(Double CPLat) {
        this.CPLat = CPLat;
    }

    /**
     *
     * @return
     * The CPLgt
     */
    public Double getCPLgt() {
        return CPLgt;
    }

    /**
     *
     * @param CPLgt
     * The CPLgt
     */
    public void setCPLgt(Double CPLgt) {
        this.CPLgt = CPLgt;
    }

    /**
     *
     * @return
     * The CPName
     */
    public String getCPName() {
        return CPName;
    }

    /**
     *
     * @param CPName
     * The CPName
     */
    public void setCPName(String CPName) {
        this.CPName = CPName;
    }
}

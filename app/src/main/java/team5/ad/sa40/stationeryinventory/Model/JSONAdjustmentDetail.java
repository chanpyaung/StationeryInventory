package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONAdjustmentDetail {

    @Expose
    private Integer adjSN;
    @Expose
    private String adjustmentID;
    @Expose
    private String itemID;
    @Expose
    private Integer quantity;
    @Expose
    private Double price;
    @Expose
    private String reason;
    @Expose
    private String remark;

    /**
     *
     * @return
     * The adjSN
     */
    public Integer getAdjSN() {
        return adjSN;
    }

    /**
     *
     * @param adjSN
     * The adjSN
     */
    public void setAdjSN(Integer adjSN) {
        this.adjSN = adjSN;
    }

    /**
     *
     * @return
     * The adjustmentID
     */
    public String getAdjustmentID() {
        return adjustmentID;
    }

    /**
     *
     * @param adjustmentID
     * The adjustmentID
     */
    public void setAdjustmentID(String adjustmentID) {
        this.adjustmentID = adjustmentID;
    }

    /**
     *
     * @return
     * The itemID
     */
    public String getItemID() {
        return itemID;
    }

    /**
     *
     * @param itemID
     * The itemID
     */
    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    /**
     *
     * @return
     * The quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     *
     * @param quantity
     * The quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     *
     * @return
     * The price
     */
    public Double getPrice() {
        return price;
    }

    /**
     *
     * @param price
     * The price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     *
     * @return
     * The reason
     */
    public String getReason() {
        return reason;
    }

    /**
     *
     * @param reason
     * The reason
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     *
     * @return
     * The remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     *
     * @param remark
     * The remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

}
package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONAdjustmentDetail {

    @Expose
    private Integer AdjSN;
    @Expose
    private String AdjID;
    @Expose
    private String ItemID;
    @Expose
    private Integer Qty;
    @Expose
    private Double Price;
    @Expose
    private String Reason;
    @Expose
    private String Remark;

    /**
     *
     * @return
     * The AdjSN
     */
    public Integer getAdjSN() {
        return AdjSN;
    }

    /**
     *
     * @param AdjSN
     * The AdjSN
     */
    public void setAdjSN(Integer AdjSN) {
        this.AdjSN = AdjSN;
    }

    /**
     *
     * @return
     * The AdjID
     */
    public String getAdjustmentID() {
        return AdjID;
    }

    /**
     *
     * @param AdjID
     * The AdjID
     */
    public void setAdjustmentID(String AdjID) {
        this.AdjID = AdjID;
    }

    /**
     *
     * @return
     * The ItemID
     */
    public String getItemID() {
        return ItemID;
    }

    /**
     *
     * @param ItemID
     * The ItemID
     */
    public void setItemID(String ItemID) {
        this.ItemID = ItemID;
    }

    /**
     *
     * @return
     * The Qty
     */
    public Integer getQuantity() {
        return Qty;
    }

    /**
     *
     * @param Qty
     * The Qty
     */
    public void setQuantity(Integer Qty) {
        this.Qty = Qty;
    }

    /**
     *
     * @return
     * The Price
     */
    public Double getPrice() {
        return Price;
    }

    /**
     *
     * @param Price
     * The Price
     */
    public void setPrice(Double Price) {
        this.Price = Price;
    }

    /**
     *
     * @return
     * The Reason
     */
    public String getReason() {
        return Reason;
    }

    /**
     *
     * @param Reason
     * The Reason
     */
    public void setReason(String Reason) {
        this.Reason = Reason;
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

}
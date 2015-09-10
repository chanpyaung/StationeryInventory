package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;
public class JSONDisbursementDetail {

    @Expose
    private Integer DisID;
    @Expose
    private Integer DisSN;
    @Expose
    private String ItemID;
    @Expose
    private Integer Qty;

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
     * The DisSN
     */
    public Integer getDisSN() {
        return DisSN;
    }

    /**
     *
     * @param DisSN
     * The DisSN
     */
    public void setDisSN(Integer DisSN) {
        this.DisSN = DisSN;
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
    public Integer getQty() {
        return Qty;
    }

    /**
     *
     * @param Qty
     * The Qty
     */
    public void setQty(Integer Qty) {
        this.Qty = Qty;
    }

}
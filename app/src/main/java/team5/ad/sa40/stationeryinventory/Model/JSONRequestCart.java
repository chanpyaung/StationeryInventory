package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/10/15.
 */
public class JSONRequestCart {

    @Expose
    private String ItemID;
    @Expose
    private String ItemName;
    @Expose
    private Integer Qty;
    @Expose
    private String UOM;

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
     * The ItemName
     */
    public String getItemName() {
        return ItemName;
    }

    /**
     *
     * @param ItemName
     * The ItemName
     */
    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
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

    /**
     *
     * @return
     * The UOM
     */
    public String getUOM() {
        return UOM;
    }

    /**
     *
     * @param UOM
     * The UOM
     */
    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

}



package team5.ad.sa40.stationeryinventory.Model;

/**
 * Created by johnmajor on 9/9/15.
 */

import com.google.gson.annotations.Expose;

public class JSONItem implements Comparable {
    @Expose
    private String Bin;
    @Expose
    private Integer ItemCatID;
    @Expose
    private String ItemID;
    @Expose
    private String ItemName;
    @Expose
    private Integer RoLvl;
    @Expose
    private Integer RoQty;
    @Expose
    private Integer Stock;
    @Expose
    private String UOM;



    /**
     *
     * @return
     * The Bin
     */
    public String getBin() {
        return Bin;
    }

    /**
     *
     * @param Bin
     * The Bin
     */
    public void setBin(String Bin) {
        this.Bin = Bin;
    }

    /**
     *
     * @return
     * The ItemCatID
     */
    public Integer getItemCatID() {
        return ItemCatID;
    }

    /**
     *
     * @param ItemCatID
     * The ItemCatID
     */
    public void setItemCatID(Integer ItemCatID) {
        this.ItemCatID = ItemCatID;
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
     * The RoLvl
     */
    public Integer getRoLvl() {
        return RoLvl;
    }

    /**
     *
     * @param RoLvl
     * The RoLvl
     */
    public void setRoLvl(Integer RoLvl) {
        this.RoLvl = RoLvl;
    }

    /**
     *
     * @return
     * The RoQty
     */
    public Integer getRoQty() {
        return RoQty;
    }

    /**
     *
     * @param RoQty
     * The RoQty
     */
    public void setRoQty(Integer RoQty) {
        this.RoQty = RoQty;
    }

    /**
     *
     * @return
     * The Stock
     */
    public Integer getStock() {
        return Stock;
    }

    /**
     *
     * @param Stock
     * The Stock
     */
    public void setStock(Integer Stock) {
        this.Stock = Stock;
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

    @Override
    public int compareTo(Object o) {

        Item f = (Item) o;

        if (getItemID().compareTo(f.getItemID()) > 0) {
            return 1;
        }
        else if (getItemID().compareTo(f.getItemID()) < 0) {
            return -1;
        }
        else {
            return 0;
        }

    }

}

package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONStockCard {

    @Expose
    private Integer Balance;
    @Expose
    private String Date;
    @Expose
    private String Description;
    @Expose
    private String ItemID;
    @Expose
    private Integer Qty;
    @Expose
    private Integer StockCardSN;

    /**
     * @return The Balance
     */
    public Integer getBalance() {
        return Balance;
    }

    /**
     * @param Balance The Balance
     */
    public void setBalance(Integer Balance) {
        this.Balance = Balance;
    }

    /**
     * @return The Date
     */
    public String getDate() {
        return Date;
    }

    /**
     * @param Date The Date
     */
    public void setDate(String Date) {
        this.Date = Date;
    }

    /**
     * @return The Description
     */
    public String getDescription() {
        return Description;
    }

    /**
     * @param Description The Description
     */
    public void setDescription(String Description) {
        this.Description = Description;
    }

    /**
     * @return The ItemID
     */
    public String getItemID() {
        return ItemID;
    }

    /**
     * @param ItemID The ItemID
     */
    public void setItemID(String ItemID) {
        this.ItemID = ItemID;
    }

    /**
     * @return The Qty
     */
    public Integer getQty() {
        return Qty;
    }

    /**
     * @param Qty The Qty
     */
    public void setQty(Integer Qty) {
        this.Qty = Qty;
    }

    /**
     * @return The StockCardSN
     */
    public Integer getStockCardSN() {
        return StockCardSN;
    }

    /**
     * @param StockCardSN The StockCardSN
     */
    public void setStockCardSN(Integer StockCardSN) {
        this.StockCardSN = StockCardSN;
    }
}
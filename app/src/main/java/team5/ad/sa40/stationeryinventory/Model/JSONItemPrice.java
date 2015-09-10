package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

public class JSONItemPrice {

    @Expose
    private Integer IPID;
    @Expose
    private String ItemID;
    @Expose
    private Double Price;
    @Expose
    private String SupplierID;

    /**
     * @return The IPID
     */
    public Integer getIPID() {
        return IPID;
    }

    /**
     * @param IPID The IPID
     */
    public void setIPID(Integer IPID) {
        this.IPID = IPID;
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
     * @return The Price
     */
    public Double getPrice() {
        return Price;
    }

    /**
     * @param Price The Price
     */
    public void setPrice(Double Price) {
        this.Price = Price;
    }

    /**
     * @return The SupplierID
     */
    public String getSupplierID() {
        return SupplierID;
    }

    /**
     * @param SupplierID The SupplierID
     */
    public void setSupplierID(String SupplierID) {
        this.SupplierID = SupplierID;
    }
}

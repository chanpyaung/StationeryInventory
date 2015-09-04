package team5.ad.sa40.stationeryinventory.Model;

/**
 * Created by johnmajor on 9/3/15.
 */
public class Item {

    private String ItemID;
    private String ItemName;
    private int ItemCatID;
    private int RoLvl;
    private int RoQty;
    private String UOM;
    private int Stock;
    private String Bin;

    public Item(String itemID, String itemName, int itemCatID, int roLvl, int roQty, String UOM, int stock, String bin) {
        ItemID = itemID;
        ItemName = itemName;
        ItemCatID = itemCatID;
        RoLvl = roLvl;
        RoQty = roQty;
        this.UOM = UOM;
        Stock = stock;
        Bin = bin;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public int getItemCatID() {
        return ItemCatID;
    }

    public void setItemCatID(int itemCatID) {
        ItemCatID = itemCatID;
    }

    public int getRoLvl() {
        return RoLvl;
    }

    public void setRoLvl(int roLvl) {
        RoLvl = roLvl;
    }

    public int getRoQty() {
        return RoQty;
    }

    public void setRoQty(int roQty) {
        RoQty = roQty;
    }

    public String getUOM() {
        return UOM;
    }

    public void setUOM(String UOM) {
        this.UOM = UOM;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public String getBin() {
        return Bin;
    }

    public void setBin(String bin) {
        Bin = bin;
    }
}

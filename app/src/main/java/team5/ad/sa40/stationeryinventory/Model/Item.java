package team5.ad.sa40.stationeryinventory.Model;

import android.media.Image;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by johnmajor on 9/3/15.
 */
public class Item implements Comparable {

    private String ItemID;
    private String ItemName;
    private int ItemCatID;
    private int RoLvl;
    private int RoQty;
    private String UOM;
    private int Stock;
    private String Bin;
    public List<ItemPrice> itemPriceList;
    private Image Image;

    public Item(String itemID, String itemName, int itemCatID, int roLvl, int roQty, String UOM, int stock, String bin) {
        ItemID = itemID;
        ItemName = itemName;
        ItemCatID = itemCatID;
        RoLvl = roLvl;
        RoQty = roQty;
        this.UOM = UOM;
        Stock = stock;
        Bin = bin;
        itemPriceList = new ArrayList<ItemPrice>();
        itemPriceList = ItemPrice.getAllItemPrices(itemID);
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

    public Image getImage() {
        return Image;
    }

    public List<ItemPrice> getItemPriceList() {
        return itemPriceList;
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


    public static List<Item> initializeData(){
        List<Item> itemList = new ArrayList<Item>();
        int i = 0;
        Item item = new Item("C001","Clip 11 inch",i+1,100, 50, "Dozen", 80, "A7");
        itemList.add(item);
        i++;
        do {
            Item item2 = new Item("E00"+i,"Envelope Brown A4",i,100, 50, "Each", 200, "B3");
            itemList.add(item2);
            i++;
        } while (i<18);
        i=0;
        do {
            Item item2 = new Item("E00"+i,"Pen",i+5,100, 50, "Box", 80, "E8");
            itemList.add(item2);
            i++;
        } while (i<5);

        return itemList;
    }
}

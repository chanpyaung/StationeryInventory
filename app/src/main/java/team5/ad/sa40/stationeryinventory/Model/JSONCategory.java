package team5.ad.sa40.stationeryinventory.Model;

import com.google.gson.annotations.Expose;

/**
 * Created by johnmajor on 9/12/15.
 */
public class JSONCategory {

    @Expose
    private Integer ItemCatID;
    @Expose
    private String ItemDescription;

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
     * The ItemDescription
     */
    public String getItemDescription() {
        return ItemDescription;
    }

    /**
     *
     * @param ItemDescription
     * The ItemDescription
     */
    public void setItemDescription(String ItemDescription) {
        this.ItemDescription = ItemDescription;
    }
}


package team5.ad.sa40.stationeryinventory.Fragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Utilities.JSONParser;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * Created by johnmajor on 9/1/15.
 */
public class CategoryItem {

    private String catName;
    private int catThumbnail;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public int getCatThumbnail() {
        return catThumbnail;
    }

    public void setCatThumbnail(int catThumbnail) {
        this.catThumbnail = catThumbnail;
    }

    public static String[] categories = {"All Categories","Clip","Envelope","Eraser","Exercise","File","Pen","Puncher",
            "Pad","Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Tparency","Tray"};

    public static List<Item> getAllCategoryItems(){
        List<Item> categoryItemList = new ArrayList<Item>();
        JSONArray result = JSONParser.getJSONArrayFromUrl(String.format("%s/CatalogAPI.svc/getitemcategory/pen",
                Setup.baseurl));
        try {
            for (int i = 0; i < result.length(); i++) {
                JSONObject cat = new JSONObject(result.getString(i));
                Item item = new Item();
                item.setBin(cat.getString("Bin"));
                item.setItemCatID(Integer.parseInt(cat.getString("ItemCatID")));
                item.setItemName(cat.getString("ItemName"));
                item.setRoLvl(cat.getInt("RoLvl"));
                item.setRoQty(cat.getInt("RoQty"));
                item.setStock(cat.getInt("Stock"));
                item.setUOM(cat.getString("UOM"));
//                try {
//                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                    Date d = format.parse(ret.getString("Date"));
//                    System.out.println(d);
//                    r.setDate(d);
//                } catch (ParseException exp) {
//                    exp.printStackTrace();
//                }
                categoryItemList.add(item);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return categoryItemList;
    }
}

package team5.ad.sa40.stationeryinventory.Model;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import team5.ad.sa40.stationeryinventory.JSONParser;
import team5.ad.sa40.stationeryinventory.Setup;

public class Retrieval {

    private int retID;
    private Date date;
    private String status;
    private List<RetrievalDetail> items;
    private List<String> reqForms;

    public Retrieval(){
        retID = 0;
        status = "";
        items = new ArrayList<RetrievalDetail>();
        reqForms = new ArrayList<String>();
    }

    public int getRetID(){
        return retID;
    }

    public Date getDate(){
        return date;
    }

    public List<RetrievalDetail> getItems(){
        return items;
    }

    public String getStatus(){
        return status;
    }

    public List<String> getReqForms() {
        return reqForms;
    }

    public void setRetID(int id){
        retID = id;
    }

    public void setDate(Date date){
        this.date = date;
    }

    public void setStatus(String s) {
        status = s;
    }

    public static List<Retrieval> initializeData(){
        List<Retrieval> retrievalList = new ArrayList<Retrieval>();
        int i = 0;
        Retrieval rp = new Retrieval();
        rp.setRetID(i+1);
        rp.setDate(new Date());
        rp.setStatus("Pending");
        retrievalList.add(rp);
        i++;
        do {
            Retrieval r = new Retrieval();
            r.setRetID(i+1);
            r.setDate(new Date());
            r.setStatus("Retrieved");
            retrievalList.add(r);
            i++;
        } while (i<10);

        return retrievalList;
    }

    public static List<Retrieval> getAllRetrievals(){
        List<Retrieval> retrievalList = new ArrayList<Retrieval>();
        Setup s = new Setup();
        JSONArray result = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalAPI.svc/getRetrieval/%s/null/null",
                Setup.baseurl, Setup.user.getEmpID()));
        try {
            Log.i("json retrievals: ", result.toString());
            for (int retrieval = 0; retrieval < result.length(); retrieval++) {
                JSONObject ret = result.getJSONObject(retrieval);
                Retrieval r = new Retrieval();
                r.setRetID(Integer.parseInt(ret.getString("RetID")));
                r.setStatus(ret.getString("Status"));
                if(ret.getString("Date") != null || ret.getString("Date")!="") {
                    Setup setup = new Setup();
                    Date d = Setup.parseJSONDateToJavaDate(ret.getString("Date").toString());
                    r.setDate(d);
                }
                retrievalList.add(r);
            }
        } catch (Exception ex) {
            Log.e("getAllCollectionPoints", "JSONError");
        }
        return retrievalList;
    }

    public static Retrieval getRetrieval(int id, String status) {
        Retrieval r = new Retrieval();

        JSONArray resultObj = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalAPI.svc/getRetrieval/%s/%s/%s",
                Setup.baseurl, Setup.user.getEmpID(), status, Integer.toString(id)));
        JSONArray resultItems = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalAPI.svc/getRetrievalDetail/%s",
                Setup.baseurl, Integer.toString(id)));
        JSONArray resultReqForms = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalAPI.svc/getReqAllocation/%s",
                Setup.baseurl,Integer.toString(id)));

        Log.i("resultObj:",resultObj.toString());
        Log.i("resultItems:",resultItems.toString());
        Log.i("resultReqForms:",resultReqForms.toString());

        try {
            JSONObject result = resultObj.getJSONObject(0);
            r.setRetID(Integer.parseInt(result.getString("RetID")));
            r.setStatus(result.getString("Status"));
            if(result.getString("Date") != null || result.getString("Date")!="") {
                Setup setup = new Setup();
                Date d = Setup.parseJSONDateToJavaDate(result.getString("Date").toString());
                r.setDate(d);
            }

            for (int retItem=0; retItem<resultItems.length(); retItem++) {
                JSONObject i = resultItems.getJSONObject(retItem);
                RetrievalDetail item = new RetrievalDetail(i.getString("ItemID"), i.getString("ItemName"),
                        i.getString("Bin"), Integer.parseInt(i.getString("TotalQty")),
                        Integer.parseInt(i.getString("ActualQty")));
                r.getItems().add(item);
                Log.i("r.getItems:", r.getItems().toString());
            }

            for (int reqForm = 0; reqForm < resultItems.length(); reqForm++) {
                JSONObject i = resultReqForms.getJSONObject(reqForm);
                if(r.getReqForms().size() == 0){
                    r.getReqForms().add(i.getString("ReqID").toString());
                    Log.i("r.getReqForms:", r.getReqForms().toString());
                }
                for(int form=0; form<r.getReqForms().size(); form++) {
                    if(i.getString("ReqID") != r.getReqForms().get(form)) {
                        r.getReqForms().add(i.getString("ReqID").toString());
                        Log.i("r.getReqForms:", r.getReqForms().toString());
                        break;
                    }
                }
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return r;
    }

    public static String saveRetrieval(Retrieval ret, List<RetrievalDetail> allItems) {
        String result = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < allItems.size(); i++) {

            try {
                JSONObject obj = new JSONObject();
                obj.put("RetID", Integer.toString(ret.getRetID()));
                obj.put("ItemID", allItems.get(i).get("itemID").toString());
                if(allItems.get(i).get("ActualQty") == null) {
                    allItems.get(i).put("ActualQty","0");
                }
                obj.put("ActualQty", allItems.get(i).get("ActualQty").toString());
                jsonArray.put(obj);
                Log.i("json post array:", jsonArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //update retrieval form in server
        Setup s = new Setup();
        result = JSONParser.postStream(String.format("%s/retrievalAPI.svc/save", Setup.baseurl),
                jsonArray.toString());
        Log.i("json post result:", result);

        return result;
    }

    public static String submitRetrieval(Retrieval ret, List<RetrievalDetail> allItems) {
        String result = null;
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < allItems.size(); i++) {

            try {
                JSONObject obj = new JSONObject();
                obj.put("RetID", Integer.toString(ret.getRetID()));
                obj.put("ItemID", allItems.get(i).get("itemID").toString());
                if(allItems.get(i).get("ActualQty") == null) {
                    allItems.get(i).put("ActualQty","0");
                }
                obj.put("ActualQty", allItems.get(i).get("ActualQty").toString());
                jsonArray.put(obj);
                Log.i("json post array:", jsonArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //update retrieval form in server
        Setup s = new Setup();
        result = JSONParser.postStream(String.format("%s/retrievalAPI.svc/submit", Setup.baseurl),
                jsonArray.toString());
        Log.i("json post result:", result);

        return result;
    }

}

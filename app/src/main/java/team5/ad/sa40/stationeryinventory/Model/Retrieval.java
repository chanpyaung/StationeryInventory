package team5.ad.sa40.stationeryinventory.Model;


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

    public static List<RetrievalDetail> initializeDataDetails(int RetID){
        List<RetrievalDetail> retrievalDetailList = new ArrayList<RetrievalDetail>();
        int i=0;
        int retID = RetID;
        RetrievalDetail rp = new RetrievalDetail(i+1, retID, "C001","Clip 11 inch","A7",12,0);
        retrievalDetailList.add(rp);
        i++;
        do {
            RetrievalDetail r = new RetrievalDetail(i+1, retID, "E00"+i,"Envelope Brown A4","D5",(i*10),0);
            retrievalDetailList.add(r);
            i++;
        } while (i<10);

        return retrievalDetailList;
    }

    public static List<Retrieval> getAllRetrievals(){
        List<Retrieval> retrievalList = new ArrayList<Retrieval>();
        Employee e = new Employee();
        JSONArray result = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalapi.svc/getRetrieval/%s/null/null",
                Setup.baseurl, Employee.EmpID));
        try {
            for (int retrieval = 0; retrieval < result.length(); retrieval++) {
                JSONObject ret = new JSONObject(result.getString(retrieval));
                Retrieval r = new Retrieval();
                r.setRetID(Integer.parseInt(ret.getString("RetID")));
                r.setStatus(ret.getString("Status"));
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    Date d = format.parse(ret.getString("Date"));
                    System.out.println(d);
                    r.setDate(d);
                } catch (ParseException exp) {
                    exp.printStackTrace();
                }
                retrievalList.add(r);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retrievalList;
    }

    public Retrieval getRetrieval(int id) {
        Retrieval r = new Retrieval();

        JSONObject result = JSONParser.getJSONFromUrl(String.format("%s/retrievalapi.svc/getRetrieval/null/null/%s",
                Setup.baseurl, Integer.toString(id)));
        JSONArray resultItems = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalapi.svc/getRetrievalDetail/%s",
                Setup.baseurl, Integer.toString(id)));
        JSONArray resultReqForms = JSONParser.getJSONArrayFromUrl(String.format("%s/retrievalapi.svc/getReqAllocation/%s",
                Setup.baseurl,Integer.toString(id)));

        if(result!=null) {
            try {
                r.setRetID(Integer.parseInt(result.getString("RetID")));
                r.setStatus(result.getString("Status"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                try {
                    Date d = format.parse(result.getString("Date"));
                    System.out.println(d);
                    r.setDate(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                for (int retItem=0; retItem<resultItems.length(); retItem++) {
                    JSONObject i = new JSONObject(resultItems.getString(retItem));
                    RetrievalDetail item = new RetrievalDetail(Integer.parseInt(i.getString("RetSN")),
                            Integer.parseInt(i.getString("RetID")), i.getString("itemID"), i.getString("itemName"),
                            i.getString("Bin"), Integer.parseInt(i.getString("RequestQty")),
                            Integer.parseInt(i.getString("ActualQty")));
                    r.getItems().add(item);
                }

            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        if(resultReqForms != null) {
            try {
                for (int reqForm = 0; reqForm < resultItems.length(); reqForm++) {
                    JSONObject i = new JSONObject(resultReqForms.getString(reqForm));
                    r.reqForms.add(i.get("ReqID").toString());
                }
            } catch(Exception e) {
            e.printStackTrace();
            }
        }

        return r;
    }

}

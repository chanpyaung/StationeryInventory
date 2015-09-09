package team5.ad.sa40.stationeryinventory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;

public class Setup {
    public static String baseurl = "http://192.168.31.202/api";
    public static String GTokenForNotification = "";
    public static JSONEmployee user;

    public static String parseDateToString(Date d) {
        String output = "";
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        output = out.format(d);
        return output;
    }

    public static Date parseStringToDate(String d){
        try{
            Date temp;
            SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
            temp = out.parse(d);
            return temp;
        }catch (ParseException e){

        }
        return null;
    }

    public static Date parseJSONDateToJavaDate(String JSONDate){
        //  "/Date(1321867151710+0100)/"
        int idx1 = JSONDate.indexOf("(");
        int idx2 = JSONDate.indexOf(")") - 5;
        String s = JSONDate.substring(idx1+1, idx2);
        long l = Long.valueOf(s);
        return new Date(l);
    }

    public static String parseJSONDateToString(String JSONDate){
        Date d = parseJSONDateToJavaDate(d);
        return parseDateToString(d);
    }
}

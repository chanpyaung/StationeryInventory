package team5.ad.sa40.stationeryinventory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Setup {
    public static String baseurl = "http://192.168.31.202/api";
    public static String GTokenForNotification = "";


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
}

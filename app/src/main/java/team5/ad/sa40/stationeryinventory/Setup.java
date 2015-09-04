package team5.ad.sa40.stationeryinventory;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Setup {
    public static String baseurl = "http://www.team5.com:8425";
    public static String GTokenForNotification = "";


    public static String parseDateToString(Date d) {
        String output = "";
        SimpleDateFormat out = new SimpleDateFormat("dd/MM/yyyy");
        output = out.format(d);
        return output;
    }
}

package team5.ad.sa40.stationeryinventory;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by student on 2/9/15.
 */
public class Disbursement {

    private int disbursementId;
    private Date disbursementDate;
    private String disbursementStatus;

    public int getDisbursementId(){

        return disbursementId;
    }
    public Date getDisbursementDate(){

        return disbursementDate;
    }
    public String getDisbursementStatus(){

        return disbursementStatus;
    }

    public void setDisbursementId(int id){

        disbursementId = id;
    }
    public void setDisbursementDate(Date date){

        disbursementDate = date;
    }
    public void setDisbursementStatus(String status){

        disbursementStatus = status;
    }

    public static ArrayList<Disbursement> getAllDisbursement(){
        ArrayList<Disbursement> temp = new ArrayList<>();

        for (int i = 0; i < 5; i++){

            Disbursement dis = new Disbursement();
            dis.setDisbursementId(i+1);
            dis.setDisbursementDate(new Date());
            dis.setDisbursementStatus("Pending");
            temp.add(dis);
        }
        return temp;
    }
}

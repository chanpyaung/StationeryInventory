package team5.ad.sa40.stationeryinventory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team5.ad.sa40.stationeryinventory.Model.JSONNotification;


public class NotificationFragment extends android.support.v4.app.ListFragment {

    private int empID;
    private int notifID;
    private List<JSONNotification> notificationList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        Setup s = new Setup();
        empID = Setup.user.getEmpID();


        new AsyncTask<Void, Void, List<JSONNotification>>() {
            @Override
            protected List<JSONNotification> doInBackground(Void... params) {
                return listNotification();
            }
            @Override
            protected void onPostExecute(List<JSONNotification> result) {
                List<Map<String,String>> notifList = new ArrayList<Map<String,String>>();
                for(JSONNotification n : result) {
                    Map<String,String> nmap = new HashMap<String,String>();
                    nmap.put("NotifID",Integer.toString(n.getNotifID()));
                    nmap.put("NotifName",n.getNotifName());
                    nmap.put("NotifDesc",n.getNotifDesc());
                    nmap.put("DateTime",n.getDateTime());
                    nmap.put("Status",n.getStatus());
                    notifList.add(nmap);
                }
                SimpleAdapter adapter = new SimpleAdapter(getActivity(),notifList,
                        android.R.layout.simple_list_item_2,
                        new String[]{"NotifName","NotifDesc"},
                        new int[]{android.R.id.text1, android.R.id.text2});
                setListAdapter(adapter);
            }
        }.execute();

        return view;
    }

    List<JSONNotification> listNotification() {
        notificationList = new ArrayList<JSONNotification>();
        try {

            JSONArray a = JSONParser.getJSONArrayFromUrl
                    (String.format("%s/notificationapi.svc/%s",Setup.baseurl,empID));
            for (int i=0; i<a.length(); i++) {
                JSONObject n = a.getJSONObject(i);
                Map<String,String> nmap = new HashMap<String,String>();
                nmap.put("NotifID",n.getString("NotifID"));
                nmap.put("NotifName",n.getString("NotifName"));
                nmap.put("NotifDesc",n.getString("NotifDesc"));
                nmap.put("DateTime",n.getString("DateTime"));
                nmap.put("Status",n.getString("Status"));

                notificationList.add(new JSONNotification());
            }
        } catch (Exception e) {
        }
        return notificationList;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(),notificationList.get(position).getNotifName().toString(),
                Toast.LENGTH_SHORT).show();
        Intent i;
        notifID = notificationList.get(position).getNotifID();
        Log.i("Notifid selected:",Integer.toString(notifID));
        /*
        switch(notificationList.get(position).get("NotifName")) {
            case "Low stock inventory":

                //update status of notification
                new AsyncTask<Void, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Void... params) {
                        return updateNotificationStatus(notifID);
                    }
                    @Override
                    protected void onPostExecute(Boolean result) {
                        if(result == true)
                            Log.i("Done updating status of:",notifID);
                        else
                            Log.i("Failed updating status of:",notifID);
                    }
                }.execute();

                i = new Intent(this, InventoryActivity.class);
                i.putExtra("NotifID",notificationList.get(position).get("NotifID"));
                i.putExtra("EmpID",empID);
                startActivityForResult(i);
                break;
        }
        */
    }

    Boolean updateNotificationStatus() {
        Boolean resultSuccess = false;

        try {
            JSONObject notif = new JSONObject();
            notif.put("NotifID", notifID);
            String json = notif.toString();
            Toast.makeText(getActivity(), json, Toast.LENGTH_SHORT);

            String result = JSONParser.postStream(
                    String.format("%s/notificationapi.svc/updateStatus",Setup.baseurl),json);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("json error",e.toString());
        }

        return resultSuccess;
    }

}

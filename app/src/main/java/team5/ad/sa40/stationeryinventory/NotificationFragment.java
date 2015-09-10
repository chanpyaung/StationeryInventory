package team5.ad.sa40.stationeryinventory;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.NotificationAPI;
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

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        NotificationAPI notifAPI = restAdapter.create(NotificationAPI.class);

        notifAPI.getList(Integer.toString(empID),new Callback<List<JSONNotification>>(){
            @Override
            public void success(List<JSONNotification> jsonItems, Response response) {
                Log.i("Result :", jsonItems.toString());
                Log.i("First item: ", jsonItems.get(0).getNotifName());
                Log.i("Response: ", response.getBody().toString());
                System.out.println("Response Status " + response.getStatus());
                notificationList = jsonItems;
                List<Map<String,String>> notifList = new ArrayList<Map<String,String>>();
                for(JSONNotification n : jsonItems) {
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

            @Override
            public void failure(RetrofitError error) {
                Log.i("Error: ", error.toString());
            }
        });

        return view;
    }
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(getActivity(),notificationList.get(position).getNotifName().toString(),
                Toast.LENGTH_SHORT).show();
        Intent i;
        notifID = notificationList.get(position).getNotifID();
        Log.i("Notifid selected:",Integer.toString(notifID));

        switch(notificationList.get(position).getNotifName()) {
            case "Low stock inventory": {
                //update status of notification
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                NotificationAPI notifAPI = restAdapter.create(NotificationAPI.class);

                notifAPI.changeStatus(Integer.toString(notifID), new Callback<String>() {
                    @Override
                    public void success(String result, Response response) {
                        Log.i("Result :", result);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error: ", error.toString());
                    }
                });
                InventoryList iLFrag = new InventoryList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                iLFrag.setArguments(args);
                fragTran.replace(R.id.frame, iLFrag).addToBackStack("TAG").commit();
                break;
            }
        }
    }
}

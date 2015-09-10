package team5.ad.sa40.stationeryinventory;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.NotificationAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;


public class NotificationFragment extends android.support.v4.app.Fragment{

    private int empID;
    private int notifID;
    private List<JSONNotification> notificationList;
    private RecyclerView mRecyclerView;
    private NotifListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public NotificationFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        Setup s = new Setup();
        empID = Setup.user.getEmpID();
        Log.i("empID: ",Integer.toString(empID));

        mRecyclerView = (RecyclerView) view.findViewById(R.id.notif_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        NotificationAPI notifAPI = restAdapter.create(NotificationAPI.class);

        notifAPI.getList(Integer.toString(empID), new Callback<List<JSONNotification>>() {
            @Override
            public void success(List<JSONNotification> jsonItems, Response response) {
                if(jsonItems.size() > 0) {
                    Log.i("Result :", jsonItems.toString());
                    Log.i("First item: ", jsonItems.get(0).getNotifName());
                }
                Log.i("Response: ", response.getBody().toString());
                System.out.println("Response Status " + response.getStatus());

                adapter = new NotifListAdapter(jsonItems);
                notificationList = NotifListAdapter.mJSONNotifications;
                mRecyclerView.setAdapter(adapter);
                adapter.SetOnItemClickListener(new NotifListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        onClickGoTo(notificationList.get(position));
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Error: ", error.toString());
            }
        });

        return view;
    }

    public void onClickGoTo(JSONNotification n) {
        notifID = n.getNotifID();
        Log.i("Notifid selected:",Integer.toString(notifID));

        switch(n.getNotifName()) {
            case "Low Stock Inventory": {
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

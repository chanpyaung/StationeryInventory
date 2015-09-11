package team5.ad.sa40.stationeryinventory;

import android.app.AlertDialog;
import android.content.DialogInterface;
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

        switch(n.getNotifName()) {
            case "New Pending Requisition":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Requisition Approved":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Requisition Rejected":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Processing Requisition":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Requisition Processed":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Requisition Disbursed":{
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "New Pending Adjustment Voucher":{
                AdjVouList fragment = new AdjVouList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Adjustment Voucher Approved":{
                AdjVouList fragment = new AdjVouList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Adjustment Voucher Rejected":{
                AdjVouList fragment = new AdjVouList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Requisition Items Not Fulfilled": {
                RequisitionListFragment fragment = new RequisitionListFragment();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "New Collection Schedule": {
                DisbursementList fragment = new DisbursementList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;
            }
            case "Change in Department's Representative": {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("To view department info, please log into LUStationery website.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                builder.show();
                break;
            }
            case "Change in Department's Collection Point": {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("To view department info, please log into LUStationery website.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        })
                        .create();
                builder.show();
                break;
            }
            case "Low Stock Inventory": {
                InventoryList iLFrag = new InventoryList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                iLFrag.setArguments(args);
                fragTran.replace(R.id.frame, iLFrag).addToBackStack("TAG").commit();
                break;
            }
            case "New Report Generated": {
                /*
                AnalyticsList fragment = new AnalyticsList();
                Bundle args = new Bundle();
                args.putString("NotifID", Integer.toString(notifID));
                Log.i("NotifID", Integer.toString(notifID));
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragment.setArguments(args);
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
                break;*/
            }

        }
    }
}

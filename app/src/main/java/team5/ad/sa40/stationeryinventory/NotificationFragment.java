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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.NotificationAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;


public class NotificationFragment extends android.support.v4.app.Fragment{

    private int empID;
    private int notifID;
    private List<JSONNotification> notificationList;
    private RecyclerView mRecyclerView;
    private NotifListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    final RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);

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
        Log.i("Notifid selected:", Integer.toString(notifID));

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
                goToReqList();
                break;
            }
            case "Requisition Approved":{
                goToReqList();
                break;
            }
            case "Requisition Rejected":{
                goToReqList();
                break;
            }
            case "Processing Requisition":{
                goToReqList();
                break;
            }
            case "Requisition Processed":{
                goToReqList();
                break;
            }
            case "Requisition Disbursed":{
                goToReqList();
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
                goToReqList();
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
                fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();*/
                break;
            }

        }
    }

    public void goToReqList(){
        reqAPI.getStatus(new Callback<List<JSONStatus>>() {
            @Override
            public void success(List<JSONStatus> jsonStatuses, Response response) {
                Log.i("Status Size", String.valueOf(jsonStatuses.size()));
                RequisitionListAdapter.mStatus = jsonStatuses;
                //if user is StoreClerk; load all requisition
                if(Setup.user.getRoleID().equals("SC")){
                    reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                        @Override
                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                            if (jsonRequisitions.size() > 0) {
                                List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                                for (JSONRequisition jsonReq : jsonRequisitions) {
                                    if (jsonReq.getStatusID().equals(2)) {
                                        reqList.add(jsonReq);
                                    }
                                }
                                Log.i("URL", response.getUrl());
                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                Log.i("REASON", response.getReason());
                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                if (jsonRequisitions.size() > 0) {
                                    System.out.println("Sorting here");
                                    Collections.sort(jsonRequisitions);
                                    Setup.allRequisition = reqList;
                                    RequisitionListAdapter.mRequisitions = reqList;
                                    for (JSONRequisition jr : jsonRequisitions) {
                                        System.out.println("ordered by Date" + jr.getDate() + " " + jr.getReqID());
                                    }
                                }
                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, reqListFrag).addToBackStack("NOTI_TAG").commit();

                            } else {

                                Toast.makeText(getActivity(), "We acknowledge you that you haven't made any requisition yet.Please made some requisition before you proceed.", Toast.LENGTH_SHORT).show();

                            }
                        }
                        @Override
                        public void failure (RetrofitError error){
                            Log.i("GetRequisitionFail", error.toString() + " " + error.getUrl());
                        }

                    });
                }
                //load requisitionlist by EmpID
                else{
                    reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                        @Override
                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                            if (jsonRequisitions.size() > 0) {
                                Log.i("URL", response.getUrl());
                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                Log.i("REASON", response.getReason());
                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                if (jsonRequisitions.size() > 0) {
                                    System.out.println("Sorting here");
                                    Collections.sort(jsonRequisitions);
                                    Setup.allRequisition = jsonRequisitions;
                                    RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                    for (JSONRequisition jr : jsonRequisitions) {
                                        System.out.println("ordered by Date" + jr.getDate() + " " + jr.getReqID());
                                    }
                                }
                                RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, reqListFrag).commit();
                            } else {
                                Toast.makeText(getActivity(), "We acknowledge you that you haven't made any requisition yet.Please made some requisition before you proceed.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("GetRequisitionFail", error.toString() + " " + error.getUrl());
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Status Fail", error.toString());
                Log.i("URL", error.getUrl());
            }
        });
    }
}

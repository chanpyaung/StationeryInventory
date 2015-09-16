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

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.API.NotificationAPI;
import team5.ad.sa40.stationeryinventory.API.ReportAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;


public class NotificationFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    private int empID;
    private int notifID;
    private String adjustID = "";
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

        getActivity().setTitle("Notifications");

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
                if (jsonItems.size() > 0) {
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
                ReportAPI rpAPI = restAdapter.create(ReportAPI.class);
                rpAPI.getReports(new Callback<List<JSONReport>>() {
                    @Override
                    public void success(List<JSONReport> jsonReports, Response response) {
                        AnalyticsAdapter.mReports = jsonReports;
                        AnalyticsListFragment afrag = new AnalyticsListFragment();
                        getFragmentManager().beginTransaction().replace(R.id.frame, afrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Fail getReports", error.toString());
                    }
                });
                break;
            }

        }
    }

    public void goToReqList(){
        if(Setup.MODE == 1){
            //user requisition page
            if (Setup.user.getRoleID().equals("SC")) {
                reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                    @Override
                    public void success(List<JSONRequisition> jsonRequisitions, Response response) {
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
                        RequisitionListAdapter.mRequisitions = reqList;
                        Setup.allRequisition = reqList;
                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                        FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, reqListFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("GetRequisitionFail", error.toString());
                    }
                });
            }
            //load requisitionlist by EmpID
            else {
                reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                    @Override
                    public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                        Log.i("URL", response.getUrl());
                        Log.i("STATUS", String.valueOf(response.getStatus()));
                        Log.i("REASON", response.getReason());
                        Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                        for(JSONRequisition jr : jsonRequisitions){
                            if(jr.getStatusID().equals(6)){
                                jsonRequisitions.remove(jr);
                            }
                        }
                        RequisitionListAdapter.mRequisitions = jsonRequisitions;
                        Setup.allRequisition = jsonRequisitions;
                        RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                        FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, reqListFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("GetRequisitionFail", error.toString());
                    }
                });
            }
        }
        else if (Setup.MODE == 2){
            //approval
            reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                @Override
                public void success(List<JSONRequisition> jsonRequisitions, Response response) {

                    Log.i("URL", response.getUrl());
                    Log.i("STATUS", String.valueOf(response.getStatus()));
                    Log.i("REASON", response.getReason());
                    Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                    List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                    //inside all requisition filter requisition by Department & PENDING Status
                    for(JSONRequisition jsonReq : jsonRequisitions){
                        if(jsonReq.getDeptID()!=null) {
                            if (jsonReq.getDeptID().toString().equals(Setup.user.getDeptID().toString())) {
                                Log.i("Here what", String.valueOf(jsonReq.getReqID()));
                                if (jsonReq.getStatusID().equals(1) || jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(5)) {
                                    reqList.add(jsonReq);
                                    Log.i("what here", String.valueOf(jsonReq.getReqID()));
                                }

                            }
                        }
                    }
                    Log.i("SIZE OFFFFF reqList", String.valueOf(reqList.size()));
                    RequisitionListAdapter.mRequisitions = reqList;
                    Setup.allRequisition = reqList;
                    //RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                    RequisitionListFragment reqListFrag = new RequisitionListFragment();
                    FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                    fragmentTran.replace(R.id.frame, reqListFrag).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("GetRequisitionFail", error.toString()+ " " + error.getUrl());
                }
            });
        }
    }

    @Override
    public void doBack() {
        NotificationFragment frag = new NotificationFragment();
        android.support.v4.app.FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.frame, frag).commit();
    }
}

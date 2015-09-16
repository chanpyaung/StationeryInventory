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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.API.NotificationAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONNotification;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;


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
                goToReqList(n);
                break;
            }
            case "Requisition Approved":{
                goToReqList(n);
                break;
            }
            case "Requisition Rejected":{
                goToReqList(n);
                break;
            }
            case "Processing Requisition":{
                goToReqList(n);
                break;
            }
            case "Requisition Processed":{
                goToReqList(n);
                break;
            }
            case "Requisition Disbursed":{
                goToReqList(n);
                break;
            }
            case "New Pending Adjustment Voucher":{
                goToAdjVoucher(n);
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
                goToReqList(n);
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

    public void goToReqList(JSONNotification n){
        final int reqID = Integer.parseInt(n.getNotifDesc().substring(7, 4));
        Log.i("reqID from notification: ", String.valueOf(reqID));

        // get object from server
        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);

        reqAPI.getRequisitionByReqID(reqID, new Callback<List<JSONRequisition>>() {
            @Override
            public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                JSONRequisition obj = new JSONRequisition();
                for (JSONRequisition jr : jsonRequisitions) {
                    if (jr.getReqID() == reqID) {
                        obj = jr;
                    }
                }
                final JSONRequisition selected = obj;

                // retrieve requisition details from server
                final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
                reqAPI.getReqDetail(selected.getReqID(), new Callback<List<JSONReqDetail>>() {
                    @Override
                    public void success(List<JSONReqDetail> jsonReqDetails, Response response) {
                        RequisitionFormAdapter.mRequisitionDetails = jsonReqDetails;
                        final Bundle args = new Bundle();
                        args.putString("Date", selected.getDate());
                        args.putInt("ReqID", selected.getReqID());
                        args.putInt("StatusID", selected.getStatusID());
                        args.putInt("EmpID", selected.getEmpID());
                        if (Setup.user.getRoleID().equals("DD") || Setup.user.getRoleID().equals("DH")) {
                            args.putString("APPROVAL", "ENABLED");
                        }
                        RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                        reqDetailFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, reqDetailFrag).addToBackStack("REQUESITION_LIST").commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void goToAdjVoucher(JSONNotification n){
        adjustID = n.getNotifDesc().substring(21,31);
        Log.i("AdjID notif selected: ", adjustID);

        final JsonObject json = new JsonObject();
        json.addProperty("adjId", adjustID);
        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        final AdjustmentAPI adjustmentAPI = restAdapter.create(AdjustmentAPI.class);
        adjustmentAPI.getAdjVoucher(json, new Callback<List<JSONAdjustment>>() {
            @Override
            public void success(List<JSONAdjustment> adjustments, Response response) {
                JSONAdjustment selected = new JSONAdjustment();
                for(JSONAdjustment j : adjustments){
                    if(j.getAdjID().equals(json.get("adjId"))) {
                        selected = j;
                    }
                }

                JsonObject object = new JsonObject();
                object.addProperty("adjId", selected.getAdjID());
                final JSONAdjustment temp = selected;
                adjustmentAPI.getAdjVoucherDetail(object, new Callback<List<JSONAdjustmentDetail>>() {
                    @Override
                    public void success(List<JSONAdjustmentDetail> jsonAdjustmentDetails, Response response) {
                        android.support.v4.app.Fragment frag;
                        if (temp.getStatus().equals("PENDING")) {
                            frag = new AdjListDetail2();
                        } else {
                            frag = new AdjListDetail();
                        }
                        ArrayList<JSONAdjustmentDetail> tempList = new ArrayList<JSONAdjustmentDetail>(jsonAdjustmentDetails);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("adjustment", temp);
                        bundle.putSerializable("adjustmentDetail", tempList);
                        for (JSONAdjustmentDetail detail : tempList) {
                            Log.e("detail id", String.valueOf(detail.getAdjustmentID()));
                        }
                        frag.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getAdjVoucher", error.toString());
            }
        });
    }

    @Override
    public void doBack() {
        NotificationFragment frag = new NotificationFragment();
        android.support.v4.app.FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.frame, frag).commit();
    }
}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;
import team5.ad.sa40.stationeryinventory.Model.Requisition;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequisitionDetailFragment extends android.support.v4.app.Fragment implements AdapterView.OnClickListener{

    private Requisition req;
    private List<JSONReqDetail> allItems;
    private RecyclerView mRecyclerView;
    private RequisitionFormAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    android.support.v4.app.FragmentTransaction fragmentTran;

    private final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    RequisitionAPI reqAPI = restadapter.create(RequisitionAPI.class);
    int reqID;
    String strRemark = "NA";

    @Bind(R.id.inv_detail_itemName) TextView reqFormID;
    @Bind(R.id.priority_text) TextView priority;
    @Bind(R.id.status_text) TextView status;
    @Bind(R.id.requisition_cancel) Button cancel;
    @Bind(R.id.requi_approve) Button approve;
    @Bind(R.id.rmkText) EditText remark;
    @Bind(R.id.createdBy) TextView createdBy;

    public RequisitionDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        req = new Requisition();

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_requisition_detail, container, false);
        ButterKnife.bind(this, view);
        getActivity().setTitle("Requisition Detail");
        Setup s = new Setup();

        if (getArguments() != null) {
            String idDisplay = "";
            reqID = getArguments().getInt("ReqID");
            if(reqID<10) {
                idDisplay = "000" + String.valueOf(reqID);
            }
            else if(reqID<100) {
                idDisplay = "00" + String.valueOf(reqID);
            }
            else if(reqID<1000) {
                idDisplay = "0" + String.valueOf(reqID);
            }
            else if(reqID<10000) {
                idDisplay = String.valueOf(reqID);
            }
            reqFormID.setText(idDisplay);

            if(getArguments().getString("EmpName") != ""){
                createdBy.setText(getArguments().getString("EmpName"));
            }


            if (getArguments().getInt("StatusID")==1){
                status.setText("Pending");
            }
            else if (getArguments().getInt("StatusID")==2){
                status.setText("Approved");
                status.setTextColor(Color.GREEN);
            }
            else if (getArguments().getInt("StatusID")==3){
                status.setText("Processed");
                status.setTextColor(Color.MAGENTA);
            }
            else if (getArguments().getInt("StatusID")==4){
                status.setText("Collected");
                status.setTextColor(Color.BLUE);
            }
            else if(getArguments().getInt("StatusID")==5){
                status.setText("Rejected");
                status.setTextColor(Color.RED);
            }
            else if(getArguments().getInt("StatusID")==6) {
                status.setText("Cancelled");
            }


                if(Setup.user.getRoleID().equals("DH") || Setup.user.getRoleID().equals("DD")){
                    if(getArguments().getString("APPROVAL").equals("ENABLED") && status.getText().equals("Pending")){
                    cancel.setText("REJECT");
                    approve.setVisibility(View.VISIBLE);
                        remark.setVisibility(View.VISIBLE);

                        remark.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                if (s.length()>0){
                                    strRemark = remark.getText().toString();
                                }
                            }

                            @Override
                            public void afterTextChanged(Editable s) {

                            }
                        });
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Reach", "Here reject click");
                            reqAPI.rejectRequisition(reqID, Setup.user.getEmpID(), strRemark, new Callback<Boolean>() {
                                @Override
                                public void success(Boolean aBoolean, Response response) {
                                    Log.i("Success Reject", aBoolean.toString());
                                    Log.i("Response", response.getUrl() + " " + String.valueOf(response.getStatus()) + " " + response.getReason());
                                    reqAPI.getStatus(new Callback<List<JSONStatus>>() {
                                        @Override
                                        public void success(List<JSONStatus> jsonStatuses, Response response) {
                                            Log.i("Status Size", String.valueOf(jsonStatuses.size()));
                                            RequisitionListAdapter.mStatus = jsonStatuses;
                                            //if user is StoreClerk; load all requisition
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
                                                        fragmentTran = getFragmentManager().beginTransaction();
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
                                                        RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                                        Setup.allRequisition = jsonRequisitions;
                                                        RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                                        fragmentTran = getFragmentManager().beginTransaction();
                                                        fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        Log.i("GetRequisitionFail", error.toString());
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

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.i("Reject Failed", error.toString() + " " + error.getUrl());
                                }
                            });

                        }
                    });
                    approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Reach", "Here approve click");
                                                reqAPI.approveRequisition(reqID, Setup.user.getEmpID(), strRemark, new Callback<Boolean>() {
                                                    @Override
                                                    public void success(Boolean aBoolean, Response response) {
                                                        Log.i("Success Approve", aBoolean.toString());
                                                        Log.i("Response", response.getUrl() + " " + String.valueOf(response.getStatus()) + " " + response.getReason());
                                                        reqAPI.getStatus(new Callback<List<JSONStatus>>() {
                                                            @Override
                                                            public void success(List<JSONStatus> jsonStatuses, Response response) {
                                                                Log.i("Status Size", String.valueOf(jsonStatuses.size()));
                                                                RequisitionListAdapter.mStatus = jsonStatuses;
                                                                //if user is StoreClerk; load all requisition
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
                                                                            fragmentTran = getFragmentManager().beginTransaction();
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
                                                                            RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                                                            Setup.allRequisition = jsonRequisitions;
                                                                            RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                                                            RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                                                            fragmentTran = getFragmentManager().beginTransaction();
                                                                            fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                                                        }

                                                                        @Override
                                                                        public void failure(RetrofitError error) {
                                                                            Log.i("GetRequisitionFail", error.toString());
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

                                                    @Override
                                                    public void failure(RetrofitError error) {

                                                        Log.i("Approved Failed", error.toString() +" "+ error.getUrl());
                                                    }
                                                });

                        }
                    });
                }
                    else {
                        cancel.setVisibility(View.GONE);
                    }
            }
            else {
                if(status.getText().equals("Pending")) {
                    cancel.setOnClickListener(this);
                }
                else if (status.getText().equals("Pending") && Setup.user.getRoleID().equals("SC")) {
                    cancel.setVisibility(View.GONE);
                }
                else {
                    cancel.setVisibility(View.GONE);
                }
            }
        }
        else {

        }
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_detail_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RequisitionFormAdapter(req.getRetID(),status.getText().toString());
        allItems = new ArrayList<JSONReqDetail>();
        allItems = adapter.mRequisitionDetails;
        mRecyclerView.setAdapter(adapter);
        System.out.println("USERROLE:::::"+Setup.user.getRoleID());


        return view;
    }

    @Override
    public void onClick(View v) {
        reqAPI.cancelRequisition(reqID, new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean, Response response) {
                Log.i("Success Cancel", aBoolean.toString());
                reqAPI.getStatus(new Callback<List<JSONStatus>>() {
                    @Override
                    public void success(List<JSONStatus> jsonStatuses, Response response) {
                        Log.i("Status Size", String.valueOf(jsonStatuses.size()));
                        RequisitionListAdapter.mStatus = jsonStatuses;
                        //if user is StoreClerk; load all requisition
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
                                    fragmentTran = getFragmentManager().beginTransaction();
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
                                    fragmentTran = getFragmentManager().beginTransaction();
                                    fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.i("GetRequisitionFail", error.toString());
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

            @Override
            public void failure(RetrofitError error) {
                Log.i("Cancel Fail", error.toString());
            }
        });
    }



}

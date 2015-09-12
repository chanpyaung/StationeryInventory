package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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

    private final RestAdapter restadapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    RequisitionAPI reqAPI = restadapter.create(RequisitionAPI.class);
    int reqID;

    @Bind(R.id.inv_detail_itemName) TextView reqFormID;
    @Bind(R.id.priority_text) TextView priority;
    @Bind(R.id.status_text) TextView status;
    @Bind(R.id.requisition_cancel) Button cancel;
    @Bind(R.id.requi_approve) Button approve;

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


            if (getArguments().getInt("StatusID")==1){
                status.setText("Pending Approval");
            }
            else if (getArguments().getInt("StatusID")==2){
                status.setText("Approved");
            }
            else if (getArguments().getInt("StatusID")==3){
                status.setText("Processed");
            }
            else if (getArguments().getInt("StatusID")==4){
                status.setText("Collected");
            }
            else if(getArguments().getInt("StatusID")==5){
                status.setText("Rejected");
            }
            else if(getArguments().getInt("StatusID")==6) {
                status.setText("Cancelled");
            }


                if(Setup.user.getRoleID().equals("DH") || Setup.user.getRoleID().equals("DD")){
                    if(getArguments().getString("APPROVAL").equals("ENABLED")){
                    cancel.setText("REJECT");
                    approve.setVisibility(View.VISIBLE);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Reach", "Here reject click");
                                                reqAPI.rejectRequisition(reqID, Setup.user.getEmpID(), "NA", new Callback<Boolean>() {
                                                    @Override
                                                    public void success(Boolean aBoolean, Response response) {
                                                        Log.i("Success Reject", aBoolean.toString());
                                                        Log.i("Response", response.getUrl() + " " + String.valueOf(response.getStatus()) + " " + response.getReason());
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {
                                                        Log.i("Reject Failed", error.toString()+" "+ error.getUrl());
                                                    }});
                        }
                    });
                    approve.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("Reach", "Here approve click");
                                                reqAPI.approveRequisition(reqID, Setup.user.getEmpID(), "NA", new Callback<Boolean>() {
                                                    @Override
                                                    public void success(Boolean aBoolean, Response response) {
                                                        Log.i("Success Approve", aBoolean.toString());
                                                        Log.i("Response", response.getUrl() + " " + String.valueOf(response.getStatus()) + " " + response.getReason());
                                                    }

                                                    @Override
                                                    public void failure(RetrofitError error) {

                                                        Log.i("Approved Failed", error.toString() +" "+ error.getUrl());
                                                    }
                                                });

                        }
                    });
                }
            }
            else {
                if(status.getText().equals("Pending Approval")) {
                    cancel.setOnClickListener(this);
                }
                else if (status.getText().equals("Pending Approval") && Setup.user.getRoleID().equals("SC")) {
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
//        adapter.SetOnItemClickListener(new RequisitionFormAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                JSONReqDetail selected = allItems.get(position);
//                Toast.makeText(RequisitionDetailFragment.this.getActivity(), "Click " + position, Toast.LENGTH_SHORT).show();
//            }
//        });
        System.out.println("USERROLE:::::"+Setup.user.getRoleID());


        return view;
    }

    @Override
    public void onClick(View v) {
        reqAPI.cancelRequisition(reqID, new Callback<Boolean>() {
            @Override
            public void success(Boolean aBoolean, Response response) {
                Log.i("Success Cancel", aBoolean.toString());
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Cancel Fail", error.toString());
            }
        });
    }



}

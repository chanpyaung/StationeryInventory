package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Requisition;
import team5.ad.sa40.stationeryinventory.Model.RequisitionDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class RequisitionDetailFragment extends android.support.v4.app.Fragment implements AdapterView.OnClickListener{

    private Requisition req;
    private List<RequisitionDetail> allItems;
    private RecyclerView mRecyclerView;
    private RequisitionFormAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.id_text) TextView reqFormID;
//    @Bind(R.id.approvedPerson) TextView approvedPerson;
//    @Bind(R.id.processedPerson) TextView processedPerson;
    @Bind(R.id.priority_text) TextView priority;
    @Bind(R.id.remark_text) TextView remark;
    @Bind(R.id.status_text) TextView status;
    @Bind(R.id.reason_text) TextView reason;
    @Bind(R.id.requisition_cancel) Button cancel;


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
            Log.i("arguments: ", getArguments().toString());
            req.setReqID(getArguments().getInt("ReqID"));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Log.i("date passed:",getArguments().getString("ReqDate"));
            try {
                Date d = format.parse(getArguments().getString("ReqDate"));
                System.out.println(d);
                req.setDate(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            req.setStatusID(getArguments().getInt("StatusID"));
        }
        else {
            req.setRetID(123);
            req.setDate(new Date());
        }

        String idDisplay = "";
        int id = req.getRetID();
        if(id<10) {
            idDisplay = "000" + String.valueOf(id);
        }
        else if(id<100) {
            idDisplay = "00" + String.valueOf(id);
        }
        else if(id<1000) {
            idDisplay = "0" + String.valueOf(id);
        }
        else if(id<10000) {
            idDisplay = String.valueOf(id);
        }
        reqFormID.setText(idDisplay);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_detail_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String status = "";
        if (req.getStatusID()==1){
            status = "Approved";
        }
        else if (req.getStatusID()==2){
            status= "Rejected";
        }
        else if (req.getStatusID()==3){
            status= "Processed";
        }
        else if (req.getStatusID()==4){
            status= "Collected";
        }
        adapter = new RequisitionFormAdapter(req.getRetID(),status);
        allItems = new ArrayList<RequisitionDetail>();
        allItems = adapter.mRequisitionDetails;
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RequisitionFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RequisitionDetail selected = allItems.get(position);
            }
        });
        //reqFormID.setText(adapter.mReqForms);

        if(status == "Pending") {
            cancel.setOnClickListener(this);
        }
        else {
            //cancel.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        Boolean checkIfNoNull = true;
        // Check for a valid password, if the user entered one.
        for (int i=0; i<allItems.size(); i++) {
            if(allItems.get(i).get("ActualQty") == "0") {
                checkIfNoNull = false;
            }
        }
        /*

        if(checkIfNoNull == true) {

            JSONArray jsonArray = new JSONArray();

            for (int i = 0; i < allItems.size(); i++) {

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("RetID", Integer.toString(ret.getRetID()));
                    obj.put("ItemID", allItems.get(i).get("itemID"));
                    obj.put("ActualQty", allItems.get(i).get("ActualQty"));
                    jsonArray.put(obj);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            //update retrieval form in server
            Setup s = new Setup();
            String result = JSONParser.postStream(String.format("%s/retrieval.svc/save", Setup.baseurl),
                    jsonArray.toString());
            Log.i("json post result:", result);
            */

        String result = "HttpResponse_OK";
        //process retrieval update status
        if (result != "HttpResponse_OK") {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Submission of Retrieval Form #" + req.getRetID() + " failed. Please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            builder.show();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Retrieval Form #" + req.getRetID() + " has been successfully submitted! Please login to the web for allocation of items retrieved.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RetrievalList fragment = new RetrievalList();
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, fragment);
                            fragmentTransaction.commit();
                        }
                    }).create();
            builder.show();
        }
        //}

    }



}

package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
            int id = getArguments().getInt("ReqID");
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
                cancel.setText("REJECT");
                approve.setVisibility(View.VISIBLE);

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RequisitionDetailFragment.this.getActivity(), "DD or DH click", Toast.LENGTH_SHORT).show();
                    }
                });

                approve.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RequisitionDetailFragment.this.getActivity(), "DD or DH click", Toast.LENGTH_SHORT).show();
                    }
                });

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
        adapter.SetOnItemClickListener(new RequisitionFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                JSONReqDetail selected = allItems.get(position);
                Toast.makeText(RequisitionDetailFragment.this.getActivity(), "Click " + position, Toast.LENGTH_SHORT).show();
            }
        });
        System.out.println("USERROLE:::::"+Setup.user.getRoleID());
        if(status.getText().equals("Pending Approval")) {
            cancel.setOnClickListener(this);
        }
        else if (status.getText().equals("Pending Approval") && Setup.user.getRoleID().equals("SC")) {
            cancel.setVisibility(View.GONE);
        }
        else {
            cancel.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public void onClick(View v) {

        Boolean checkIfNoNull = true;
        // Check for a valid password, if the user entered one.
        for (int i=0; i<allItems.size(); i++) {
            if(allItems.get(i).getRequestQty() == 0) {
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

package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
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
import team5.ad.sa40.stationeryinventory.Model.Retrieval;
import team5.ad.sa40.stationeryinventory.Model.RetrievalDetail;


public class RetrievalFormDetails extends android.support.v4.app.Fragment implements AdapterView.OnClickListener {

    private Retrieval ret;
    private List<RetrievalDetail> allItems;
    private RecyclerView mRecyclerView;
    private RetFormAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.ret_detail_retID) TextView retFormID;
    @Bind(R.id.ret_detail_reqId) TextView requsitionForms;
    @Bind(R.id.ret_detail_date) TextView retrievalDate;
    @Bind(R.id.ret_detail_submit) Button submitBtn;

    public RetrievalFormDetails() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ret = new Retrieval();

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_retrieval_form_details, container, false);
        ButterKnife.bind(this, view);
        Setup s = new Setup();

        if (getArguments() != null) {
            Log.i("arguments: ",getArguments().toString());
            ret.setRetID(getArguments().getInt("RetID"));
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            Log.i("date passed:",getArguments().getString("RetDate"));
            try {
                Date d = format.parse(getArguments().getString("RetDate"));
                System.out.println(d);
                ret.setDate(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            ret.setStatus(getArguments().getString("RetStatus"));
        }
        retrievalDate.setText(Setup.parseDateToString(ret.getDate()));

        String idDisplay = "";
        int id = ret.getRetID();
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
        retFormID.setText(idDisplay);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_detail_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new RetFormAdapter(ret.getRetID(),ret.getStatus());
        allItems = new ArrayList<RetrievalDetail>();
        allItems = adapter.mRetrievalDetails;
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RetFormAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                RetrievalDetail selected = allItems.get(position);
            }
        });
        requsitionForms.setText(adapter.mReqForms);

        if(ret.getStatus() == "Pending") {
            submitBtn.setOnClickListener(this);
        }
        else {
            submitBtn.setVisibility(View.GONE);
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
                builder.setMessage("Submission of Retrieval Form #" + ret.getRetID() + " failed. Please try again.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                builder.show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Retrieval Form #" + ret.getRetID() + " has been successfully submitted! Please login to the web for allocation of items retrieved.")
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

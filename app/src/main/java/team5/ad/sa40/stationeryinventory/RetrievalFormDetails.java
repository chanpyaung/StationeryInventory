package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
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
    private View vBtn;
    private String btnPressedNameSuccess = "Error. Please try again.";
    private String btnPressedNameFail = "Error. Please try again.";

    @Bind(R.id.ret_detail_retID) TextView retFormID;
    @Bind(R.id.ret_detail_reqId) TextView requsitionForms;
    @Bind(R.id.ret_detail_date) TextView retrievalDate;
    @Bind(R.id.ret_detail_save) Button saveBtn;
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
            Log.i("arguments: ", getArguments().toString());
            ret.setRetID(getArguments().getInt("RetID"));
            if (getArguments().getString("RetDate") != "" || getArguments().getString("RetDate") != null) {
                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                Log.i("date passed:", getArguments().getString("RetDate"));
                try {
                    Date d = format.parse(getArguments().getString("RetDate"));
                    System.out.println(d);
                    ret.setDate(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                retrievalDate.setText(Setup.parseDateToString(ret.getDate()));
            }
            ret.setStatus(getArguments().getString("RetStatus"));
        }

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
        Log.i("status: ", ret.getStatus());
        if(ret.getStatus().equals("PENDING")) {
            submitBtn.setOnClickListener(this);
            saveBtn.setOnClickListener(this);
        }
        else {
            submitBtn.setVisibility(View.GONE);
            saveBtn.setVisibility(View.GONE);
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_detail_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        allItems = new ArrayList<RetrievalDetail>();

        new AsyncTask<Void, Void, RetFormAdapter>(){
            @Override
            protected RetFormAdapter doInBackground(Void... params) {
                adapter = new RetFormAdapter(ret.getRetID(),ret.getStatus());
                return adapter;
            }
            @Override
            protected void onPostExecute(RetFormAdapter result) {
                allItems = result.mRetrievalDetails;
                ret = result.mRetrieval;
                mRecyclerView.setAdapter(result);
                result.SetOnItemClickListener(new RetFormAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        RetrievalDetail selected = allItems.get(position);
                    }
                });
                requsitionForms.setText(result.mReqForms);

            }
        }.execute();

        return view;
    }

    @Override
    public void onClick(View v) {

        vBtn = v;
        String btnPressedName;
        if(vBtn.getId() == R.id.ret_detail_submit) {
            btnPressedName = "submit";
            btnPressedNameSuccess = "Retrieval Form #" + ret.getRetID() + " has been successfully submitted! Please login to the web to allocate items retrieved.";
            btnPressedNameFail = "Submission of Retrieval Form #" + ret.getRetID() + " failed. Please try again.";
        }
        else {
            btnPressedName = "save";
            btnPressedNameSuccess = "Retrieval Form #" + ret.getRetID() + " has been successfully saved!";
            btnPressedNameFail = "Unable to save Retrieval Form #" + ret.getRetID() + ". Please try again.";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are you sure you want to " + btnPressedName + " this retrieval form?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                String result = "";
                                if (vBtn.getId() == R.id.ret_detail_submit) {
                                    result = Retrieval.submitRetrieval(ret, allItems);
                                } else {
                                    result = Retrieval.saveRetrieval(ret, allItems);
                                }
                                return result;
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                Log.i("result->", result);
                                //process retrieval update status
                                if (result == null) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(btnPressedNameFail)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    RetrievalList fragment = new RetrievalList();
                                                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                    fragmentTransaction.replace(R.id.frame, fragment);
                                                    fragmentTransaction.commit();
                                                }
                                            }).create();
                                    builder.show();
                                } else {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setMessage(btnPressedNameSuccess)
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

                            }
                        }.execute();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }}).create();
        builder.show();
        }
    }

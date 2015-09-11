package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjustmentItemListFragment extends android.support.v4.app.Fragment {

    public static List<JSONAdjustmentDetail> reportItemList;
    private RecyclerView mRecyclerView;
    private ReportItemListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.btnSubmit) Button btnSubmit;

    public AdjustmentItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_adjustment_item_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.report_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //get stored list of items to be reported and generated in an adjustment voucher.
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        Set<String> jsonArray = appSharedPrefs.getStringSet("ReportItemList", new HashSet<String>());
        Log.i("SharedPref-json array:", jsonArray.toString());
        reportItemList = new ArrayList<JSONAdjustmentDetail>();
        if(jsonArray.size() > 0) {
            String[] reportItems = jsonArray.toArray(new String[jsonArray.size()]);
            Log.i("string[] reportItems:", reportItems.toString());
            for(int i=0; i<reportItems.length; i++) {
                JSONAdjustmentDetail obj = gson.fromJson(reportItems[i], JSONAdjustmentDetail.class);
                Log.i("in json:",obj.getItemID());
                reportItemList.add(obj);
            }
        }
        Log.i("NEW json array:", jsonArray.toString());
        prefsEditor.putStringSet("ReportItemList", jsonArray);
        prefsEditor.commit();

        adapter = new ReportItemListAdapter(reportItemList);
        mRecyclerView.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to submit this list and ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {









/*


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
                                            builder.setMessage("")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            ReportItemListFragment fragment3 = new ReportItemListFragment();
                                                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(R.id.frame, fragment3).commit();
                                                        }
                                                    }).create();
                                            builder.show();
                                        } else {
                                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                            builder.setMessage("")
                                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {
                                                            ReportItemListFragment fragment3 = new ReportItemListFragment();
                                                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                                            fragmentTransaction.replace(R.id.frame, fragment3).commit();
                                                        }
                                                    }).create();
                                            builder.show();
                                        }

                                    }
                                }.execute();*/

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }}).create();
                builder.show();
            }
        });

        return view;
    }


}

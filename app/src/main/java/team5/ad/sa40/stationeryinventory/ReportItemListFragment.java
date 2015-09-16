package team5.ad.sa40.stationeryinventory;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemListFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener {

    public static List<JSONAdjustmentDetail> reportItemList;
    private RecyclerView mRecyclerView;
    private ReportItemListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.add_new_item) Button addItemBtn;
    @Bind(R.id.adjVouchers) Button adjVoucherList;
    @Bind(R.id.genAdjVoucher) Button generateAdjVoucher;

    public ReportItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_item_list, container, false);
        ButterKnife.bind(this, view);

        getActivity().setTitle("Adjustment");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.report_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //get stored list of items to be reported and generated in an adjustment voucher.
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        Set<String> jsonArray = appSharedPrefs.getStringSet("ReportItemsList", new HashSet<String>());
        Log.i("SharedPref-json array:", jsonArray.toString());
        reportItemList = new ArrayList<JSONAdjustmentDetail>();
        if(jsonArray.size() > 0) {
            String[] reportItems = jsonArray.toArray(new String[jsonArray.size()]);
            Log.i("string[] reportItems:", String.valueOf(reportItems.length));
            for(int i=0; i<reportItems.length; i++) {
                JSONAdjustmentDetail obj = gson.fromJson(reportItems[i], JSONAdjustmentDetail.class);
                Log.i("in json:",obj.getItemID());
                reportItemList.add(obj);
            }
        }
        Log.i("NEW json array:", jsonArray.toString());
        prefsEditor.putStringSet("ReportItemsList", jsonArray);
        prefsEditor.commit();

        adapter = new ReportItemListAdapter(reportItemList);
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new ReportItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final JSONAdjustmentDetail selectedItem = reportItemList.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Are you sure you want to delete item " + selectedItem.getItemID() + " from adjustment voucher list?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SharedPreferences appSharedPrefs = PreferenceManager
                                        .getDefaultSharedPreferences(getActivity().getApplicationContext());
                                SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
                                Set<String> jsonArray = new HashSet<String>();
                                Log.i("SharedPref-json array:", jsonArray.toString());
                                reportItemList.remove(selectedItem);
                                if (reportItemList.size() > 0) {
                                    for (int i = 0; i < reportItemList.size(); i++) {
                                        Gson gson = new Gson();
                                        String json = gson.toJson(reportItemList.get(i));
                                        Log.i("in json:", reportItemList.get(i).getItemID());
                                        jsonArray.add(json);
                                    }
                                }
                                Log.i("NEW json array:", jsonArray.toString());
                                prefsEditor.putStringSet("ReportItemsList", jsonArray);
                                prefsEditor.commit();

                                ReportItemListFragment fragment3 = new ReportItemListFragment();
                                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.replace(R.id.frame, fragment3).commit();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create();
                builder.show();
            }
        });

        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("Add Report Item", "Add Report Item");
                Log.i("goto inv: ", "Add Report Item");

                InventoryList fragment = new InventoryList();
                fragment.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("TAG").commit();
            }
        });
        adjVoucherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdjVouList fragment2 = new AdjVouList();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment2).addToBackStack("TAG").commit();
            }
        });
        generateAdjVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reportItemList.size() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Unable to generate an empty list.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            }).create();
                    builder.show();
                } else {
                    AdjustmentItemListFragment fragment3 = new AdjustmentItemListFragment();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.frame, fragment3).addToBackStack("TAG").commit();
                }
            }
        });

        return view;
    }

    @Override
    public void doBack(){
        ReportItemListFragment frag = new ReportItemListFragment();
        android.support.v4.app.FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.frame, frag).commit();
    }

}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.CustomDisbursementDetail;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursementDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisListDetailItemList extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListDetailItemAdapter mAdapter;
    JSONDisbursement dis;

    @Bind(R.id.inv_detail_itemName)TextView disName;
    public DisListDetailItemList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dis_list_detail_item_list, container, false);
        ButterKnife.bind(this, v);

        getActivity().setTitle("Disbursement Detail Items");
        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        Bundle bundle = this.getArguments();
        ArrayList<JSONItem> items = (ArrayList<JSONItem>)bundle.getSerializable("items");
        ArrayList<JSONDisbursementDetail> disList = (ArrayList<JSONDisbursementDetail>) bundle.getSerializable("disbursementDetail");
        dis = (JSONDisbursement)bundle.getSerializable("disbursement");
        ArrayList<CustomDisbursementDetail> customDisbursementDetails = new ArrayList<>();

        Log.e("Size of items", String.valueOf(items.size()));
        Log.e("Size of disbursements", String.valueOf(disList.size()));
        disName.setText(String.valueOf(disList.get(0).getDisID()));
        for(int i = 0; i < disList.size(); i++){
            for(int a = 0; a <items.size(); a++){

                if(disList.get(i).getItemID().equals(items.get(a).getItemID())){
                    CustomDisbursementDetail c = new CustomDisbursementDetail(items.get(a).getItemName(), disList.get(i).getQty());
                    customDisbursementDetails.add(c);
                }
            }
        }
        Log.e("Size of custom", String.valueOf(customDisbursementDetails.size()));
        mRecyclerView = (RecyclerView)v.findViewById(R.id.ret_detail_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DisListDetailItemAdapter(customDisbursementDetails);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }


    @Override
    public void doBack() {
        DisbursementListDetail fragment = new DisbursementListDetail();
        Bundle bundle = new Bundle();
        bundle.putSerializable("disbursement", dis);
        fragment.setArguments(bundle);
        FragmentTransaction fragtran = getFragmentManager().beginTransaction();
        fragtran.replace(R.id.frame, fragment).commit();
    }
}


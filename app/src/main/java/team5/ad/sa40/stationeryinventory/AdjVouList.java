package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.json.JSONException;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjVouList extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AdjListGridAdapter mAdapter;
    private List<JSONAdjustment> mAdjustment;
    String[] stat_ary = {"View All","PENDING", "APPROVED", "REJECTED"};

    @Bind(R.id.spnStat) Spinner spn_status;
    public AdjVouList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_adj_vou_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ShowAllAdjustments();


        return view;
    }

    public void ShowAllAdjustments(){

        JsonObject jobj = new JsonObject();
        jobj.addProperty("adjId", "null");
        jobj.addProperty("startDate", "null");
        jobj.addProperty("endDate", "null");

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        AdjustmentAPI adjustmentAPI = restAdapter.create(AdjustmentAPI.class);
        adjustmentAPI.getAdjVoucher(jobj, new Callback<List<JSONAdjustment>>() {
            @Override
            public void success(List<JSONAdjustment> adjustments, Response response) {
                mAdjustment = adjustments;
                mAdapter = new AdjListGridAdapter("Adj", mAdjustment);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        android.support.v4.app.Fragment frag = new AdjListDetail();
                        Bundle bundle = new Bundle();
                        JSONAdjustment temp = mAdjustment.get(position);
                        bundle.putSerializable("adjustment", temp);
                        frag.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                                .commit();
                    }
                });

                ArrayAdapter<String> col_collect = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, stat_ary);
                col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spn_status.setAdapter(col_collect);

                spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (spn_status.getSelectedItemPosition() > 0) {
                            FilterAdjustments(spn_status.getSelectedItem().toString());
                        } else {
                            mAdapter.mAdjustments = mAdjustment;
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getAdjVoucher", error.toString());
            }
        });
    }

    public void FilterAdjustments(String status){

        ArrayList<JSONAdjustment> temp = new ArrayList<>();
        for(int i=0; i<mAdjustment.size(); i++){
            if (status.equals(mAdjustment.get(i).getStatus())){
                temp.add(mAdjustment.get(i));
            }
        }
        mAdapter.mAdjustments = temp;
        mAdapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.clerk_dis_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            //android.support.v4.app.Fragment frag = new AdjListSearch();
            //getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Adj").commit();
        }
        return super.onOptionsItemSelected(item);
    }
}

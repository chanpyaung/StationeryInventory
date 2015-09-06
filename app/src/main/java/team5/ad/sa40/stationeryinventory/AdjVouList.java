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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjVouList extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AdjListGridAdapter mAdapter;
    private List<Adjustment> mAdjustment;
    String[] stat_ary = {"View All","Pending", "Approved", "Rejected"};

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

        ArrayAdapter<String> col_collect = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item, stat_ary);
        col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spn_status.setAdapter(col_collect);

        spn_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(spn_status.getSelectedItemPosition() > 0){
                    FilterAdjustments(spn_status.getSelectedItem().toString());
                }
                else{
                    ShowAllAdjustments();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return view;
    }

    public void ShowAllAdjustments(){
        mAdapter = new AdjListGridAdapter("Adj");
        mRecyclerView.setAdapter(mAdapter);
        mAdjustment = mAdapter.mAdjustments;
        mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                android.support.v4.app.Fragment frag = new AdjListDetail();
                Bundle bundle = new Bundle();
                Adjustment temp = mAdjustment.get(position);
                bundle.putSerializable("adjustment", temp);
                frag.setArguments(bundle);
                Log.i("Reached into method", "Hello");
                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                        .commit();
            }
        });
    }

    public void FilterAdjustments(String status){

        ArrayList<Adjustment> temp = new ArrayList<>();
        for(int i=0; i<mAdjustment.size(); i++){
            if (status == mAdjustment.get(i).getStatus()){
                temp.add(mAdjustment.get(i));
            }
        }
        mAdapter.mAdjustments = temp;
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                android.support.v4.app.Fragment frag = new AdjListDetail();
                Bundle bundle = new Bundle();
                Adjustment temp = mAdjustment.get(position);
                bundle.putSerializable("adjustment", temp);
                frag.setArguments(bundle);
                Log.i("Reached into method", "Hello");
                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                        .commit();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mAdjustment = new ArrayList<>();
        for(Adjustment c: mAdapter.mAdjustments){
            mAdjustment.add(c);
        }
        mAdapter = new AdjListGridAdapter("Adj");
        mRecyclerView.setAdapter(mAdapter);
        Log.i("Size of created list", String.valueOf(mAdjustment.size()));
        mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });
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
            android.support.v4.app.Fragment frag = new AdjListSearch();
            getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Adj").commit();
        }
        return super.onOptionsItemSelected(item);
    }
}

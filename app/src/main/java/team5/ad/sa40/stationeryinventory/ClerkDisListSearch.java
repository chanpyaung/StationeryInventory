package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClerkDisListSearch extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListGridAdapter mAdapter;
    private List<Disbursement> mDisbursement;

    @Bind(R.id.txtSearch) EditText txtSearch;
    @Bind(R.id.btnSearch) Button btnSearch;

    public ClerkDisListSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_clerk_dis_list_search, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new DisListGridAdapter("Store");
        mRecyclerView.setAdapter(mAdapter);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //search button action
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mDisbursement = new ArrayList<>();
        if(mAdapter.mdisbursements.size() > 1){
         for(Disbursement c: mAdapter.mdisbursements){
                mDisbursement.add(c);
            }
        }
        mAdapter = new DisListGridAdapter("Store");
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                android.support.v4.app.Fragment frag = new ClerkDisListDetail();
                Bundle bundle = new Bundle();
                Disbursement temp = mDisbursement.get(position);
                bundle.putSerializable("disbursement", temp);
                frag.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Dis")
                        .commit();
            }
        });
    }
}

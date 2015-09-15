package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClerkDisListSearch extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListGridAdapter mAdapter;
    private List<JSONDisbursement> mDisbursement;
    private List<JSONCollectionPoint> mCollectionPoint;

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

        getActivity().setTitle("Disbursement Search");

        Bundle bundle = this.getArguments();
        mCollectionPoint = (ArrayList<JSONCollectionPoint>)bundle.getSerializable("collection");
        //mCollectionPoint = (ArrayList<JSONCollectionPoint>)this.getArguments().getSerializable("collection");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search button action
                if (txtSearch.getText().toString().matches("")) {
                    txtSearch.setError("Fill Data!");
                } else {
                    txtSearch.setError(null);
                    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                    DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
                    disbursementAPI.getDisbursementByDisID(Integer.parseInt(txtSearch.getText().toString()), new Callback<List<JSONDisbursement>>() {
                        @Override
                        public void success(List<JSONDisbursement> jsonDisbursements, Response response) {
                            mDisbursement = jsonDisbursements;
                            mAdapter = new DisListGridAdapter("Search", mDisbursement, mCollectionPoint);
                            mRecyclerView.setAdapter(mAdapter);
                            if (mDisbursement.size() < 1) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Information")
                                        .setMessage("No disbursement is found!")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else if (mDisbursement.size() > 1) {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Information")
                                        .setMessage(mDisbursement.size() + " disbursements are found!")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            } else {
                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Information")
                                        .setMessage(mDisbursement.size() + " disbursement is found!")
                                        .setCancelable(false)
                                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                            mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    android.support.v4.app.Fragment frag = new ClerkDisListDetail();
                                    Bundle bundle = new Bundle();
                                    JSONDisbursement temp = mDisbursement.get(position);
                                    bundle.putSerializable("disbursement", temp);
                                    for (int i = 0; i < mCollectionPoint.size(); i++) {
                                        if (temp.getCPID() == mCollectionPoint.get(i).getCPID()) {
                                            bundle.putSerializable("collection", mCollectionPoint.get(i));
                                        }
                                    }
                                    frag.setArguments(bundle);
                                    getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                                            .commit();
                                }
                            });
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("getDisbursementByDisID", error.toString());
                        }
                    });
                }
            }
        });

        return view;
    }

}

package team5.ad.sa40.stationeryinventory.Fragment;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.ReportAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsListFragment extends android.support.v4.app.Fragment implements AnalyticsAdapter.OnItemClickListener, MainActivity.OnBackPressedListener{

    private RecyclerView mRecyclerView;
    private AnalyticsAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public AnalyticsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.fragment_analytics_list, container, false);
        getActivity().setTitle("Analytics Reports");
        mRecyclerView = (RecyclerView) view.findViewById(R.id.analytics_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AnalyticsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new AnalyticsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Bundle args = new Bundle();
                args.putInt("ReportID", mAdapter.mReports.get(position).getReportID());
                args.putString("ReportName", mAdapter.mReports.get(position).getTitle());
                AnalyticsFragment aFrag = new AnalyticsFragment();
                aFrag.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.frame, aFrag).addToBackStack("ReportList").commit();
            }
        });
        return view;
    }


    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    public void doBack() {
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        ReportAPI rpAPI = restAdapter.create(ReportAPI.class);
        rpAPI.getReports(new Callback<List<JSONReport>>() {
            @Override
            public void success(List<JSONReport> jsonReports, Response response) {
                AnalyticsAdapter.mReports = jsonReports;
                AnalyticsListFragment afrag = new AnalyticsListFragment();
                getFragmentManager().beginTransaction().replace(R.id.frame, afrag).commit();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Fail getReports", error.toString());
            }
        });
    }
}

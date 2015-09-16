package team5.ad.sa40.stationeryinventory;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AnalyticsListFragment extends android.support.v4.app.Fragment implements AnalyticsAdapter.OnItemClickListener{

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
}

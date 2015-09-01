package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GridAdapter();
        mRecyclerView.setAdapter(mAdapter);
        return  view;
    }


}

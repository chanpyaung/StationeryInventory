package team5.ad.sa40.stationeryinventory;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemListFragment extends android.support.v4.app.Fragment {


    public ReportItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_item_list, container, false);
        //ButterKnife.bind(this, view);

        return view;
    }


}

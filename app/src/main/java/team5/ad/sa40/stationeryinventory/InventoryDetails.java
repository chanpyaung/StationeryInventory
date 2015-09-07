package team5.ad.sa40.stationeryinventory;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Item;


public class InventoryDetails extends android.support.v4.app.Fragment {

    private Item ret;
    private RecyclerView mRecyclerView;
    private RetFormAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    public InventoryDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_inventory_details, container, false);
        return view;
    }


}

package team5.ad.sa40.stationeryinventory;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class InventoryStockCardAdd extends android.support.v4.app.Fragment {


    public InventoryStockCardAdd() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_inventory_stockcard_add, container, false);
    }


}

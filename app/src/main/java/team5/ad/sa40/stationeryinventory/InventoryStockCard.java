package team5.ad.sa40.stationeryinventory;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.StockCard;


public class InventoryStockCard extends android.support.v4.app.Fragment  {

    private String itemID = "";
    private RecyclerView mRecyclerView;
    private SCListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.addStockBtn) View addStockBtn;

    public InventoryStockCard() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_inventory_stockcard, container, false);
        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            Log.i("arguments: ",getArguments().toString());
            itemID = getArguments().getString("ItemID");
        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.stockcard_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        adapter = new SCListAdapter(itemID);
        mRecyclerView.setAdapter(adapter);

        addStockBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ItemID", itemID);
                Log.i("selected itemID: ", itemID);

                InventoryStockCardAdd fragment = new InventoryStockCardAdd();
                fragment.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}

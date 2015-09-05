package team5.ad.sa40.stationeryinventory;


import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.Retrieval;


public class InventoryList extends android.support.v4.app.Fragment {


    private String empID;
    private String[] filters = {"View All","Pending","Retrieved"};
    private List<Item> allInv;
    private RecyclerView mRecyclerView;
    private InvListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Bind(R.id.spinnerInvCat) Spinner spinnerInvCat;
    @Bind(R.id.spinnerInvStatus) Spinner spinnerInvStatus;

    public InventoryList() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_inventory_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.inv_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        showAllInventory();

        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,filters);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerInvStatus.setAdapter(FiltersAdapter);

        spinnerInvStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(InventoryList.this.getActivity(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                Log.i("spinner's:", filters[position]);
                switch (position) {
                    case (0):
                        showAllInventory();
                        break;
                    case (1):
                        //showPendingInventory();
                        break;
                    case (2):
                        //showRetrievedInventory();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showAllInventory();
            }
        });

        return view;
    }

    public void showAllInventory() {
        adapter = new InvListAdapter();
        allInv= new ArrayList<Item>();
        allInv = adapter.mItems;
        Collections.sort(allInv);
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new InvListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Item selected = allInv.get(position);
                Bundle args = new Bundle();
                args.putString("ItemID", selected.getItemID());
                Log.i("selected itemID: ", selected.getItemID());

                        /*
                RetrievalFormDetails fragment = new RetrievalFormDetails();
                fragment.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();*/

            }
        });
    }

}

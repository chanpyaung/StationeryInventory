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


    private String[] categories = {"All Categories","Clip","Envelope","Eraser","Exercise","File","Pen","Puncher",
            "Pad","Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Tparency","Tray"};
    private String[] filters = {"View All","Low Stock","Available"};
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
                        showLowInventory();
                        break;
                    case (2):
                        showAvailableInventory();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showAllInventory();
            }
        });

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,categories);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerInvCat.setAdapter(FiltersAdapter);

        spinnerInvCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(InventoryList.this.getActivity(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                Log.i("spinner's:", categories[position]);
                switch (position) {
                    case (0):
                        showAllInventory();
                        break;
                    case (1):
                        showCategoryItems(1);
                        break;
                    case (2):
                        showCategoryItems(2);
                        break;
                    case (3):
                        showCategoryItems(3);
                        break;
                    case (4):
                        showCategoryItems(4);
                        break;
                    case (5):
                        showCategoryItems(5);
                        break;
                    case (6):
                        showCategoryItems(6);
                        break;
                    case (7):
                        showCategoryItems(7);
                        break;
                    case (8):
                        showCategoryItems(8);
                        break;
                    case (9):
                        showCategoryItems(9);
                        break;
                    case (10):
                        showCategoryItems(10);
                        break;
                    case (11):
                        showCategoryItems(11);
                        break;
                    case (12):
                        showCategoryItems(12);
                        break;
                    case (13):
                        showCategoryItems(13);
                        break;
                    case (14):
                        showCategoryItems(14);
                        break;
                    case (15):
                        showCategoryItems(15);
                        break;
                    case (16):
                        showCategoryItems(16);
                        break;
                    case (17):
                        showCategoryItems(17);
                        break;
                    case (18):
                        showCategoryItems(18);
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

    public void showLowInventory() {
        List<Item> lowStockItems = new ArrayList<Item>();
        for(int i=0; i<allInv.size(); i++) {
            Item item = allInv.get(i);
            if(item.getStock() < item.getRoLvl()) {
                lowStockItems.add(item);
            }
        }
        adapter.mItems = lowStockItems;
        mRecyclerView.setAdapter(adapter);
    }

    public void showAvailableInventory() {
        List<Item> availableItems = new ArrayList<Item>();
        for(int i=0; i<allInv.size(); i++) {
            Item item = allInv.get(i);
            if(!(item.getStock() < item.getRoLvl())) {
                availableItems.add(item);
            }
        }
        adapter.mItems = availableItems;
        mRecyclerView.setAdapter(adapter);
    }

    public void showCategoryItems(int catID) {
        List<Item> categoryItems = new ArrayList<Item>();
        for(int i=0; i<allInv.size(); i++) {
            Item item = allInv.get(i);
            if(item.getItemCatID() == catID) {
                categoryItems.add(item);
            }
        }
        adapter.mItems = categoryItems;
        mRecyclerView.setAdapter(adapter);
    }
}

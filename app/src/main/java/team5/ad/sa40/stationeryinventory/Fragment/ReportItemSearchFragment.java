package team5.ad.sa40.stationeryinventory.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemSearchFragment extends android.support.v4.app.Fragment implements View.OnClickListener {

    @Bind(R.id.search_itemCode) EditText searchItemCode;
    @Bind(R.id.search_itemName) EditText searchItemName;
    @Bind(R.id.spinner_item_category) Spinner spinnerCateogry;
    @Bind(R.id.btnSearchItem) Button btnSearchItem;

    private List<JSONItem> searchResultsList;
    private List<JSONItem> itemList;
    private String[] categories = {"All Categories","Clip","Envelope","Eraser","Exercise","File","Pen","Puncher",
            "Pad","Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Tparency","Tray"};
    private RecyclerView mRecyclerView;
    private InvListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;


    public ReportItemSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_item_search, container, false);
        ButterKnife.bind(this,view);

        getActivity().setTitle("Search Item");

        searchItemCode.setVisibility(View.GONE);
        itemList = new ArrayList<JSONItem>();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.inv_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //load spinner
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,categories);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCateogry.setAdapter(FiltersAdapter);

        //connect to server and handled JSON here
        Log.i("itemList:", String.valueOf(itemList.size()));
        if(itemList.size() == 0) {
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
            InventoryAPI invAPI = restAdapter.create(InventoryAPI.class);

            invAPI.getList(new Callback<List<JSONItem>>() {
                @Override
                public void success(List<JSONItem> jsonItems, Response response) {
                    Log.i("Result :", jsonItems.toString());
                    Log.i("First item: ", jsonItems.get(0).getItemID().toString());
                    Log.i("Response: ", response.getBody().toString());
                    System.out.println("Response Status " + response.getStatus());
                    itemList = jsonItems;
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("Error: ", error.toString());
                }
            });
        }

        btnSearchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItems();
            }
        });

        return  view;
    }

    @Override
    public void onClick (View v){

    }

    private void searchItems() {

        searchResultsList = new ArrayList<JSONItem>();
        adapter = new InvListAdapter(searchResultsList);
        mRecyclerView.setAdapter(adapter);

        int itemCat = spinnerCateogry.getSelectedItemPosition();

        String itemName = "";

        if(!searchItemName.getText().toString().matches("")){
            itemName = searchItemName.getText().toString();
        }

        for(int i = 0; i < itemList.size(); i++){
            JSONItem item = itemList.get(i);
            Log.e("itemList.get(i)", itemList.get(i).getItemName().toLowerCase());
            Log.e("itemName.toLowerCase()", itemName.toLowerCase());
            if(!spinnerCateogry.getSelectedItem().toString().equals("All Categories")){
                Log.e("Not categories", "Reach");
                if(itemList.get(i).getItemName().toLowerCase().contains(itemName.toLowerCase()) && itemList.get(i).getItemCatID() == spinnerCateogry.getSelectedItemPosition()){
                    searchResultsList.add(itemList.get(i));
                }
            }
            else{
                Log.e("Not categories", "Reach");
                if(itemList.get(i).getItemName().toLowerCase().contains(itemName.toLowerCase())){
                    searchResultsList.add(itemList.get(i));
                }
            }
        }

        Log.e("itemList size()", String.valueOf(searchResultsList.size()));
        for(JSONItem it: searchResultsList){
            Log.e("Item Name", it.getItemName());
        }

        // if no search result:
        if(searchResultsList.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Oops! No search result found. Please try again.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.dismiss();
                        }
                    }).create();
            builder.show();
        }
        else {
            displaySearchResults(searchResultsList);
        }
    }

    public void displaySearchResults(List<JSONItem> list){
        //connect to adapter
        adapter = new InvListAdapter(list);
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new InvListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                String itemID = searchResultsList.get(position).getItemID();

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                InventoryAPI invAPI = restAdapter.create(InventoryAPI.class);

                invAPI.getItemDetails(itemID, new Callback<JSONItem>() {
                    @Override
                    public void success(JSONItem jsonItem, Response response) {
                        Log.i("Result :", jsonItem.toString());
                        Log.i("First item: ", jsonItem.getItemID().toString());
                        Log.i("Response: ", response.getBody().toString());
                        System.out.println("Response Status " + response.getStatus());

                        InventoryDetails detailsFragment = new InventoryDetails();
                        detailsFragment.item = jsonItem;
                        android.support.v4.app.FragmentTransaction fragmentTransaction2 = getFragmentManager().beginTransaction();
                        fragmentTransaction2.replace(R.id.frame, detailsFragment).addToBackStack("INVENTORYLIST TAG").commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error: ", error.toString());
                    }
                });
            }
        });
    }
}

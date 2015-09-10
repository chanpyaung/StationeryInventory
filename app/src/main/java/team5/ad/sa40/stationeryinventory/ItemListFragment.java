package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.ItemAPI;
import team5.ad.sa40.stationeryinventory.API.RequestCartAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONRequestCart;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.item_recycler_view) RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ItemListAdapter mAdapter;
    @Bind(R.id.searchItem) SearchView search;
    private List<JSONItem> mItems;

    public ItemListFragment() {
        // Required empty public constructor
        Log.i("Size of item List", String.valueOf(CategoryFragment.itemsbyCategory.size()));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_item_list,container,false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new ItemListAdapter();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mItems = new ArrayList<JSONItem>();
        ScaleInAnimationAdapter animatedAdapter = new ScaleInAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(animatedAdapter);
        for(JSONItem it: mAdapter.myItemlist){
            mItems.add(it);
            Log.i("Add to mitems:", it.getItemName());
        }
        Log.i("Size of mItems: ", String.valueOf(mItems.size()));
        mAdapter.SetOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ItemListFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<JSONItem> filteredModelList = filter(mItems, newText);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

    }

    private List<JSONItem> filter(List<JSONItem> items, String query) {
        query = query.toLowerCase();
        Log.i("Search: ", query);
        final List<JSONItem> filteredItemList = new ArrayList<>();
        for (JSONItem itemm : items) {
            final String text = itemm.getItemName().toLowerCase();
            Log.i("Item Name", text);
            if (text.contains(query)) {
                filteredItemList.add(itemm);
                Log.i("Search Result", itemm.getItemName() + itemm.toString());
            }
        }
        System.out.println("Result list: "+filteredItemList.get(filteredItemList.size()-1));
        return filteredItemList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_item_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_requestCart){
            //to load request cart list inside here
//            mAdapter.myItemlist = mAdapter.cartItemList;

            //Retrofit start here
            final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
            RequestCartAPI rqAPI = restAdapter.create(RequestCartAPI.class);
            rqAPI.getItemsbyEmpID(Setup.user.getEmpID(), new Callback<List<JSONRequestCart>>() {
                @Override
                public void success(final List<JSONRequestCart> jsonreqItems, Response response) {
                    if (response.getStatus() == 200) {
                        Log.i("RetrofitSuccess: ", String.valueOf(response.getStatus()));
                        //calling another API to get all items
                        ItemAPI itemAPI = restAdapter.create(ItemAPI.class);
                        itemAPI.getItems(new Callback<List<JSONItem>>() {
                            @Override
                            public void success(List<JSONItem> jsonItems, Response response) {
                                Setup.allitems = jsonItems;
                                Log.i("GettingAllItem: ", String.valueOf(Setup.allitems.size()));

                                //
                                if (jsonreqItems.size() > 0) {
                                    for (int i = 0; i < jsonreqItems.size(); i++) {
                                        String itemID = jsonreqItems.get(i).getItemID();
                                        int qty = jsonreqItems.get(i).getQty();
                                        for (JSONItem jitem : Setup.allitems) {
                                            if (itemID.equals(jitem.getItemID())) {
                                                jitem.setStock(qty);
                                                MainActivity.requestCart.add(jitem);
                                                Log.i("ITEM in reqCart: ", jitem.getItemID());
                                                RequestCartFragment reqCartFrag = new RequestCartFragment();
                                                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                                                fragTran.replace(R.id.frame, reqCartFrag).addToBackStack("REQUEST_CART TAG").commit();
                                            }
                                        }
                                    }
                                }
                                else {
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("Request Cart Empty")
                                            .setMessage("We acknowledge you that you haven't add any item yet. Please add some item before you proceed.")
                                            .setCancelable(false)
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mRecyclerView.setAdapter(mAdapter);
                                                }
                                            })
                                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    mRecyclerView.setAdapter(mAdapter);
                                                }
                                            })
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Fail", error.toString());
                            }
                        });
                        //Call to another Fragment
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    System.out.println("Retrofit Failed : " + error.toString());
                }
            });


//            Toast.makeText(this.getActivity(), "View Request Cart is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}

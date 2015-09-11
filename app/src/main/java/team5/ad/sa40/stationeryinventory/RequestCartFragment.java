package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.RequestCartAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONRequestCart;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestCartFragment extends android.support.v4.app.Fragment {


    @Bind(R.id.item_recycler_view)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RequestCartAdapter mAdapter;
    @Bind(R.id.searchItem)
    SearchView search;
    private List<JSONRequestCart> mItems;
    private List<JSONItem> itemList;

    public RequestCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_item_list,container,false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mAdapter = new RequestCartAdapter();
        Log.i("Request Cart Size: ", String.valueOf(MainActivity.requestCart.size()));
        mItems = new ArrayList<>();
        ScaleInAnimationAdapter animatedAdapter = new ScaleInAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(animatedAdapter);
        Log.i("mAdapterSize: ", String.valueOf(mAdapter.myItemlist.size()));
        for(JSONRequestCart it: mAdapter.myItemlist){
            mItems.add(it);
        }
        mAdapter.SetOnItemClickListener( new RequestCartAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RequestCartFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<JSONRequestCart> filteredModelList = filter(mItems, newText);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {

                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("EmpID", Setup.user.getEmpID());
                jsonObject.addProperty("ItemID", Setup.allRequestItems.get(viewHolder.getAdapterPosition()).getItemID());
                jsonObject.addProperty("Qty", Setup.allRequestItems.get(viewHolder.getAdapterPosition()).getQty());
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                RequestCartAPI rqAPI = restAdapter.create(RequestCartAPI.class);
                rqAPI.deletefromCart(jsonObject, new Callback<Boolean>() {
                    @Override
                    public void success(Boolean aBoolean, Response response) {
                        Setup.allRequestItems.remove(viewHolder.getAdapterPosition());
                        mAdapter.myItemlist = Setup.allRequestItems;
                        mRecyclerView.setAdapter(mAdapter);
                        Toast.makeText(RequestCartFragment.this.getActivity(), "Item removed from Cart.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });


            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    private List<JSONRequestCart> filter(List<JSONRequestCart> items, String query) {
        query = query.toLowerCase();

        final List<JSONRequestCart> filteredItemList = new ArrayList<>();
        for (JSONRequestCart itemm : items) {
            final String text = itemm.getItemName().toLowerCase();
            if (text.contains(query)) {
                filteredItemList.add(itemm);
            }
        }
        return filteredItemList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_requestcart_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_request_done){
            //to load request cart list inside here

            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
            final RequestCartAPI rqAPI = restAdapter.create(RequestCartAPI.class);
            rqAPI.getItemsbyEmpID(Setup.user.getEmpID(), new Callback<List<JSONRequestCart>>() {
                @Override
                public void success(List<JSONRequestCart> jsonRequestCarts, Response response) {
                    List<JsonObject> myRequest = new ArrayList<JsonObject>();
                    for(JSONRequestCart jitem : jsonRequestCarts){
                        JsonObject myItem = new JsonObject();
                        myItem.addProperty("EmpID", Setup.user.getEmpID());
                        myItem.addProperty("ItemID", jitem.getItemID());
                        myItem.addProperty("Qty", jitem.getQty());
                        myItem.addProperty("UOM", jitem.getUOM());
                        myRequest.add(myItem);
                        JsonObject delItem = new JsonObject();
                        delItem.addProperty("EmpID", Setup.user.getEmpID());
                        delItem.addProperty("ItemID", jitem.getItemID());
                        delItem.addProperty("Qty", jitem.getQty());
                        rqAPI.deletefromCart(delItem, new Callback<Boolean>() {
                            @Override
                            public void success(Boolean aBoolean, Response response) {
                                Log.i("Delete ", aBoolean.toString());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Delete Fail ", error.toString());
                            }
                        });
                    }
                    rqAPI.submit(myRequest, new Callback<Integer>() {
                        @Override
                        public void success(Integer integer, Response response) {
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Request Successful")
                                    .setMessage("Your have been submitted successfuly.")
                                    .setCancelable(false)
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            RequisitionListFragment rqListFrag = new RequisitionListFragment();
                                            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.frame, rqListFrag).addToBackStack("REQUEST_CART").commit();
                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                            Log.i("Success", String.valueOf(integer));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("fail submit", error.toString());
                        }
                    });
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("Failed calling rqCart", error.toString());
                }
            });

        }
        return super.onOptionsItemSelected(item);
    }

}

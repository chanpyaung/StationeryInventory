package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.ItemAPI;
import team5.ad.sa40.stationeryinventory.GridAdapter.OnItemClickListener;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener {

    public static List<JSONItem> itemsbyCategory = new ArrayList<JSONItem>();
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    GridAdapter mAdapter;
    SearchView search;
    private List<CategoryItem> mItems;

    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        getActivity().setTitle("Catalog");
        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GridAdapter();
        mRecyclerView.setAdapter(mAdapter);
        search = (SearchView)view.findViewById(R.id.searchCat);
        return  view;
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 2));
        mItems = new ArrayList<>();
        for(CategoryItem c: mAdapter.mItems){
            mItems.add(c);
        }
        mAdapter = new GridAdapter();
        AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(alphaAdapter);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                ItemAPI itemAPI = restAdapter.create(ItemAPI.class);

                itemAPI.getItemsByCategory(mAdapter.categoryNames[position], new Callback<List<JSONItem>>() {
                    @Override
                    public void success(List<JSONItem> jsonItems, Response response) {
                        itemsbyCategory = jsonItems;
                        System.out.println("JSON Result : " + jsonItems);
                        System.out.println("Response " + response.getStatus());
                        for (JSONItem jitem : jsonItems) {
                            System.out.println("ITem " + jitem.getItemID() + " " + jitem.getItemName());
                            //itemsbyCategory.add(jitem);
                        }
                        ItemListFragment iLFrag = new ItemListFragment();
                        Bundle args = new Bundle();
                        args.putString("CategoryName", mAdapter.categoryNames[position]);
                        Log.i("CategoryName", mAdapter.categoryNames[position]);
                        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                        iLFrag.setArguments(args);
                        fragTran.replace(R.id.frame, iLFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        System.out.println("Failure: " + error.toString());
                    }
                });

            }
        });
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<CategoryItem> filteredModelList = filter(mItems, newText);
                mAdapter.mItems = filteredModelList;
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });
    }

    private List<CategoryItem> filter(List<CategoryItem> models, String query) {
        query = query.toLowerCase();

        final List<CategoryItem> filteredModelList = new ArrayList<>();
        for (CategoryItem model : models) {
            final String text = model.getCatName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_category_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_scan){
            ScannerFragment scanFrag = new ScannerFragment();
            FragmentTransaction fragTran = getFragmentManager().beginTransaction();
            fragTran.replace(R.id.frame,scanFrag);
            fragTran.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        CategoryFragment fragment = new CategoryFragment();
        FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.replace(R.id.frame, fragment).commit();
    }
}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import team5.ad.sa40.stationeryinventory.GridAdapter.OnItemClickListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends android.support.v4.app.Fragment {

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

        mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GridAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //need to load item list based on category id
                //call api here
                ItemListFragment iLFrag = new ItemListFragment();
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragTran.replace(R.id.frame, iLFrag).commit();
                Toast.makeText(CategoryFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
            }
        });

        search = (SearchView)view.findViewById(R.id.searchCat);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<CategoryItem> filteredModelList = filter(mItems, newText);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
            public void onItemClick(View view, int position) {
                ItemListFragment iLFrag = new ItemListFragment();
                FragmentTransaction fragTran = getFragmentManager().beginTransaction();
                fragTran.replace(R.id.frame, iLFrag).addToBackStack("TAG").commit();
                Toast.makeText(CategoryFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
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


}

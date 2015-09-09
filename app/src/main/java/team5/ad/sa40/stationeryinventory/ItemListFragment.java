package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.AsyncTask;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import team5.ad.sa40.stationeryinventory.Model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemListFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.item_recycler_view) RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ItemListAdapter mAdapter;
    @Bind(R.id.searchItem) SearchView search;
    private List<Item> mItems;
    private List<Item> itemList;


    public ItemListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_item_list,container,false);
        mAdapter = new ItemListAdapter();
        Bundle args = getArguments();
        String catName = "";
        if(args!=null){
            catName = args.getString("CategoryName");
        }
        final String categoryName = catName;

        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(Void... params) {
                Log.i("From JSON", Item.getItemByCategory(categoryName).toString());
                return Item.getItemByCategory(categoryName);
            }
            @Override
            protected void onPostExecute(List<Item> result){
                itemList = result;
                mAdapter.myItemlist = itemList;
            }
        }.execute();
        ButterKnife.bind(this, view);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Item> filteredModelList = filter(mItems, newText);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        return view;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mAdapter = new ItemListAdapter();
        Bundle args = getArguments();
        String catName = "";
        if(args !=null ){
            catName = args.getString("CategoryName");
            Log.i("CategoryName from ", catName);
        }
        final String categoryName = catName;
        new AsyncTask<Void, Void, List<Item>>() {
            @Override
            protected List<Item> doInBackground(Void... params) {
                Log.i("Result from JSON",Item.getItemByCategory(categoryName).toString());
                return Item.getItemByCategory(categoryName);
            }
            @Override
            protected void onPostExecute(List<Item> result){
                itemList = new ArrayList<Item>();
                itemList = result;
                ItemListAdapter.myItemlist = itemList;
                System.out.println("Out form onPostExecute "+itemList.toString());
                System.out.println("I output"+ItemListAdapter.myItemlist.toString());
                for(int i=0; i<itemList.size(); i++){
                    Item item = itemList.get(i);
                    System.out.println("we are from onPostExecute "+ item.getItemName());
                }
            }
        }.execute();
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
//        mItems = new ArrayList<>();
        ScaleInAnimationAdapter animatedAdapter = new ScaleInAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(animatedAdapter);
//        for(Item it: itemList){
//            mItems.add(it);
//        }
        mAdapter.SetOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ItemListFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private List<Item> filter(List<Item> items, String query) {
        query = query.toLowerCase();

        final List<Item> filteredItemList = new ArrayList<>();
        for (Item itemm : items) {
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
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_item_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_requestCart){
            //to load request cart list inside here
            mAdapter.myItemlist = mAdapter.cartItemList;
            RequestCartFragment reqCartFrag = new RequestCartFragment();
            FragmentTransaction fragTran = getFragmentManager().beginTransaction();
            fragTran.replace(R.id.frame, reqCartFrag).addToBackStack("REQUEST_CART TAG").commit();
            Toast.makeText(this.getActivity(), "View Request Cart is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}

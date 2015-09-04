package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.adapters.ScaleInAnimationAdapter;
import team5.ad.sa40.stationeryinventory.Model.Item;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequestCartFragment extends android.support.v4.app.Fragment {


    @Bind(R.id.item_recycler_view)
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ItemListAdapter mAdapter;
    @Bind(R.id.searchItem)
    SearchView search;
    private List<Item> mItems;
    private List<Item> itemList;

    public RequestCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_item_list,container,false);
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
        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mAdapter = new ItemListAdapter();
        mAdapter.myItemlist = MainActivity.requestCart;
        mItems = new ArrayList<>();
        ScaleInAnimationAdapter animatedAdapter = new ScaleInAnimationAdapter(mAdapter);
        mRecyclerView.setAdapter(animatedAdapter);
        for(Item it: mAdapter.cartItemList){
            mItems.add(it);
        }
        mAdapter.SetOnItemClickListener(new ItemListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RequestCartFragment.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
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
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_requestcart_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_requestCart){
            //to load request cart list inside here

            Toast.makeText(this.getActivity(), "View Request Cart is clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

}

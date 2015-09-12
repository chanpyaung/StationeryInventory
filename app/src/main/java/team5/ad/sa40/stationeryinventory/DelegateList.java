package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
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

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.DelegateAPI;
import team5.ad.sa40.stationeryinventory.Model.Delegate;
import team5.ad.sa40.stationeryinventory.Model.JSONDelegate;


/**
 * A simple {@link Fragment} subclass.
 */
public class DelegateList extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DelegateGridAdapter mAdapter;
    private List<JSONDelegate> mDelegates;

    public DelegateList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_delegate_list, container, false);
        setHasOptionsMenu(true);

        new AlertDialog.Builder(getActivity())
                .setTitle("Information")
                .setMessage("Please swipe the item to delete!")
                .setCancelable(false)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        final DelegateAPI delegateAPI = restAdapter.create(DelegateAPI.class);
        delegateAPI.getDelegate(Setup.user.getDeptID(), new Callback<List<JSONDelegate>>() {
            @Override
            public void success(List<JSONDelegate> jsonDelegates, Response response) {
                mDelegates = jsonDelegates;
                mAdapter = new DelegateGridAdapter(mDelegates);
                mRecyclerView.setAdapter(mAdapter);

                mAdapter.SetOnItemClickListener(new DelegateGridAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        android.support.v4.app.Fragment frag = new AddNewDelegate();
                        Bundle bundle = new Bundle();
                        JSONDelegate temp = mDelegates.get(position);
                        bundle.putSerializable("delegate", temp);
                        frag.setArguments(bundle);
                        Log.i("Reached into method", "Hello");
                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                                .commit();
                    }
                });
                ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Delete Delegate")
                                .setMessage("Are you sure you want to delete this delegation?")
                                .setCancelable(false)
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.i("Position", String.valueOf(viewHolder.getPosition()));

                                        JSONDelegate temp = mDelegates.get(viewHolder.getPosition());
                                        Log.e("getDelegateSN", String.valueOf(temp.getDelegateSN()));
                                        delegateAPI.deleteDelegate(temp.getDelegateSN(), new Callback<Boolean>() {
                                            @Override
                                            public void success(Boolean aBoolean, Response response) {
                                                mDelegates.remove(viewHolder.getPosition());
                                                mAdapter.mDelegates = mDelegates;
                                                mRecyclerView.setAdapter(mAdapter);
                                            }

                                            @Override
                                            public void failure(RetrofitError error) {
                                                Log.e("deleteDelegate", error.toString());
                                                mRecyclerView.setAdapter(mAdapter);
                                            }
                                        });
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
                };
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                itemTouchHelper.attachToRecyclerView(mRecyclerView);

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.add_delegate_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            android.support.v4.app.Fragment frag = new AddNewDelegate();
            getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("del").commit();
        }
        return super.onOptionsItemSelected(item);
    }
}

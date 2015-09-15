package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.CollectionPointAPI;
import team5.ad.sa40.stationeryinventory.API.DepartmentAPI;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDepartment;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClerkDisList extends android.support.v4.app.Fragment implements Spinner.OnItemSelectedListener, MainActivity.OnBackPressedListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListGridAdapter mAdapter;
    List<JSONDisbursement> mDisbursement;
    List<JSONCollectionPoint> col_arr;
    List<JSONDepartment> dept_arr;
    String[] col_names;
    String[] dept_names;

    @Bind(R.id.spnCol) Spinner spnCol;
    @Bind(R.id.spnDept) Spinner spnDept;

    public ClerkDisList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_clerk_dis_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Disbursement List");

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        //binding values to spinners
        ShowAllDisbursements();

        //binding codes end here

        return view;
    }

    public void ShowAllDisbursements(){

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
        Log.e("Adapter:", "reach into constructor");
        Log.e("Setup.user.getDeptID()", String.valueOf(Setup.user.getDeptID()));
        disbursementAPI.getAllDisbursements(new Callback<List<JSONDisbursement>>() {
            @Override
            public void success(List<JSONDisbursement> jsonDisbursements, Response response) {
                Log.e("Response:", response.toString());
                Log.e("Size of json", String.valueOf(jsonDisbursements.size()));
                mDisbursement = jsonDisbursements;
                Log.e("Size of list", String.valueOf(mDisbursement.size()));
                CollectionPointAPI collectionPointAPI = restAdapter.create(CollectionPointAPI.class);

                collectionPointAPI.getAllCollectionPoints(new Callback<List<JSONCollectionPoint>>() {
                    @Override
                    public void success(List<JSONCollectionPoint> jsonCollectionPoints, Response response) {
                        col_arr = jsonCollectionPoints;

                        DepartmentAPI departmentAPI = restAdapter.create(DepartmentAPI.class);
                        departmentAPI.getAllDepartments(new Callback<List<JSONDepartment>>() {
                            @Override
                            public void success(List<JSONDepartment> jsonDepartments, Response response) {
                                dept_arr = jsonDepartments;

                                mAdapter = new DisListGridAdapter("Store", mDisbursement, col_arr);
                                mRecyclerView.setAdapter(mAdapter);
                                mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        android.support.v4.app.Fragment frag = new ClerkDisListDetail();
                                        Bundle bundle = new Bundle();
                                        JSONDisbursement temp = mDisbursement.get(position);
                                        bundle.putSerializable("disbursement", temp);
                                        for(int i = 0; i < col_arr.size(); i++){
                                            if(temp.getCPID() == col_arr.get(i).getCPID()){
                                                bundle.putSerializable("collection", col_arr.get(i));
                                            }
                                        }
                                        frag.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                                    }
                                });
                                //        col_arr = mAdapter.mCollectionPoints;
                                col_names = new String[col_arr.size() + 1];
                                col_names[0] = "All Collection Points";
                                for (int q = 0; q < col_arr.size(); q++) {
                                    col_names[q + 1] = col_arr.get(q).getCPName();
                                }

                                dept_names = new String[dept_arr.size() + 1];
                                dept_names[0] = "All Departments";
                                for (int i = 0; i < dept_arr.size(); i++) {
                                    dept_names[i + 1] = dept_arr.get(i).getDeptID();
                                }

                                for (int i = 0; i < col_names.length; i++) {
                                    Log.i("value at" + i, col_names[i]);
                                }

                                for (int i = 0; i < dept_names.length; i++) {
                                    Log.i("value at" + i, dept_names[i]);
                                }
                                Log.i("Size of col names ", String.valueOf(col_names.length));
                                Log.i("Size of dept names ", String.valueOf(dept_names.length));

                                ArrayAdapter<String> col_collect = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, col_names);
                                col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                spnCol.setAdapter(col_collect);

                                ArrayAdapter<String> dept_collect = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, dept_names);
                                dept_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                spnDept.setAdapter(dept_collect);
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("getAllCollectionPoints", error.toString());
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getDisbursementByDeptID", error.toString());
            }
        });
        spnCol.setOnItemSelectedListener(this);
        spnDept.setOnItemSelectedListener(this);

//        mAdapter = new DisListGridAdapter("Store");
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                android.support.v4.app.Fragment frag = new ClerkDisListDetail();
//                Bundle bundle = new Bundle();
//                JSONDisbursement temp = mDisbursement.get(position);
//                bundle.putSerializable("disbursement", temp);
//                frag.setArguments(bundle);
//                Log.i("Reached into method", "Hello");
//                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
//                        .commit();
//            }
//        });
    }

    public void FilterDisbursements(String col, String dept) {

        ArrayList<JSONDisbursement> fil_dis = new ArrayList<>();
        int col_id = -1;
        Log.i("col para", col);
        for (int i = 0; i < col_arr.size(); i++) {
            Log.i("room at " + i, col_arr.get(i).getCPName());
            if (col_arr.get(i).getCPName() == col) {
                col_id = col_arr.get(i).getCPID();
                break;
            }
        }
        if (spnCol.getSelectedItemPosition() != 0 && spnDept.getSelectedItemPosition() != 0) {
            for (int z = 0; z < mDisbursement.size(); z++) {
                if (mDisbursement.get(z).getDeptID().equals(dept) && mDisbursement.get(z).getCPID() == col_id) {
                    JSONDisbursement temp = mDisbursement.get(z);
                    fil_dis.add(temp);
                }
            }
        } else {
            for (int z = 0; z < mDisbursement.size(); z++) {
                if (mDisbursement.get(z).getDeptID().equals(dept) || mDisbursement.get(z).getCPID() == col_id) {
                    JSONDisbursement temp = mDisbursement.get(z);
                    fil_dis.add(temp);
                }
            }
        }

        mAdapter.mdisbursements = fil_dis;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(spnCol.getSelectedItemPosition() > 0 || spnDept.getSelectedItemPosition() > 0 ){
            FilterDisbursements(spnCol.getSelectedItem().toString(), spnDept.getSelectedItem().toString());
        }
        else if(spnCol.getSelectedItemPosition() == 0 && spnDept.getSelectedItemPosition() == 0 ){
            mAdapter.mdisbursements = mDisbursement;
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_dis_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            android.support.v4.app.Fragment frag = new ClerkDisListSearch();
            Bundle bundle = new Bundle();
            ArrayList<JSONCollectionPoint> temp = new ArrayList<JSONCollectionPoint>(col_arr);
            Log.e("temp.size()", String.valueOf(temp.size()));
            bundle.putSerializable("collection", temp);
            frag.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        android.support.v4.app.Fragment frag = new ClerkDisList();
        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
    }
}

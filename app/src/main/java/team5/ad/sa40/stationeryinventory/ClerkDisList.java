//package team5.ad.sa40.stationeryinventory;
//
//
//import android.app.Fragment;
//import android.os.Bundle;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Spinner;
//import android.widget.Toast;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
//import team5.ad.sa40.stationeryinventory.Model.Disbursement;
//import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
//import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
//
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class ClerkDisList extends android.support.v4.app.Fragment implements Spinner.OnItemSelectedListener{
//
//    RecyclerView mRecyclerView;
//    RecyclerView.LayoutManager mLayoutManager;
//    DisListGridAdapter mAdapter;
//    private List<JSONDisbursement> mDisbursement;
//    List<JSONCollectionPoint> col_arr;
//    String[] col_names;
//    String[] dept_names;
//
//    @Bind(R.id.spnCol) Spinner spnCol;
//    @Bind(R.id.spnDept) Spinner spnDept;
//
//    public ClerkDisList() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        inflater = getActivity().getLayoutInflater();
//        View view = inflater.inflate(R.layout.fragment_clerk_dis_list, container, false);
//        ButterKnife.bind(this, view);
//        setHasOptionsMenu(true);
//
//        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//
//        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        //binding values to spinners
//        ShowAllDisbursements();
//
//        ArrayAdapter<String> col_collect = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,col_names);
//        col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spnCol.setAdapter(col_collect);
//        spnCol.setOnItemSelectedListener(this);
//
//        ArrayAdapter<String> dept_collect = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,dept_names);
//        dept_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
//        spnDept.setAdapter(dept_collect);
//        spnDept.setOnItemSelectedListener(this);
//
//        //binding codes end here
//
//        return view;
//    }
//
//    public void ShowAllDisbursements(){
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
//        col_arr = mAdapter.mCollectionPoints;
//        col_names = new String[col_arr.size()+1];
//
//        col_names[0] = "All Collection Points";
//        for (int q=0; q<col_arr.size(); q++){
//            col_names[q+1] = col_arr.get(q).getCPName();
//        }
//
//        Log.i("Size of list", String.valueOf(mAdapter.mdisbursements.size()));
//        dept_names = new String[mAdapter.mdisbursements.size()+1];
//        dept_names[0] = "All Departments";
//        for(int i=0; i<mAdapter.mdisbursements.size(); i++){
//            dept_names[i+1] = mAdapter.mdisbursements.get(i).getDeptID();
//        }
//
//        for(int i=0; i< col_names.length; i++){
//            Log.i("value at" + i, col_names[i]);
//        }
//
//        for(int i=0; i< dept_names.length; i++){
//            Log.i("value at" + i, dept_names[i]);
//        }
//        Log.i("Size of col names ", String.valueOf(col_names.length));
//        Log.i("Size of dept names ", String.valueOf(dept_names.length));
//    }
//
//    public void FilterDisbursements(String col, String dept){
//
//        mAdapter = new DisListGridAdapter("Store");
//        mRecyclerView.setAdapter(mAdapter);
//
//        ArrayList<JSONDisbursement> fil_dis = new ArrayList<>();
//        int col_id = -1;
//        Log.i("col para",col);
//        for(int i=0; i< col_arr.size(); i++){
//            Log.i("room at " + i, col_arr.get(i).getCPName());
//            if(col_arr.get(i).getCPName() == col){
//                col_id = col_arr.get(i).getCPID();
//                break;
//            }
//        }
//
//        if (spnCol.getSelectedItemPosition() != 0 && spnDept.getSelectedItemPosition() != 0){
//            for(int z=0; z<mAdapter.mdisbursements.size(); z++){
//                if(mAdapter.mdisbursements.get(z).getDeptID() == dept && mAdapter.mdisbursements.get(z).getCPID() == col_id){
//                    JSONDisbursement temp = mAdapter.mdisbursements.get(z);
//                    fil_dis.add(temp);
//                }
//            }
//        }
//        else{
//            for(int z=0; z<mAdapter.mdisbursements.size(); z++){
//                if(mAdapter.mdisbursements.get(z).getDeptID() == dept || mAdapter.mdisbursements.get(z).getCPID() == col_id){
//                    JSONDisbursement temp = mAdapter.mdisbursements.get(z);
//                    fil_dis.add(temp);
//                }
//            }
//        }
//
//        mAdapter.mdisbursements = fil_dis;
//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                android.support.v4.app.Fragment frag = new ClerkDisListDetail();
//                Bundle bundle = new Bundle();
//                JSONDisbursement temp = mAdapter.mdisbursements.get(position);
//                bundle.putSerializable("disbursement", temp);
//                frag.setArguments(bundle);
//                Log.i("Reached into method", "Hello");
//                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
//                        .commit();
//            }
//        });
//    }
//
//    @Override
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        setHasOptionsMenu(true);
//
//        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
//        mDisbursement = new ArrayList<>();
//        for(JSONDisbursement c: mAdapter.mdisbursements){
//            mDisbursement.add(c);
//        }
//        mAdapter = new DisListGridAdapter("Store");
//        mRecyclerView.setAdapter(mAdapter);
//        Log.i("Size of created list", String.valueOf(mDisbursement.size()));
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
//    }
//
//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(ClerkDisList.this.getActivity(), "Selected: " + position, Toast.LENGTH_SHORT).show();
//        if(spnCol.getSelectedItemPosition() > 0 || spnDept.getSelectedItemPosition() > 0 ){
//            FilterDisbursements(spnCol.getSelectedItem().toString(), spnDept.getSelectedItem().toString());
//        }
//        else if(spnCol.getSelectedItemPosition() == 0 && spnDept.getSelectedItemPosition() == 0 ){
//            ShowAllDisbursements();
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        this.getActivity().getMenuInflater().inflate(R.menu.fragment_dis_list_menu, menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if(id == R.id.action_details){
//            android.support.v4.app.Fragment frag = new ClerkDisListSearch();
//            getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Dis").commit();
//        }
//        return super.onOptionsItemSelected(item);
//    }
//}

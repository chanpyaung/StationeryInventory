package team5.ad.sa40.stationeryinventory.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequisitionListFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{


    private String User;
    private String empName = "";
    public static String[] filters = {"View All","Pending", "Approved", "Processed", "Collected", "Rejected", "Cancelled"};
    public static String[] filters2 = {"View All", "Approved", "Processed", "Collected"};
    public static String[] priority = {"ViewAll","High", "Low"};
    private List<JSONRequisition> allRequisitions;
    private RecyclerView mRecyclerView;
    private RequisitionListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.spinnerRet) Spinner spinnerRetStatus;
    @Bind(R.id.req_search) SearchView requisitionSearch;
    @Bind(R.id.spinnerStatus) Spinner spinnerStatus;
    @Bind(R.id.filter) TextView filterBy;
    @Bind(R.id.swipeRefreshLayout) SwipeRefreshLayout mSwipeRefreshLayout;
    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
    public RequisitionListFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            User = getArguments().getString("User");
            Log.i("User value", User);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = new Bundle();
        inflater = getActivity().getLayoutInflater();
        getActivity().setTitle("Requisition");
        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        View view = inflater.inflate(R.layout.fragment_retrieval_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        System.out.println(filters.length);
        adapter = new RequisitionListAdapter();
        showAllRequisition();
        allRequisitions = Setup.allRequisition;
        adapter.SetOnItemClickListener(new RequisitionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // retrieve requisition details from server and set to adapter here
                Log.i("Req Click", "Reached");
                final JSONRequisition selected = RequisitionListAdapter.mRequisitions.get(position);
                RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
                reqAPI.getReqDetail(selected.getReqID(), new Callback<List<JSONReqDetail>>() {
                    @Override
                    public void success(List<JSONReqDetail> jsonReqDetails, Response response) {
                        RequisitionFormAdapter.mRequisitionDetails = jsonReqDetails;
                        final Bundle args = new Bundle();
                        args.putString("Date", selected.getDate());
                        args.putInt("ReqID", selected.getReqID());
                        args.putInt("StatusID", selected.getStatusID());
                        args.putInt("EmpID", selected.getEmpID());
                        String p = "";
                        if(selected.getPriorityID().equals(1)){
                            p = "Low";
                            System.out.println(p +" "+selected.getPriorityID());
                            args.putString("Priority", p);
                        }
                        else if (selected.getPriorityID().equals(2)) {
                            p = "High";
                            System.out.println(p +" "+selected.getPriorityID());
                            args.putString("Priority", p);
                        }
                        if (Setup.user.getRoleID().equals("DD") || Setup.user.getRoleID().equals("DH")) {
                            args.putString("APPROVAL", "ENABLED");
                        }
                        RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                        reqDetailFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, reqDetailFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
            }
        });
        if(Setup.user.getRoleID().equals("EM") || Setup.user.getRoleID().equals("DR") || Setup.user.getRoleID().equals("DH") || Setup.user.getRoleID().equals("DD")){
            ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,filters);
            FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinnerRetStatus.setAdapter(FiltersAdapter);
            spinnerRetStatus.getLayoutParams().width += 200;
            requisitionSearch.setVisibility(View.VISIBLE);
            requisitionSearch.setQueryHint("Search Requisition ID");
            requisitionSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    final List<JSONRequisition> filteredModelList = filter(allRequisitions, newText);
                    adapter.animateTo(filteredModelList);
                    mRecyclerView.scrollToPosition(0);
                    return true;
                }
            });

            spinnerRetStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("spinner's :", filters[position]);
                    switch (position) {
                        case (0):
                            showAllRequisition();
                            break;
                        case (1):
                            showPendingRequisition();
                            break;
                        case (2):
                            showApprovedRequisition();
                            break;
                        case (3):
                            showProcessedRequistion();
                            break;
                        case (4):
                            showCollectedRequisition();
                            break;
                        case (5):
                            showRejectedRequisition();
                            break;
                        case (6):
                            showCancelledRequisition();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    showAllRequisition();
                }
            });

            adapter.SetOnItemClickListener(new RequisitionListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    // retrieve requisition details from server and set to adapter here
                    final JSONRequisition selected = RequisitionListAdapter.mRequisitions.get(position);
                    RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
                    reqAPI.getReqDetail(selected.getReqID(), new Callback<List<JSONReqDetail>>() {
                        @Override
                        public void success(List<JSONReqDetail> jsonReqDetails, Response response) {
                            RequisitionFormAdapter.mRequisitionDetails = jsonReqDetails;
                            final Bundle args = new Bundle();
                            args.putString("Date", selected.getDate());
                            args.putInt("ReqID", selected.getReqID());
                            args.putInt("StatusID", selected.getStatusID());
                            args.putInt("EmpID", selected.getEmpID());
                            String p = "";
                            if(selected.getPriorityID().equals(1)){
                                p = "Low";
                                System.out.println(p +" "+selected.getPriorityID());
                                args.putString("Priority", p);
                            }
                            else if (selected.getPriorityID().equals(2)) {
                                p = "High";
                                System.out.println(p +" "+selected.getPriorityID());
                                args.putString("Priority", p);
                            }
                            if (Setup.user.getRoleID().equals("DD") || Setup.user.getRoleID().equals("DH")) {
                                args.putString("APPROVAL", "ENABLED");
                            }
                            RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                            reqDetailFrag.setArguments(args);
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, reqDetailFrag).commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {

                        }
                    });
                }
            });
        }
        else if (Setup.user.getRoleID().equals("SC")){
            filterBy.setVisibility(View.GONE);
            ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,filters2);
            FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinnerRetStatus.setAdapter(FiltersAdapter);
            spinnerRetStatus.getLayoutParams().width = 360;
            spinnerStatus.setVisibility(View.VISIBLE);
            spinnerStatus.getLayoutParams().width = 360;

            ArrayAdapter<String> PriorityAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,priority);
            PriorityAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            spinnerStatus.setAdapter(PriorityAdapter);


            spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    switch (position) {
                        case (0):
                            showAllRequisition();
                            break;
                        case (1):
                            showHighPriorityRequisition();
                            break;
                        case (2):
                            showLowPriorityRequisition();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            spinnerRetStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.i("spinner's :", filters[position]);
                    switch (position) {
                        case (0):
                            showAllRequisition();
                            break;
                        case (1):
                            showApprovedRequisition();
                            break;
                        case (2):
                            showProcessedRequistion();
                            break;
                        case (3):
                            showCollectedRequisition();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    showAllRequisition();
                }
            });

            adapter.SetOnItemClickListener(new RequisitionListAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    Log.i("Click", "Here");
                    // retrieve requisition details from server and set to adapter here
                    final JSONRequisition selected = RequisitionListAdapter.mRequisitions.get(position);
                    RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
                    reqAPI.getReqDetail(selected.getReqID(), new Callback<List<JSONReqDetail>>() {
                        @Override
                        public void success(List<JSONReqDetail> jsonReqDetails, Response response) {
                            Log.i("Success", "success");
                            Log.i("priorityID", selected.getPriorityID().toString());
                            System.out.println(" "+selected.getPriorityID());
                            RequisitionFormAdapter.mRequisitionDetails = jsonReqDetails;
                            final Bundle args = new Bundle();
                            args.putString("Date", selected.getDate());
                            args.putInt("ReqID", selected.getReqID());
                            args.putInt("StatusID", selected.getStatusID());
                            args.putInt("EmpID", selected.getEmpID());
                            String p = "";
                            if(selected.getPriorityID().equals(1)){
                                p = "Low";
                                System.out.println(p +" "+selected.getPriorityID());
                                args.putString("Priority", p);
                            }
                            else if (selected.getPriorityID().equals(2)) {
                                p = "High";
                                System.out.println(p +" "+selected.getPriorityID());
                                args.putString("Priority", p);
                            }
                            if (Setup.user.getRoleID().equals("DD") || Setup.user.getRoleID().equals("DH")) {
                                args.putString("APPROVAL", "ENABLED");
                            }
                            RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                            reqDetailFrag.setArguments(args);
                            android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.frame, reqDetailFrag).commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("Fail get Detail", error.toString());
                        }
                    });
                }
            });

        }

        //PullToRefresh
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(Setup.MODE == 1){
                    //user requisition page
                    if (Setup.user.getRoleID().equals("SC") || Setup.user.getRoleID().equals("SS") || Setup.user.getRoleID().equals("SM")) {
                        reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                            @Override
                            public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                                for (JSONRequisition jsonReq : jsonRequisitions) {
                                    if (jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(3) || jsonReq.getStatusID().equals(4)) {
                                        reqList.add(jsonReq);
                                    }
                                }
                                Log.i("URL", response.getUrl());
                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                Log.i("REASON", response.getReason());
                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                RequisitionListAdapter.mRequisitions = reqList;
                                Setup.allRequisition = reqList;
                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, reqListFrag).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("GetRequisitionFail", error.toString());
                            }
                        });
                    }
                    //load requisitionlist by EmpID
                    else {
                        reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                            @Override
                            public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                Log.i("URL", response.getUrl());
                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                Log.i("REASON", response.getReason());
                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                Setup.allRequisition = jsonRequisitions;
                                RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, reqListFrag).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("GetRequisitionFail", error.toString());
                            }
                        });
                    }
                }
                else if (Setup.MODE == 2){
                    //approval
                    reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                        @Override
                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {

                            Log.i("URL", response.getUrl());
                            Log.i("STATUS", String.valueOf(response.getStatus()));
                            Log.i("REASON", response.getReason());
                            Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                            List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                            //inside all requisition filter requisition by Department & PENDING Status
                            for(JSONRequisition jsonReq : jsonRequisitions){
                                if(jsonReq.getDeptID()!=null) {
                                    if (jsonReq.getDeptID().toString().equals(Setup.user.getDeptID().toString())) {
                                        Log.i("Here what", String.valueOf(jsonReq.getReqID()));
                                        if (jsonReq.getStatusID().equals(1) || jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(5)) {
                                            reqList.add(jsonReq);
                                            Log.i("what here", String.valueOf(jsonReq.getReqID()));
                                        }

                                    }
                                }
                            }
                            Log.i("SIZE OFFFFF reqList", String.valueOf(reqList.size()));
                            RequisitionListAdapter.mRequisitions = reqList;
                            Setup.allRequisition = reqList;
                            //RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                            RequisitionListFragment reqListFrag = new RequisitionListFragment();
                            FragmentTransaction fragmentTran = getFragmentManager().beginTransaction();
                            fragmentTran.replace(R.id.frame, reqListFrag).commit();
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("GetRequisitionFail", error.toString()+ " " + error.getUrl());
                        }
                    });
                }

                onItemsLoadComplete();
            }
        });

        return view;
    }

    void onItemsLoadComplete() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void showAllRequisition() {
        allRequisitions = new ArrayList<JSONRequisition>();
        allRequisitions = Setup.allRequisition;
        adapter.mRequisitions = Setup.allRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showHighPriorityRequisition(){
        List<JSONRequisition> highPriorityRequisition = new ArrayList<JSONRequisition>();
        for(JSONRequisition jrq : allRequisitions){
            if(jrq.getPriorityID() == 1){
                highPriorityRequisition.add(jrq);
            }
        }
        adapter.mRequisitions = highPriorityRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showLowPriorityRequisition(){
        List<JSONRequisition> highPriorityRequisition = new ArrayList<JSONRequisition>();
        for(JSONRequisition jrq : allRequisitions){
            if(jrq.getPriorityID() == 2){
                highPriorityRequisition.add(jrq);
            }
        }
        adapter.mRequisitions = highPriorityRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showPendingRequisition() {
        List<JSONRequisition> approvedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==1) {
                approvedRequisition.add(r);
            }
        }
        adapter.mRequisitions = approvedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showApprovedRequisition() {
        List<JSONRequisition> approvedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==2) {
                approvedRequisition.add(r);
            }
        }
        adapter.mRequisitions = approvedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showProcessedRequistion() {
        List<JSONRequisition> processedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==3) {
                processedRequisition.add(r);
            }
        }
        adapter.mRequisitions = processedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showCollectedRequisition(){
        List<JSONRequisition> collectedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==4) {
                collectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = collectedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showRejectedRequisition(){
        List<JSONRequisition> rejectedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++){
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==5){
                rejectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = rejectedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showCancelledRequisition(){
        List<JSONRequisition> collectedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==6) {
                collectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = collectedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    private List<JSONRequisition> filter(List<JSONRequisition> requests, String query) {
        query = query.toLowerCase();

        final List<JSONRequisition> filteredRequestList = new ArrayList<>();
        for (JSONRequisition req : requests) {
            final String reqID = new StringBuilder().append(req.getReqID()).toString();
            if (reqID.contains(query)) {
                filteredRequestList.add(req);
            }
        }
        return filteredRequestList;
    }

    public void getEmpName(int id){
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        EmployeeAPI empAPI = restAdapter.create(EmployeeAPI.class);
        empAPI.getEmployeeById(id, new Callback<JSONEmployee>() {
            String name;
            @Override
            public void success(JSONEmployee jsonItem, Response response) {
                Log.i("Result :", jsonItem.toString());
                Log.i("First Person: ", jsonItem.getEmpName() + " " + jsonItem.getEmpID());
                Log.i("Response: ", response.getBody().toString());
                System.out.println("Response Status " + response.getStatus());
                String eName = jsonItem.getEmpName();
                empName = jsonItem.getEmpName();
                name = eName;
                empName = name;
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Error: ", error.toString());
            }
        });

    }

    @Override
    public void doBack() {
        if(Setup.MODE == 1){
            //user requisition page
            if (Setup.user.getRoleID().equals("SC")) {
                reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                    @Override
                    public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                        List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                        for (JSONRequisition jsonReq : jsonRequisitions) {
                            if (jsonReq.getStatusID().equals(2)) {
                                reqList.add(jsonReq);
                            }
                        }
                        Log.i("URL", response.getUrl());
                        Log.i("STATUS", String.valueOf(response.getStatus()));
                        Log.i("REASON", response.getReason());
                        Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                        RequisitionListAdapter.mRequisitions = reqList;
                        Setup.allRequisition = reqList;
                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                        getFragmentManager().beginTransaction().replace(R.id.frame, reqListFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("GetRequisitionFail", error.toString());
                    }
                });
            }
            //load requisitionlist by EmpID
            else {
                reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                    @Override
                    public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                        Log.i("URL", response.getUrl());
                        Log.i("STATUS", String.valueOf(response.getStatus()));
                        Log.i("REASON", response.getReason());
                        Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                        RequisitionListAdapter.mRequisitions = jsonRequisitions;
                        Setup.allRequisition = jsonRequisitions;
                        RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                        getFragmentManager().beginTransaction().replace(R.id.frame, reqListFrag).commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("GetRequisitionFail", error.toString());
                    }
                });
            }
        }
        else if (Setup.MODE == 2){
            //approval
            reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                @Override
                public void success(List<JSONRequisition> jsonRequisitions, Response response) {

                    Log.i("URL", response.getUrl());
                    Log.i("STATUS", String.valueOf(response.getStatus()));
                    Log.i("REASON", response.getReason());
                    Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                    List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                    //inside all requisition filter requisition by Department & PENDING Status
                    for(JSONRequisition jsonReq : jsonRequisitions){
                        if(jsonReq.getDeptID()!=null) {
                            if (jsonReq.getDeptID().toString().equals(Setup.user.getDeptID().toString())) {
                                Log.i("Here what", String.valueOf(jsonReq.getReqID()));
                                if (jsonReq.getStatusID().equals(1) || jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(5)) {
                                    reqList.add(jsonReq);
                                    Log.i("what here", String.valueOf(jsonReq.getReqID()));
                                }

                            }
                        }
                    }
                    Log.i("SIZE OFFFFF reqList", String.valueOf(reqList.size()));
                    RequisitionListAdapter.mRequisitions = reqList;
                    Setup.allRequisition = reqList;
                    //RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                    RequisitionListFragment reqListFrag = new RequisitionListFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frame, reqListFrag).commit();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("GetRequisitionFail", error.toString()+ " " + error.getUrl());
                }
            });
        }
    }
}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONReqDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequisitionListFragment extends android.support.v4.app.Fragment {


    private String User;
    public static String[] filters = {"View All","Pending Approval", "Approved", "Processed", "Collected", "Rejected", "Cancelled"};
    private List<JSONRequisition> allRequisitions;
    private RecyclerView mRecyclerView;
    private RequisitionListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.spinnerRet) Spinner spinnerRetStatus;
    @Bind(R.id.req_search) SearchView requisitionSearch;
    @Bind(R.id.spinnerStatus) Spinner spinnerStatus;
    @Bind(R.id.filter) TextView filterBy;
    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
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
        View view = inflater.inflate(R.layout.fragment_retrieval_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        System.out.println(filters.length);
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,filters);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRetStatus.setAdapter(FiltersAdapter);
        showAllRequisition();
        if(Setup.user.getRoleID().equals("EM") || Setup.user.getRoleID().equals("DR") || Setup.user.getRoleID().equals("DH") || Setup.user.getRoleID().equals("DD")){
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
        }
        else if (Setup.user.getRoleID().equals("SC")){
            filterBy.setVisibility(View.GONE);

            spinnerRetStatus.getLayoutParams().width = 340;
            spinnerStatus.getLayoutParams().width = 340;
            spinnerStatus.setVisibility(View.VISIBLE);
            spinnerStatus.setAdapter(FiltersAdapter);
        }
        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case (0):
                        showAllRequisition();
                        break;
                    case (1):
                        showApprovedRequisition();
                        break;
                    case (2):
                        showRejectedRequisition();
                        break;
                    case (3):
                        showProcessedRequistion();
                        break;
                    case (4):
                        showCollectedRequisition();
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
                Toast.makeText(RequisitionListFragment.this.getActivity(), "Selected: " + position, Toast.LENGTH_SHORT).show();
                Log.i("spinner's :", filters[position]);
                switch (position) {
                    case (0):
                        showAllRequisition();
                        break;
                    case (1):
                        showApprovedRequisition();
                        break;
                    case (2):
                        showRejectedRequisition();
                        break;
                    case (3):
                        showProcessedRequistion();
                        break;
                    case (4):
                        showCollectedRequisition();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                showAllRequisition();
            }
        });

        return view;
    }

    public void showAllRequisition() {
        adapter = new RequisitionListAdapter();
        allRequisitions = new ArrayList<JSONRequisition>();
        allRequisitions = adapter.mRequisitions;
        mRecyclerView.setAdapter(adapter);
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
                        Bundle args = new Bundle();
                        args.putString("Date", selected.getDate());
                        args.putInt("ReqID", selected.getReqID());
                        args.putInt("StatusID", selected.getStatusID());
                        if(Setup.user.getRoleID().equals("DD") && Setup.user.getRoleID().equals("DH")){
                            args.putString("APPROVAL", "ENABLED");
                        }
//                        args.putInt("PriorityID", selected.getPriorityID());
//                        args.putString("PRemark", selected.getPRemark());
//                        args.putString("Remark", selected.getRemark());
                        RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                        reqDetailFrag.setArguments(args);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, reqDetailFrag).addToBackStack("REQUESITION_LIST").commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
                /*DELETE START HERE
                JSONRequisition selected = allRequisitions.get(position);
                Bundle args = new Bundle();
                args.putInt("ReqID", selected.getReqID());
                args.putString("ReqDate", selected.getDate().toString());
                String status = "";
                String priority = "";
                if (selected.getStatusID() == 1) {
                    status = "Approved";
                } else if (selected.getStatusID() == 2) {
                    status = "Rejected";
                } else if (selected.getStatusID() == 3) {
                    status = "Processed";
                } else if (selected.getStatusID() == 4) {
                    status = "Collected";
                }

                if (selected.getPriorityID() == 1) {
                    priority = "High";
                } else if (selected.getPriorityID() == 2) {
                    priority = "Normal";
                } else if (selected.getPriorityID() == 3) {
                    priority = "Low";
                }
                //get Dept ID first, then based on DeptID > get DeptHead Name
                args.putString("approvedBy", selected.getDeptID());
                //pass store clerk empID & convert to name on next Fragment
                args.putInt("processedBy", selected.getHandledBy());
                args.putInt("StatusID", selected.getStatusID());
                args.putString("Status", status);
                args.putString("Priority", priority);
                args.putString("Remark", selected.getRemark());
                args.putString("Reason", selected.getPRemark());
                DELETE END HERE*/
            }
        });
    }

    public void showApprovedRequisition() {
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

    public void showRejectedRequisition(){
        List<JSONRequisition> rejectedRequisition = new ArrayList<JSONRequisition>();
        for(int i=0; i<allRequisitions.size(); i++){
            JSONRequisition r = allRequisitions.get(i);
            if(r.getStatusID()==2){
                rejectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = rejectedRequisition;
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
            if(r.getStatusID()==3) {
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


}

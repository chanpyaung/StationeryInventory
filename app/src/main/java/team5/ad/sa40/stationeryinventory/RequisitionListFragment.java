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
import team5.ad.sa40.stationeryinventory.Model.Requisition;

/**
 * A simple {@link Fragment} subclass.
 */
public class RequisitionListFragment extends android.support.v4.app.Fragment {


    private String User;


    private String[] filters = {"View All","Approved","Rejected", "Processed", "Collected"};
    private List<Requisition> allRequisitions;
    private RecyclerView mRecyclerView;
    private RequisitionListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    @Bind(R.id.spinnerRet) Spinner spinnerRetStatus;
    @Bind(R.id.req_search) SearchView requisitionSearch;
    @Bind(R.id.spinnerStatus) Spinner spinnerStatus;
    @Bind(R.id.filter) TextView filterBy;

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
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_retrieval_list, container, false);
        ButterKnife.bind(this, view);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.ret_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        showAllRequisition();
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,filters);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerRetStatus.setAdapter(FiltersAdapter);
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
                    final List<Requisition> filteredModelList = filter(allRequisitions, newText);
                    adapter.animateTo(filteredModelList);
                    mRecyclerView.scrollToPosition(0);
                    return true;
                }
            });
        }
        else if (User.equals("clerk")){
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
        allRequisitions = new ArrayList<Requisition>();
        allRequisitions = adapter.mRequisitions;
        mRecyclerView.setAdapter(adapter);
        adapter.SetOnItemClickListener(new RequisitionListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Requisition selected = allRequisitions.get(position);
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
                args.putString("Reason", selected.getpRemark());
                RequisitionDetailFragment reqDetailFrag = new RequisitionDetailFragment();
                reqDetailFrag.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, reqDetailFrag);
                fragmentTransaction.commit();
            }
        });
    }

    public void showApprovedRequisition() {
        List<Requisition> approvedRequisition = new ArrayList<Requisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            Requisition r = allRequisitions.get(i);
            if(r.getStatusID()==1) {
                approvedRequisition.add(r);
            }
        }
        adapter.mRequisitions = approvedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showRejectedRequisition(){
        List<Requisition> rejectedRequisition = new ArrayList<Requisition>();
        for(int i=0; i<allRequisitions.size(); i++){
            Requisition r = allRequisitions.get(i);
            if(r.getStatusID()==2){
                rejectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = rejectedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showProcessedRequistion() {
        List<Requisition> processedRequisition = new ArrayList<Requisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            Requisition r = allRequisitions.get(i);
            if(r.getStatusID()==3) {
                processedRequisition.add(r);
            }
        }
        adapter.mRequisitions = processedRequisition;
        mRecyclerView.setAdapter(adapter);
    }

    public void showCollectedRequisition(){
        List<Requisition> collectedRequisition = new ArrayList<Requisition>();
        for(int i=0; i<allRequisitions.size(); i++) {
            Requisition r = allRequisitions.get(i);
            if(r.getStatusID()==3) {
                collectedRequisition.add(r);
            }
        }
        adapter.mRequisitions = collectedRequisition;
        mRecyclerView.setAdapter(adapter);
    }


    private List<Requisition> filter(List<Requisition> requests, String query) {
        query = query.toLowerCase();

        final List<Requisition> filteredRequestList = new ArrayList<>();
        for (Requisition req : requests) {
            final String reqID = new StringBuilder().append(req.getReqID()).toString();
            if (reqID.contains(query)) {
                filteredRequestList.add(req);
            }
        }
        return filteredRequestList;
    }


}

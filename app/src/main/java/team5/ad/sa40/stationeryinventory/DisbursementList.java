package team5.ad.sa40.stationeryinventory;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;


public class DisbursementList extends android.support.v4.app.Fragment {

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListGridAdapter mAdapter;
    SearchView search;
    private List<Disbursement> mDisbursement;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    @Bind(R.id.startDate) EditText text_start_date;
    @Bind(R.id.endDate) EditText text_end_date;

    public DisbursementList(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_disbursement_list, container, false);
        ButterKnife.bind(this, view);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new DisListGridAdapter("Dept");
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(DisbursementList.this.getActivity(), "Click position at " + position, Toast.LENGTH_SHORT).show();
            }
        });
        search = (SearchView)view.findViewById(R.id.disSearchCat);

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                final List<Disbursement> filteredModelList = filter(mDisbursement, newText);
                mAdapter.animateTo(filteredModelList);
                mRecyclerView.scrollToPosition(0);
                return true;
            }
        });

        //Date dialog codes
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                text_start_date.setText(Setup.parseDateToString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this.getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                text_end_date.setText(Setup.parseDateToString(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        text_start_date.setInputType(InputType.TYPE_NULL);
        text_start_date.requestFocus();
        text_end_date.setInputType(InputType.TYPE_NULL);

        text_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromDatePickerDialog.show();
            }
        });

        text_end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toDatePickerDialog.show();
            }
        });
        //Date dialog code ends here
        return  view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mDisbursement = new ArrayList<>();
        for(Disbursement c: mAdapter.mdisbursements){
            mDisbursement.add(c);
        }
        mAdapter = new DisListGridAdapter("Dept");
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                android.support.v4.app.Fragment frag = new DisbursementListDetail();
                Bundle bundle = new Bundle();
                Disbursement temp = mDisbursement.get(position);
                bundle.putSerializable("disbursement", temp);
                frag.setArguments(bundle);
                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Dis")
                        .commit();
            }
        });
    }

    private List<Disbursement> filter(List<Disbursement> models, String query) {
        query = query.toLowerCase();

        final List<Disbursement> filteredModelList = new ArrayList<>();
        for (Disbursement model : models) {
            final String text = String.valueOf(model.getDisbursementId()).toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}

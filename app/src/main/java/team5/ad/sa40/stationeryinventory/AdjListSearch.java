package team5.ad.sa40.stationeryinventory;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjListSearch extends android.support.v4.app.Fragment {


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AdjListGridAdapter mAdapter;
    private List<Adjustment> mAdjustment;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    @Bind(R.id.txtSearch) EditText txtSearch;
    @Bind(R.id.btnSearch) Button btnSearch;
    @Bind(R.id.startDate) EditText text_start_date;
    @Bind(R.id.endDate) EditText text_end_date;
    public AdjListSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_adj_list_search, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new AdjListGridAdapter("Search");
        mRecyclerView.setAdapter(mAdapter);

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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search button action
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity().getBaseContext(), 1));
        mAdjustment = new ArrayList<>();
        for(Adjustment c: mAdapter.mAdjustments){
            mAdjustment.add(c);
        }
        mAdapter = new AdjListGridAdapter("Adj");
        mRecyclerView.setAdapter(mAdapter);
        Log.i("Size of created list", String.valueOf(mAdjustment.size()));
        mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                android.support.v4.app.Fragment frag = new AdjListDetail();
                Bundle bundle = new Bundle();
                Adjustment temp = mAdjustment.get(position);
                bundle.putSerializable("adjustment", temp);
                frag.setArguments(bundle);
                Log.i("Reached into method", "Hello");
                getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                        .commit();
            }
        });
    }

}

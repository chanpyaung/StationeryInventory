package team5.ad.sa40.stationeryinventory;


import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjListSearch extends android.support.v4.app.Fragment {


    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AdjListGridAdapter mAdapter;
    private List<JSONAdjustment> mAdjustment;
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

        JsonObject jobj = new JsonObject();
        jobj.addProperty("adjId", "null");
        jobj.addProperty("startDate", "null");
        jobj.addProperty("endDate", "null");
        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        final AdjustmentAPI adjustmentAPI = restAdapter.create(AdjustmentAPI.class);

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
                JsonObject object = new JsonObject();
                if(txtSearch.getText().toString().matches("")){
                    object.addProperty("adjId", "null");
                }
                if(text_start_date.getText().toString().matches("") || text_end_date.getText().toString().matches("")){
                    object.addProperty("startDate", "null");
                    object.addProperty("endDate", "null");
                }
                else{
                    SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                    SimpleDateFormat original_format = new SimpleDateFormat("dd/MM/yyyy");

                    Date uDate1 = null;
                    try {
                        uDate1 = original_format.parse(text_start_date.getText().toString());
                        String str_date1 = format.format(uDate1);
                        str_date1 = str_date1.replace('/', '-');
                        Log.e("SQLDate 1", str_date1);

                        Date uDate2 = original_format.parse(text_end_date.getText().toString());
                        String str_date2 = format.format(uDate2);
                        str_date2 = str_date2.replace('/', '-');
                        Log.e("SQLDate 2", str_date2);

                        object.addProperty("startDate", str_date1);
                        object.addProperty("endDate", str_date2);
                        Log.e("Search adj json", object.toString());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                adjustmentAPI.getAdjVoucher(object, new Callback<List<JSONAdjustment>>() {
                    @Override
                    public void success(List<JSONAdjustment> adjustments, Response response) {
                        mAdjustment = adjustments;
                        mAdapter = new AdjListGridAdapter("Search", mAdjustment);
                        mRecyclerView.setAdapter(mAdapter);
                        if(mAdjustment.size() > 1){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Search results")
                                    .setMessage(String.valueOf(mAdjustment.size()) + " adjustments are found!")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else if(mAdjustment.size() == 0){
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Search results")
                                    .setMessage("No adjustment is found!")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                        else{
                            new AlertDialog.Builder(getActivity())
                                    .setTitle("Search results")
                                    .setMessage(String.valueOf(mAdjustment.size()) + " adjustment is found!")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                        }
                                    })
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }

                        mAdapter.SetOnItemClickListener(new AdjListGridAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                JsonObject object = new JsonObject();
                                object.addProperty("adjId", String.valueOf(mAdjustment.get(position).getAdjID()));
                                final JSONAdjustment temp = mAdjustment.get(position);
                                adjustmentAPI.getAdjVoucherDetail(object, new Callback<List<JSONAdjustmentDetail>>() {
                                    @Override
                                    public void success(List<JSONAdjustmentDetail> jsonAdjustmentDetails, Response response) {
                                        android.support.v4.app.Fragment frag;
                                        if(temp.getStatus().equals("PENDING")){
                                            frag = new AdjListDetail2();
                                        }
                                        else{
                                            frag = new AdjListDetail();
                                        }
                                        ArrayList<JSONAdjustmentDetail> tempList = new ArrayList<JSONAdjustmentDetail>(jsonAdjustmentDetails);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("adjustment", temp);
                                        bundle.putSerializable("adjustmentDetail", tempList);
                                        for(JSONAdjustmentDetail detail:tempList){
                                            Log.e("detail id", String.valueOf(detail.getAdjustmentID()));
                                        }
                                        frag.setArguments(bundle);
                                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Detail")
                                                .commit();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.e("search trigger", error.toString());
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("getAdjVou in adjSearch", error.toString());
                    }
                });
            }
        });


        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.search_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            txtSearch.setText("");
            text_start_date.setText("");
            text_end_date.setText("");
        }
        return super.onOptionsItemSelected(item);
    }
}

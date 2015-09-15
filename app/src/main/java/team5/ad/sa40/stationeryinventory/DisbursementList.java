package team5.ad.sa40.stationeryinventory;

import android.app.DatePickerDialog;
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
import android.widget.SearchView;
import android.widget.Toast;

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
import team5.ad.sa40.stationeryinventory.API.CollectionPointAPI;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;


public class DisbursementList extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    DisListGridAdapter mAdapter;
    SearchView search;
    private List<JSONDisbursement> mDisbursement;
    private List<JSONCollectionPoint> mCollectionPoint;

    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    @Bind(R.id.startDate) EditText text_start_date;
    @Bind(R.id.endDate) EditText text_end_date;
    @Bind(R.id.button) Button btnSearch;

    public DisbursementList(){

        mDisbursement = new ArrayList<>();
        mCollectionPoint = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_disbursement_list, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        getActivity().setTitle("Disbursement List");
        ((MainActivity)getActivity()).setOnBackPressedListener(this);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);


        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
        Log.e("Adapter:", "reach into constructor");
        Log.e("Setup.user.getDeptID()", String.valueOf(Setup.user.getDeptID()));
        disbursementAPI.getDisbursementByDeptID(Setup.user.getDeptID(), new Callback<List<JSONDisbursement>>() {
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
                        mCollectionPoint = jsonCollectionPoints;
                        Log.e("Size of list", String.valueOf(mCollectionPoint.size()));
                        mAdapter = new DisListGridAdapter("Dept", mDisbursement, mCollectionPoint);
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.SetOnItemClickListener(new DisListGridAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                android.support.v4.app.Fragment frag = new DisbursementListDetail();
                                Bundle bundle = new Bundle();
                                JSONDisbursement temp = mDisbursement.get(position);
                                bundle.putSerializable("disbursement", temp);
                                frag.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
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

        search = (SearchView)view.findViewById(R.id.disSearchCat);
        search.setQueryHint("Search disbursement ID");
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.e("onQueryTextChange0", String.valueOf(mDisbursement.size()));
                DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
                disbursementAPI.getDisbursementByDeptID(Setup.user.getDeptID(), new Callback<List<JSONDisbursement>>() {
                    @Override
                    public void success(List<JSONDisbursement> jsonDisbursements, Response response) {
                        mDisbursement = jsonDisbursements;
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("getDisbursementByDeptID", error.toString());
                    }
                });
                ArrayList<JSONDisbursement> dis = new ArrayList<JSONDisbursement>();
                for(int i = 0; i < mDisbursement.size(); i++){
                    JSONDisbursement temp = mDisbursement.get(i);
                    dis.add(temp);
                }
                final List<JSONDisbursement> filteredModelList = filter(dis, newText);
                Log.e("onQueryTextChange1", String.valueOf(dis.size()));
                Log.e("onQueryTextChange2", String.valueOf(mCollectionPoint.size()));
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

//        text_end_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    text_end_date.setError(null);
//                    Date start = Setup.parseStringToDate(text_start_date.getText().toString());
//                    Date end = Setup.parseStringToDate(text_end_date.getText().toString());
//
//                    if (end.before(start)) {
//                        text_end_date.setError("End Date should be after Start Date");
//                    }
//                }
//            }
//        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!text_start_date.getText().toString().matches("") && !text_end_date.getText().toString().matches("")){
                    Date start = Setup.parseStringToDate(text_start_date.getText().toString());
                    Date end = Setup.parseStringToDate(text_end_date.getText().toString());

                    if (end.before(start)) {
                        text_end_date.setError("End Date should be after Start Date");
                    }
                    else{
                        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
                        SimpleDateFormat original_format = new SimpleDateFormat("dd/MM/yyyy");
                        try {
                            Date uDate1 = original_format.parse(text_start_date.getText().toString());
                            String str_date1 = format.format(uDate1);
                            str_date1 = str_date1.replace('/', '-');
                            Log.e("SQLDate 1", str_date1);

                            Date uDate2 = original_format.parse(text_end_date.getText().toString());
                            String str_date2 = format.format(uDate2);
                            str_date2 = str_date2.replace('/', '-');
                            Log.e("SQLDate 2", str_date2);

                            DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
                            disbursementAPI.getDisbursementByDates(Setup.user.getDeptID(), str_date1, str_date2, new Callback<List<JSONDisbursement>>() {
                                @Override
                                public void success(List<JSONDisbursement> jsonDisbursements, Response response) {
                                    mDisbursement = jsonDisbursements;
                                    Log.e("Success", String.valueOf(jsonDisbursements.size()));
                                    mAdapter.animateTo(mDisbursement);
                                    mRecyclerView.scrollToPosition(0);

                                    if (mDisbursement.size() < 1) {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Information")
                                                .setMessage("No disbursement is found!")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    } else if (mDisbursement.size() > 1) {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Information")
                                                .setMessage(mDisbursement.size() + " disbursements are found!")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    } else {
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Information")
                                                .setMessage(mDisbursement.size() + " disbursement is found!")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }

                                    text_start_date.setError(null);
                                    text_end_date.setError(null);
                                }

                                @Override
                                public void failure(RetrofitError error) {
                                    Log.e("getDisbursementByDates", error.toString());
                                }
                            });

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
                else {
                    if(text_start_date.getText().toString().matches("")){
                        text_start_date.setError("Fill Data!");
                    }
                    if (text_end_date.getText().toString().matches("")){
                        text_end_date.setError("Fill Data!");
                    }
                }
            }
        });
        //Date dialog code ends here
        return  view;
    }

    private List<JSONDisbursement> filter(List<JSONDisbursement> models, String query) {
        query = query.toLowerCase();
        Log.e("query.toLowerCase()", query);
        Log.e("filter", String.valueOf(models.size()));
        final List<JSONDisbursement> filteredModelList = new ArrayList<>();
        for (JSONDisbursement model : models) {
            final String text = String.valueOf(model.getDisID());
            Log.e("model.getDisID()", String.valueOf(model.getDisID()));
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
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
            text_start_date.setText("");
            text_end_date.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        android.support.v4.app.Fragment frag = new DisbursementList();
        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
    }
}

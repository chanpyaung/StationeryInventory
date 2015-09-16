package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;
import team5.ad.sa40.stationeryinventory.Model.AdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjListDetail extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    AdjListDetailAdapter mAdapter;
    ArrayList<JSONAdjustmentDetail> adjustmentDetails;

    @Bind(R.id.txtAdjID) TextView txtAdjID;
    @Bind(R.id.txtAdjDate) TextView txtAdjDate;
    @Bind(R.id.txtEmpName) TextView txtEmpName;
    @Bind(R.id.txtEmpID) TextView txtEmpID;
    @Bind(R.id.txtStatus) TextView txtStatus;
    @Bind(R.id.txtTotalCost) TextView txtTotalCost;
    @Bind(R.id.txtAppID) TextView txtAppID;//
    @Bind(R.id.txtAppName) TextView txtAppName;//
    @Bind(R.id.txtStatusLabel) TextView txtStatusLabel;
    @Bind(R.id.img2) ImageView app_img;//

    public AdjListDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_adj_list_detail, container, false);

        Bundle bundle = this.getArguments();
        final JSONAdjustment dis = (JSONAdjustment) bundle.getSerializable("adjustment");
        adjustmentDetails = (ArrayList<JSONAdjustmentDetail>)bundle.getSerializable("adjustmentDetail");

        mRecyclerView = (RecyclerView)v.findViewById(R.id.my_dis_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(this.getActivity().getBaseContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.e("adjustmentDetails size", String.valueOf(adjustmentDetails.size()));
        for(JSONAdjustmentDetail detail:adjustmentDetails){
            Log.e("detail id", String.valueOf(detail.getAdjustmentID()));
        }

        ButterKnife.bind(this, v);


        if(dis.getStatus().equals("REJECT")){
            txtStatusLabel.setText("Rejected By");
        }

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Adjustment List Detail");

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                Log.e("Reach into BuildTable", "Success");
                EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
                employeeAPI.getEmployeeById(dis.getReportedBy(), new Callback<JSONEmployee>() {
                    @Override
                    public void success(JSONEmployee jsonEmployee, Response response) {
                        EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
                        JSONEmployee employee = jsonEmployee;
                        txtEmpName.setText(employee.getEmpName());
                        employeeAPI.getEmployeeById(dis.getApprovedBy(), new Callback<JSONEmployee>() {
                            @Override
                            public void success(JSONEmployee jsonEmployee, Response response) {
                                JSONEmployee employee = jsonEmployee;
                                mAdapter = new AdjListDetailAdapter(adjustmentDetails);
                                mRecyclerView.setAdapter(mAdapter);
                                Log.e("adjustmentDetails size", String.valueOf(adjustmentDetails.size()));
                                txtAppID.setText(String.valueOf(dis.getApprovedBy()));
                                new InventoryDetails.DownloadImageTask(app_img).execute("http://192.168.31.202/img/user/" + dis.getApprovedBy() + ".jpg");
                                Log.i("Dis id is ", String.valueOf(dis.getAdjID()));
                                txtAdjID.setText(dis.getAdjID());
                                String string_date = Setup.parseJSONDateToString(dis.getDate());
                                txtStatus.setText(dis.getStatus());
                                txtAdjDate.setText(string_date);
                                txtEmpID.setText(String.valueOf(dis.getReportedBy()));

                                if(String.valueOf(dis.getTotalAmt()).equals("null")){
                                    txtTotalCost.setText("$ 0");
                                }
                                else{
                                    DecimalFormat df = new DecimalFormat();
                                    df.setMaximumFractionDigits(2);
                                    txtTotalCost.setText("$ " + String.valueOf(df.format(dis.getTotalAmt())));
                                }
                                txtAppName.setText(employee.getEmpName());
                                if (dis.getStatus().equals("REJECTED")) {
                                    txtStatusLabel.setText("REJECTED BY");
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("getEmployeeById Re", error.toString());
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("getEmployeeById Re", error.toString());
                    }
                });

        return v;
    }

    @Override
    public void doBack() {
        AdjVouList fragment = new AdjVouList();
        FragmentTransaction fragtran = getFragmentManager().beginTransaction();
        fragtran.replace(R.id.frame, fragment).commit();
    }
}

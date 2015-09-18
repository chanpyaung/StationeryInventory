package team5.ad.sa40.stationeryinventory.Fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.AdjustmentAPI;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustment;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * Created by student on 11/9/15.
 */
public class AdjListDetail2 extends android.support.v4.app.Fragment  implements MainActivity.OnBackPressedListener {

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
    @Bind(R.id.btnApprove) Button btnApprove;
    @Bind(R.id.btnReject) Button btnReject;


    public AdjListDetail2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_adj_list_detail2, container, false);

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

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Adjustment Detail");

        if(Setup.user.getRoleID().equals("SC") || !dis.getStatus().equals("PENDING"))
        {
            btnApprove.setVisibility(View.GONE);
            btnReject.setVisibility(View.GONE);
        }
        if(dis.getStatus().equals("PENDING")){
            txtStatus.setTextColor(Color.RED);
        }

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
        employeeAPI.getEmployeeById(dis.getReportedBy(), new Callback<JSONEmployee>() {

            @Override
            public void success(JSONEmployee jsonEmployee, Response response) {
                JSONEmployee employee = jsonEmployee;
                mAdapter = new AdjListDetailAdapter(adjustmentDetails);
                mRecyclerView.setAdapter(mAdapter);

                txtEmpName.setText(employee.getEmpName());
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
                 btnApprove.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Approve");
                        builder.setMessage("Do you want to approve?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                JsonObject object = new JsonObject();
                                object.addProperty("adjId", dis.getAdjID());
                                object.addProperty("ApprovedBy", Setup.user.getEmpID());
                                AdjustmentAPI adjustmentAPI = restAdapter.create(AdjustmentAPI.class);
                                adjustmentAPI.approveAdjVoucher(object, new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {
                                        Log.e("response", s);
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Success")
                                                .setMessage("The adjustment voucher was approved!")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        btnApprove.setVisibility(View.GONE);
                                                        btnReject.setVisibility(View.GONE);
                                                        AdjVouList fragment = new AdjVouList();
                                                        FragmentTransaction fragtran = getFragmentManager().beginTransaction();
                                                        fragtran.replace(R.id.frame, fragment).commit();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.e("approveAdjVoucher", error.toString());
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Fail")
                                                .setMessage("Cannot approve the adjustment!")
                                                .setCancelable(false)
                                                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                btnReject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("Reject");
                        builder.setMessage("Do you want to reject?");
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                JsonObject object = new JsonObject();
                                object.addProperty("adjId", dis.getAdjID());
                                object.addProperty("ApprovedBy", Setup.user.getEmpID());
                                AdjustmentAPI adjustmentAPI = restAdapter.create(AdjustmentAPI.class);
                                adjustmentAPI.rejectAdjVoucher(object, new Callback<String>() {
                                    @Override
                                    public void success(String s, Response response) {
                                        Log.e("response", s);
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Success")
                                                .setMessage("The adjustment voucher was rejected!")
                                                .setCancelable(false)
                                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        btnApprove.setVisibility(View.GONE);
                                                        btnReject.setVisibility(View.GONE);
                                                        AdjVouList fragment = new AdjVouList();
                                                        FragmentTransaction fragtran = getFragmentManager().beginTransaction();
                                                        fragtran.replace(R.id.frame, fragment).commit();
                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        Log.e("approveAdjVoucher", error.toString());
                                        new AlertDialog.Builder(getActivity())
                                                .setTitle("Fail")
                                                .setMessage("Cannot reject the adjustment!")
                                                .setCancelable(false)
                                                .setPositiveButton("TRY AGAIN", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                })
                                                .setIcon(android.R.drawable.ic_dialog_alert)
                                                .show();
                                    }
                                });
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO
                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
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

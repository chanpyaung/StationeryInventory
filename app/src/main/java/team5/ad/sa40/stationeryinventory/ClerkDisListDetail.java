package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.DepartmentAPI;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.Employee;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDepartment;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClerkDisListDetail extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{


    MapView mapView;
    GoogleMap map;
    JSONDisbursement dis;
    JSONEmployee rep;
    JSONCollectionPoint selected_colPt;

    @Bind(R.id.txtNo) TextView text_dis_no;
    @Bind(R.id.txtDisDate) TextView text_dis_date;
    @Bind(R.id.txtColPt) TextView text_col_pt;
    @Bind(R.id.txtDept) TextView text_dept;
    @Bind(R.id.imgPhone) ImageView img_phCall;
    @Bind(R.id.txtRepID) TextView txtRepID;
    @Bind(R.id.txtRepName) TextView txtRepName;
    @Bind(R.id.img2) ImageView rep_img;
    @Bind(R.id.txtRepLabel)TextView txtRepLabel;

    public ClerkDisListDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_clerk_dis_list_detail, container, false);
        ButterKnife.bind(this, v);

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Disbursement Detail");

        Bundle bundle = this.getArguments();
        dis = (JSONDisbursement) bundle.getSerializable("disbursement");
        Log.i("Dis id is ", String.valueOf(dis.getDisID()));
        selected_colPt = (JSONCollectionPoint) bundle.getSerializable("collection");

        if(!dis.getStatus().equals("DISBURSED")){
            setHasOptionsMenu(true);
        }
        txtRepLabel.setText("Received By");

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);
        map.addMarker(new MarkerOptions()
                .position(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()))
                .title(selected_colPt.getCPName()));

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()), 15);
        map.animateCamera(cameraUpdate);

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        DepartmentAPI departmentAPI = restAdapter.create(DepartmentAPI.class);
        departmentAPI.getDepartmentByDeptID(dis.getDeptID(), new Callback<JSONDepartment>() {
            @Override
            public void success(JSONDepartment jsonDepartment, Response response) {
                JSONDepartment sel_dept = jsonDepartment;

                EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);

                if(!dis.getStatus().equals("DISBURSED")){
                    Log.e("Reach in Pending", "pending");
                    employeeAPI.getEmployeeById(sel_dept.getDeptRep(), new Callback<JSONEmployee>() {
                        @Override
                        public void success(JSONEmployee jsonEmployee, Response response) {
                            rep = jsonEmployee;

                            text_dis_no.setText(String.valueOf(dis.getDisID()));
                            String string_date = Setup.parseJSONDateToString(dis.getDate());
                            text_dis_date.setText(string_date);
                            text_col_pt.setText(selected_colPt.getCPName());
                            text_dept.setText(dis.getDeptID());
                            txtRepID.setText(String.valueOf(rep.getEmpID()));
                            txtRepName.setText(rep.getEmpName());

                            img_phCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri uri = Uri.parse("tel:" + String.valueOf(rep.getPhone()));
                                    Intent i = new Intent(Intent.ACTION_CALL, uri);
                                    startActivity(i);
                                }
                            });
                            new InventoryDetails.DownloadImageTask(rep_img).execute("http://192.168.31.202/img/user/" + rep.getEmpID() + ".jpg");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("getEmployeeById", error.toString());
                        }
                    });
                }
                else{
                    Log.e("Reach in Disbursed", "disbursed");
                    employeeAPI.getEmployeeById(dis.getReceivedBy(), new Callback<JSONEmployee>() {
                        @Override
                        public void success(JSONEmployee jsonEmployee, Response response) {
                            rep = jsonEmployee;

                            text_dis_no.setText(String.valueOf(dis.getDisID()));
                            String string_date = Setup.parseJSONDateToString(dis.getDate());
                            text_dis_date.setText(string_date);
                            text_col_pt.setText(selected_colPt.getCPName());
                            text_dept.setText(dis.getDeptID());
                            txtRepID.setText(String.valueOf(rep.getEmpID()));
                            txtRepName.setText(rep.getEmpName());

                            img_phCall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Uri uri = Uri.parse("tel:" + String.valueOf(rep.getPhone()));
                                    Intent i = new Intent(Intent.ACTION_CALL, uri);
                                    startActivity(i);
                                }
                            });
                            new InventoryDetails.DownloadImageTask(rep_img).execute("http://192.168.31.202/img/user/" + rep.getEmpID() + ".jpg");
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e("getEmployeeById", error.toString());
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getDepartmentByDeptID", error.toString());
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.clerk_dis_list_detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            Bundle bundle = new Bundle();
            android.support.v4.app.Fragment frag = new SignatureFragment();
            bundle.putSerializable("disbursement", dis);
            bundle.putInt("RepID", rep.getEmpID());
            bundle.putSerializable("collection", selected_colPt);
            frag.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        android.support.v4.app.Fragment frag = new ClerkDisList();
        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
    }
}

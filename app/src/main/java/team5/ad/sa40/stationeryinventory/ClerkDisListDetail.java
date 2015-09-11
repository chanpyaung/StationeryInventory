package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
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
public class ClerkDisListDetail extends android.support.v4.app.Fragment {


    MapView mapView;
    GoogleMap map;

    @Bind(R.id.txtNo) TextView text_dis_no;
    @Bind(R.id.txtDisDate) TextView text_dis_date;
    @Bind(R.id.txtColPt) TextView text_col_pt;
    @Bind(R.id.txtDept) TextView text_dept;
    @Bind(R.id.imgPhone) ImageView img_phCall;
    @Bind(R.id.txtRepID) TextView txtRepID;
    @Bind(R.id.txtRepName) TextView txtRepName;
    @Bind(R.id.img2) ImageView rep_img;

    public ClerkDisListDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_clerk_dis_list_detail, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);

        Bundle bundle = this.getArguments();
        final JSONDisbursement dis = (JSONDisbursement) bundle.getSerializable("disbursement");
        Log.i("Dis id is ", String.valueOf(dis.getDisID()));
        final JSONCollectionPoint selected_colPt;
        selected_colPt = (JSONCollectionPoint) bundle.getSerializable("collection");

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()), 10);
        map.animateCamera(cameraUpdate);

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        DepartmentAPI departmentAPI = restAdapter.create(DepartmentAPI.class);
        departmentAPI.getDepartmentByDeptID(dis.getDeptID(), new Callback<JSONDepartment>() {
            @Override
            public void success(JSONDepartment jsonDepartment, Response response) {
                JSONDepartment sel_dept = jsonDepartment;

                EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
                employeeAPI.getEmployeeById(sel_dept.getDeptRep(), new Callback<JSONEmployee>() {
                    @Override
                    public void success(JSONEmployee jsonEmployee, Response response) {
                        final JSONEmployee rep = jsonEmployee;

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
            Toast.makeText(ClerkDisListDetail.this.getActivity(), "Detail selected!", Toast.LENGTH_SHORT);
            text_col_pt.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

}

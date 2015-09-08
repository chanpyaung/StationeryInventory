package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Department;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentInfoFragment extends android.support.v4.app.Fragment {

    @Bind(R.id.contactName) EditText contactName;
    @Bind(R.id.telephone) EditText telephone;
    @Bind(R.id.fax) EditText fax;
    @Bind(R.id.deptHead) EditText deptHead;
    @Bind(R.id.spinnerRep) Spinner representative;
    @Bind(R.id.spinnerCPoint) Spinner collectionPoint;
    @Bind(R.id.mapview) MapView cpMap;
    GoogleMap map;
    Department dept;

    public DepartmentInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_department_info, container, false);
        ButterKnife.bind(this,view);

        new AsyncTask<Void, Void, Department>(){
            @Override
            protected Department doInBackground(Void... params) {
                return Department.getDeptByID("ENGL");
            }
            @Override
            protected void onPostExecute(Department result) {
                setValues(result);
            }
        }.execute();

        CollectionPoint selected_colPt = new CollectionPoint();
        selected_colPt.setColPt_lat(1.295675F);
        selected_colPt.setColPt_long(103.781525F);

        cpMap.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = cpMap.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        try {
            MapsInitializer.initialize(this.getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getColPt_lat(), selected_colPt.getColPt_long()), 10);
        map.animateCamera(cameraUpdate);
        return  view;
    }

    public void setValues(Department _dept){
        dept = _dept;
        if(dept != null) {
            Log.i("Department ID: ", dept.getDeptID());
            contactName.setText(String.valueOf(dept.getContact()));
            telephone.setText(String.valueOf(dept.getPhone()));
            fax.setText(String.valueOf(dept.getFax()));
            deptHead.setText(String.valueOf(dept.getDeptHead()));
        }
    }
}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
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
    @Bind(R.id.mapview) MapView cpMap;
    GoogleMap map;

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
        Bundle bundle = this.getArguments();
        Disbursement dis = (Disbursement) bundle.getSerializable("disbursement");
        ArrayList<CollectionPoint> col_pts = CollectionPoint.getAllCollectionPoints();
        CollectionPoint selected_colPt = new CollectionPoint();

        for (int i = 0; i< col_pts.size(); i++){
            CollectionPoint col = col_pts.get(i);
            if(dis.getDisbursement_colID() == col.getColPt_id()){
                selected_colPt = col;
                break;
            }
        }
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

}

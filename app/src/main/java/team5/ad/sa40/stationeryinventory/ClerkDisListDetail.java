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
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;


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
        Disbursement dis = (Disbursement) bundle.getSerializable("disbursement");
        Log.i("Dis id is ", String.valueOf(dis.getDisbursementId()));
        ArrayList<CollectionPoint> col_pts = CollectionPoint.getAllCollectionPoints();
        CollectionPoint selected_colPt = new CollectionPoint();

        for (int i = 0; i< col_pts.size(); i++){
            CollectionPoint col = col_pts.get(i);
            if(dis.getDisbursement_colID() == col.getColPt_id()){
                selected_colPt = col;
                break;
            }
        }

        text_dis_no.setText(String.valueOf(dis.getDisbursementId()));
        String string_date = Setup.parseDateToString(dis.getDisbursementDate());
        text_dis_date.setText(string_date);
        text_col_pt.setText(selected_colPt.getColPt_name());
        text_dept.setText(dis.getDeptID());

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

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
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getColPt_lat(), selected_colPt.getColPt_long()), 10);
        map.animateCamera(cameraUpdate);


        img_phCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:94725311");
                Intent i = new Intent(Intent.ACTION_CALL, uri);
                startActivity(i);
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

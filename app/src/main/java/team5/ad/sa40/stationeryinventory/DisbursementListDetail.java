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
import android.widget.Button;
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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.CollectionPointAPI;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisbursementListDetail extends android.support.v4.app.Fragment {


    MapView mapView;
    GoogleMap map;
    List<JSONCollectionPoint> col_pts;
    JSONCollectionPoint selected_colPt;

    @Bind(R.id.txtNo) TextView text_dis_no;
    @Bind(R.id.txtDisDate) TextView text_dis_date;
    @Bind(R.id.txtColPt) TextView text_col_pt;
    @Bind(R.id.txtStatus) TextView text_status;
    //@Bind(R.id.imgPhone) ImageView img_phCall;
    @Bind(R.id.btnView) Button btn_view;


    public DisbursementListDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_disbursement_list_detail, container, false);
        setHasOptionsMenu(true);
        ButterKnife.bind(this, v);
        Bundle bundle = this.getArguments();
        final JSONDisbursement dis = (JSONDisbursement) bundle.getSerializable("disbursement");
        Log.i("Dis id is ", String.valueOf(dis.getDisID()));

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

        final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        CollectionPointAPI collectionPointAPI = restAdapter.create(CollectionPointAPI.class);

        collectionPointAPI.getAllCollectionPoints(new Callback<List<JSONCollectionPoint>>() {
            @Override
            public void success(List<JSONCollectionPoint> jsonCollectionPoints, Response response) {
                col_pts = jsonCollectionPoints;
                Log.e("Size of list", String.valueOf(col_pts.size()));

                for(int i = 0; i < col_pts.size(); i++){
                    if(dis.getCPID() == col_pts.get(i).getCPID()){
                        selected_colPt = col_pts.get(i);
                    }
                }

                text_dis_no.setText(String.valueOf(dis.getDisID()));
                String string_date = Setup.parseJSONDateToString(dis.getDate());
                text_dis_date.setText(string_date);
                text_col_pt.setText(selected_colPt.getCPName());
                text_status.setText(dis.getStatus());

                // Updates the location and zoom of the MapView
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()), 100);
                map.animateCamera(cameraUpdate);

//
//                img_phCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Uri uri = Uri.parse("tel:94725311");
//                        Intent i = new Intent(Intent.ACTION_CALL, uri);
//                        startActivity(i);
//                    }
//                });

                btn_view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        android.support.v4.app.Fragment frag = new DisListDetailItemList();
                        Bundle bundle = new Bundle();
                        ArrayList<Item> items = getAllItemList();
                        bundle.putSerializable("items", items);
                        frag.setArguments(bundle);
                        getFragmentManager().beginTransaction().replace(R.id.frame, frag).addToBackStack("Dis")
                                .commit();
                    }
                });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getAllCollectionPoints", error.toString());
            }
        });

        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_dis_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_details){
            Toast.makeText(DisbursementListDetail.this.getActivity(), "Detail selected!", Toast.LENGTH_SHORT);
            text_status.setText("");
        }
        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Item> getAllItemList(){

        ArrayList<Item> items = new ArrayList<>();

        for(int i=1; i < 11; i++){
            Item item = new Item("I00"+ i, "Item " + i, i, i, i, "UOM", i, "Bin " + i);
            items.add(item);
        }
        return items;
    }
}

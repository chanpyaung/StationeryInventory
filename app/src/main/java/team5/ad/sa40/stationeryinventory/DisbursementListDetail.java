package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisbursementListDetail extends android.support.v4.app.Fragment {


    MapView mapView;
    GoogleMap map;

    @Bind(R.id.txtNo) TextView text_dis_no;
    @Bind(R.id.txtDisDate) TextView text_dis_date;
    @Bind(R.id.txtColPt) TextView text_col_pt;
    @Bind(R.id.txtStatus) TextView text_status;
    @Bind(R.id.item_table) TableLayout detail_table;
    @Bind(R.id.imgPhone) ImageView img_phCall;


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
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String string_date = format.format(dis.getDisbursementDate());
        text_dis_date.setText(string_date);
        text_col_pt.setText(selected_colPt.getColPt_name());
        text_status.setText(dis.getDisbursementStatus());

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



        //table codes
        TableRow tr_head = new TableRow(this.getActivity());
        tr_head.setId(+10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this.getActivity());
        label_date.setId(+20);
        label_date.setText("Description");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_weight_kg = new TextView(this.getActivity());
        label_weight_kg.setId(+21);// define id that must be unique
        label_weight_kg.setText("Quantity"); // set the text for the header
        label_weight_kg.setTextColor(Color.WHITE); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        detail_table.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TableRow tr = new TableRow(this.getActivity());
        tr.setId(+100);
        tr.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        //Create two columns to add as table data
        // Create a TextView to add date
        TextView ladelName = new TextView(this.getActivity());
        ladelName.setId(+200);
        ladelName.setText("Pencil B");
        ladelName.setPadding(2, 0, 5, 0);
        tr.addView(ladelName);

        TextView labelQty = new TextView(this.getActivity());
        labelQty.setId(+201);
        labelQty.setText("3");
        tr.addView(labelQty);

        // finally add this to the table row
        detail_table.addView(tr, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        img_phCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:94725311");
                Intent i = new Intent(Intent.ACTION_CALL, uri);
                startActivity(i);
            }
        });

        return v;
        //return inflater.inflate(R.layout.fragment_disbursement_list_detail, container, false);
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

}

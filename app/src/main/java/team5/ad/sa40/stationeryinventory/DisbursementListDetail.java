package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentTransaction;
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
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.CollectionPointAPI;
import team5.ad.sa40.stationeryinventory.API.DisbursementAPI;
import team5.ad.sa40.stationeryinventory.API.EmployeeAPI;
import team5.ad.sa40.stationeryinventory.API.ItemAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Disbursement;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursementDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONEmployee;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisbursementListDetail extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{


    MapView mapView;
    GoogleMap map;
    List<JSONCollectionPoint> col_pts;
    JSONCollectionPoint selected_colPt;
    JSONEmployee clerk, representative;
    JSONDisbursement dis;

    @Bind(R.id.txtNo) TextView text_dis_no;
    @Bind(R.id.txtDisDate) TextView text_dis_date;
    @Bind(R.id.txtColPt) TextView text_col_pt;
    @Bind(R.id.txtStatus) TextView text_status;
    @Bind(R.id.txtRep) TextView text_rep;
    @Bind(R.id.txtClerk) TextView text_clerk;
    //@Bind(R.id.imgPhone) ImageView img_phCall;
    @Bind(R.id.btnView) Button btn_view;
    @Bind(R.id.img3) ImageView clerk_img;
    @Bind(R.id.img2) ImageView signImage;


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

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Disbursement Detail");

        Bundle bundle = this.getArguments();
        dis = (JSONDisbursement) bundle.getSerializable("disbursement");
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

                EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
                employeeAPI.getEmployeeById(dis.getEmpID(), new Callback<JSONEmployee>() {
                    @Override
                    public void success(JSONEmployee jsonEmployee, Response response) {
                        clerk = jsonEmployee;

                        text_clerk.setText(clerk.getEmpName());
                        text_dis_no.setText(String.valueOf(dis.getDisID()));
                        String string_date = Setup.parseJSONDateToString(dis.getDate());
                        text_dis_date.setText(string_date);
                        text_col_pt.setText(selected_colPt.getCPName());
                        text_status.setText(dis.getStatus());

                        map.addMarker(new MarkerOptions()
                                .position(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()))
                                .title(selected_colPt.getCPName()));
                        // Updates the location and zoom of the MapView
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getCPLat(), selected_colPt.getCPLgt()), 15);
                        map.animateCamera(cameraUpdate);

                        new InventoryDetails.DownloadImageTask(clerk_img).execute("http://192.168.31.202/img/user/" + clerk.getEmpID() + ".jpg");

                        if(dis.getStatus().equals("DISBURSED")){
                        EmployeeAPI employeeAPI = restAdapter.create(EmployeeAPI.class);
                        employeeAPI.getEmployeeById(dis.getReceivedBy(), new Callback<JSONEmployee>() {
                            @Override
                            public void success(JSONEmployee jsonEmployee, Response response) {
                                representative = jsonEmployee;

                                text_rep.setText(representative.getEmpName());

//                                String root = Environment.getExternalStorageDirectory().toString();
//                                String fname =  String.valueOf(dis.getDisID()) +".jpg";
//                                String completeDir = root + "/signatures/"+ fname;
//                                Bitmap bmp = BitmapFactory.decodeFile(completeDir);
//                                if(bmp != null){
//                                    signImage.setImageBitmap(bmp);
//                                }
                                //new InventoryDetails.DownloadImageTask(signImage).execute("http://192.168.31.202/img/signatures/" + dis.getDisID() + ".jpg");

                                new AsyncTask<Void, Void, Bitmap>() {
                                    @Override
                                    protected Bitmap doInBackground(Void... params) {
                                        Bitmap mIcon11 = null;
                                        String urldisplay = "http://192.168.31.202/img/signatures/" + dis.getDisID() + ".jpg";
                                        try {
                                            InputStream in = new java.net.URL(urldisplay).openStream();
                                            mIcon11 = BitmapFactory.decodeStream(in);
                                        } catch (Exception e) {
                                            Log.e("Error", e.getMessage());
                                            e.printStackTrace();
                                        }
                                        return mIcon11;
                                    }
                                @Override
                                protected void onPostExecute(Bitmap result) {
                                    if(result != null){
                                        signImage.setImageBitmap(result);
                                    }
                                }
                            }.execute();
                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                        }
                    }

                    @Override
                    public void failure(RetrofitError error) {

                    }
                });
//
//                img_phCall.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Uri uri = Uri.parse("tel:94725311");
//                        Intent i = new Intent(Intent.ACTION_CALL, uri);
//                        startActivity(i);
//                    }
//                });

            }

            @Override
            public void failure(RetrofitError error) {
                Log.e("getAllCollectionPoints", error.toString());
            }
        });

        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                ItemAPI itemAPI = restAdapter.create(ItemAPI.class);
                itemAPI.getItems(new Callback<List<JSONItem>>() {
                    @Override
                    public void success(List<JSONItem> jsonItems, Response response) {
                        final ArrayList<JSONItem> itemList = new ArrayList<JSONItem>(jsonItems);

                        DisbursementAPI disbursementAPI = restAdapter.create(DisbursementAPI.class);
                        disbursementAPI.getDisbursementDetail(dis.getDisID(), new Callback<List<JSONDisbursementDetail>>() {
                            @Override
                            public void success(List<JSONDisbursementDetail> jsonDisbursementDetails, Response response) {
                                ArrayList<JSONDisbursementDetail> disbursementDetails = new ArrayList<JSONDisbursementDetail>(jsonDisbursementDetails);
                                Log.e("Size of items", String.valueOf(itemList.size()));
                                Log.e("Size of disbursements", String.valueOf(disbursementDetails.size()));
                                android.support.v4.app.Fragment frag = new DisListDetailItemList();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("items", itemList);
                                bundle.putSerializable("disbursementDetail", disbursementDetails);
                                bundle.putSerializable("disbursement", dis);
                                frag.setArguments(bundle);
                                getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.e("getDisbursementDetail", error.toString());
                            }
                        });
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.e("getItems", error.toString());
                    }
                });
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
            RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
            RequisitionAPI requisitionAPI = restAdapter.create(RequisitionAPI.class);
            requisitionAPI.getRequisitionListByDisID(dis.getDisID(), new Callback<List<JSONRequisition>>() {
                @Override
                public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                    RequisitionListAdapter.mRequisitions = jsonRequisitions;
                    Setup.allRequisition = jsonRequisitions;
                    Log.e("mRequisitions Size", String.valueOf(jsonRequisitions.size()));
                    android.support.v4.app.Fragment frag = new RequisitionListFragment();
                    getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
                    Log.e("Adap mRequisitions Size", String.valueOf(RequisitionListAdapter.mRequisitions.size()));
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e("getRequisitionByDisID", error.toString());
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        DisbursementList fragment = new DisbursementList();
        FragmentTransaction fragtran = getFragmentManager().beginTransaction();
        fragtran.replace(R.id.frame, fragment).commit();
    }
}

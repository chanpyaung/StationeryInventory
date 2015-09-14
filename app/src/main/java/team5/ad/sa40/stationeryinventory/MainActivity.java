package team5.ad.sa40.stationeryinventory;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.API.RequestCartAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONRequestCart;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;

public class MainActivity extends AppCompatActivity {

    public static ActionBarDrawerToggle actionBarDrawerToggle;
    android.support.v4.app.FragmentTransaction fragmentTran;

    public static List<JSONItem> requestCart = new ArrayList<JSONItem>();
    public static String user;
    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    final RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);

    //Call UI element with butter knife
    @Bind(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawerLayout;

    private CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to receive notifications:
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //Check user role and redirect to respective layout
        switch (Setup.user.getRoleID()) {
            case "EM":
                setContentView(R.layout.activity_main);
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, mCategoryFragment);
                fragmentTransaction.commit();
                break;
            case "DD":
                setContentView(R.layout.empdelegate_activity_main);
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDD = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDD.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDD.commit();
                break;
            case "DR":
                setContentView(R.layout.emprep_activity_main);
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDR = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDR.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDR.commit();
                break;
            case "DH":
                setContentView(R.layout.depthead_activity_main);
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDH = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDH.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDH.commit();
                break;
            case "SC":
                setContentView(R.layout.storeclerk_activity_main);
                //Set fragment
                InventoryList fragment = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragment);
                fragmentTran.commit();
                break;
            case "SS":
                setContentView(R.layout.storesupervisor_activity_main);
                //Set fragment
                InventoryList fragmentSS = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragmentSS);
                fragmentTran.commit();
                break;
            case "SM":
                setContentView(R.layout.storesupervisor_activity_main);
                //Set fragment
                InventoryList fragmentSM = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragmentSM);
                fragmentTran.commit();
                break;
            default:
                setContentView(R.layout.activity_main);
                //Set fragment
                InventoryList fragmentDef = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragmentDef);
                fragmentTran.commit();
                break;
        }


        //Bind to activity
        ButterKnife.bind(this);

        //setup tool bar
        setSupportActionBar(toolbar);

        //navigation drawer item listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {

                    case R.id.catalog:
                        mCategoryFragment = new CategoryFragment();
                        final android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, mCategoryFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.requisition:
                        reqAPI.getStatus(new Callback<List<JSONStatus>>() {
                            @Override
                            public void success(List<JSONStatus> jsonStatuses, Response response) {
                                Log.i("Status Size", String.valueOf(jsonStatuses.size()));
                                RequisitionListAdapter.mStatus = jsonStatuses;
                                //if user is StoreClerk; load all requisition
                                if(Setup.user.getRoleID().equals("SC")){
                                    reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                                        @Override
                                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                            List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                                            for(JSONRequisition jsonReq : jsonRequisitions){
                                                if(jsonReq.getStatusID().equals(2)){
                                                    reqList.add(jsonReq);
                                                }
                                            }
                                            Log.i("URL", response.getUrl());
                                            Log.i("STATUS", String.valueOf(response.getStatus()));
                                            Log.i("REASON", response.getReason());
                                            Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                            RequisitionListAdapter.mRequisitions = reqList;
                                            Setup.allRequisition = reqList;
                                            RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                            fragmentTran = getSupportFragmentManager().beginTransaction();
                                            fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.i("GetRequisitionFail", error.toString());
                                        }
                                    });
                                }
                                //load requisitionlist by EmpID
                                else{
                                    reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                                        @Override
                                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                            Log.i("URL", response.getUrl());
                                            Log.i("STATUS", String.valueOf(response.getStatus()));
                                            Log.i("REASON", response.getReason());
                                            Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                            RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                            Setup.allRequisition = jsonRequisitions;
                                            RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                            RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                            fragmentTran = getSupportFragmentManager().beginTransaction();
                                            fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.i("GetRequisitionFail", error.toString());
                                        }
                                    });
                                }
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Status Fail", error.toString());
                                Log.i("URL", error.getUrl());
                            }
                        });
                        return true;

                    //load all requisition under department of current user
                    case R.id.approval:
                        //by default load all requisition
                        reqAPI.getRequisitionFromSC(new Callback<List<JSONRequisition>>() {
                            @Override
                            public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                Log.i("URL", response.getUrl());
                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                Log.i("REASON", response.getReason());
                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                                //inside all requisition filter requisition by Department & PENDING Status
                                for(JSONRequisition jsonReq : jsonRequisitions){
                                    if(jsonReq.getDeptID().equals(Setup.user.getDeptID())){
                                        Log.i("Here what", String.valueOf(jsonReq.getReqID()));
                                        if(jsonReq.getStatusID().equals(1) || jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(5)){
                                            reqList.add(jsonReq);
                                            Log.i("what here", String.valueOf(jsonReq.getReqID()));
                                        }

                                    }
                                }
                                Log.i("SIZE OFFFFF reqList", String.valueOf(reqList.size()));
                                RequisitionListAdapter.mRequisitions = reqList;
                                Setup.allRequisition = reqList;
                                //RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                fragmentTran = getSupportFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, reqListFrag).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("GetRequisitionFail", error.toString());
                            }
                        });
                        return true;

                    case R.id.cart:
                        RequestCartAPI rqAPI = restAdapter.create(RequestCartAPI.class);
                        rqAPI.getItemsbyEmpID(Setup.user.getEmpID(), new Callback<List<JSONRequestCart>>() {
                            @Override
                            public void success(List<JSONRequestCart> jsonRequestCarts, Response response) {
                                Setup.allRequestItems = jsonRequestCarts;
                                if (Setup.allRequestItems.size() > 0) {
                                    RequestCartFragment rqFrag = new RequestCartFragment();
                                    fragmentTran = getSupportFragmentManager().beginTransaction();
                                    fragmentTran.replace(R.id.frame, rqFrag).addToBackStack("REQUEST_CART_FRAG").commit();
                                } else {
                                    Toast.makeText(MainActivity.this, "We acknowledge you that you haven't add any item yet.Please add some items before you proceed.", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                        return true;

                    case R.id.Dept:
                        DepartmentInfoFragment deptFrag = new DepartmentInfoFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame,deptFrag).commit();
                        return true;

                    case R.id.disburse://change
                        DisbursementList disbursementList = new DisbursementList();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, disbursementList);
                        fragmentTran.commit();
                        return true;

                    case R.id.disbursement:
                        ClerkDisList temp = new ClerkDisList();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, temp);
                        fragmentTran.commit();
                        return true;

                    case R.id.retrieval://change
                        RetrievalList retrievalList = new RetrievalList();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, retrievalList);
                        fragmentTran.commit();
                        return true;

                    case R.id.inventory://change
                        RestAdapter restAdapter2 = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                        InventoryAPI invAPI = restAdapter2.create(InventoryAPI.class);

                        invAPI.getList(new Callback<List<JSONItem>>() {
                            @Override
                            public void success(List<JSONItem> jsonItems, Response response) {
                                Log.i("Result :", jsonItems.toString());
                                Log.i("First item: ", jsonItems.get(0).getItemID().toString());
                                Log.i("Response: ", response.getBody().toString());
                                System.out.println("Response Status " + response.getStatus());
                                InvListAdapter.mJSONItems = jsonItems;
                                System.out.println("SIZE:::::" + InvListAdapter.mJSONItems.size());
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Error: ", error.toString());
                            }
                        });
                        InventoryList fragment = new InventoryList();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, fragment);
                        fragmentTran.commit();
                        return true;

                    case R.id.delegate:
                        DelegateList del = new DelegateList();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, del);
                        fragmentTran.commit();
                        return true;

                    case R.id.reportItem://change
                        AdjVouList reportItemFrag = new AdjVouList();
                        //ReportItemListFragment reportItemFrag = new ReportItemListFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, reportItemFrag);
                        fragmentTran.commit();
                        return true;

                    case R.id.notification://change
                        NotificationFragment notification = new NotificationFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, notification);
                        fragmentTran.commit();
                        return true;

                    case R.id.Setting:
                        SignatureFragment signFrag = new SignatureFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, signFrag).commit();
                        return true;

                    case R.id.logout:
                        SharedPreferences pref =
                                PreferenceManager.getDefaultSharedPreferences
                                        (getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.commit();
                        ParseUser.logOutInBackground();

                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(i);
                        return true;

                    default:
                        Toast.makeText(MainActivity.this, "Default", Toast.LENGTH_SHORT).show();
                        return true;
                }
            }
        });

        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
}
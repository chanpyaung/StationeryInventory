package team5.ad.sa40.stationeryinventory.Fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.API.ReportAPI;
import team5.ad.sa40.stationeryinventory.API.RequestCartAPI;
import team5.ad.sa40.stationeryinventory.API.RequisitionAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.Model.JSONRequestCart;
import team5.ad.sa40.stationeryinventory.Model.JSONRequisition;
import team5.ad.sa40.stationeryinventory.Model.JSONStatus;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;


public class MainActivity extends AppCompatActivity {

    static OnBackPressedListener onBackPressedListener;

    public static ActionBarDrawerToggle actionBarDrawerToggle;
    android.support.v4.app.FragmentTransaction fragmentTran;

    public static List<JSONItem> requestCart = new ArrayList<JSONItem>();
    public static String user;
    final RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
    final RequisitionAPI reqAPI = restAdapter.create(RequisitionAPI.class);
    Boolean drawerisOpen = false;

    //Call UI element with butter knife
    @Bind(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawerLayout;


    private CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //to receive notifications for the employee logged in:
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.remove("channels");
        String emp = Setup.user.getRoleID() + String.valueOf(Setup.user.getEmpID());
        installation.add("channels",emp);
        Log.i("channel: ", installation.get("channels").toString());
        installation.saveInBackground();
        ParseAnalytics.trackAppOpenedInBackground(getIntent());

        //Check user role and redirect to respective layout
        switch (Setup.user.getRoleID()) {
            case "EM":
                setContentView(R.layout.activity_main);
                CircleImageView profileImage = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameText = (TextView) findViewById(R.id.unameText);
                TextView email = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImage).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameText.setText(Setup.user.getEmpName());
                email.setText("Employee");
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, mCategoryFragment);
                fragmentTransaction.commit();
                break;
            case "DD":
                setContentView(R.layout.empdelegate_activity_main);
                CircleImageView profileImageDD = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextDD = (TextView) findViewById(R.id.unameText);
                TextView emailDD = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageDD).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextDD.setText(Setup.user.getEmpName());
                emailDD.setText("Department Delegate");
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDD = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDD.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDD.commit();
                break;
            case "DR":
                setContentView(R.layout.emprep_activity_main);
                CircleImageView profileImageDR = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextDR = (TextView) findViewById(R.id.unameText);
                TextView emailDR = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageDR).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextDR.setText(Setup.user.getEmpName());
                emailDR.setText("Department Representative");
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDR = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDR.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDR.commit();
                break;
            case "DH":
                setContentView(R.layout.depthead_activity_main);
                CircleImageView profileImageDH = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextDH = (TextView) findViewById(R.id.unameText);
                TextView emailDH = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageDH).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextDH.setText(Setup.user.getEmpName());
                emailDH.setText("Department Head");
                //Set fragment
                mCategoryFragment = new CategoryFragment();
                android.support.v4.app.FragmentTransaction fragmentTransactionDH = getSupportFragmentManager().beginTransaction();
                fragmentTransactionDH.replace(R.id.frame, mCategoryFragment);
                fragmentTransactionDH.commit();
                break;
            case "SC":
                setContentView(R.layout.storeclerk_activity_main);
                CircleImageView profileImageSC = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextSC = (TextView) findViewById(R.id.unameText);
                TextView emailSC = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageSC).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextSC.setText(Setup.user.getEmpName());
                emailSC.setText("Store Clerk");
                //Set fragment
                InventoryList fragment = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragment);
                fragmentTran.commit();
                break;
            case "SS":
                setContentView(R.layout.storesupervisor_activity_main);
                CircleImageView profileImageSS = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextSS = (TextView) findViewById(R.id.unameText);
                TextView emailSS = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageSS).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextSS.setText(Setup.user.getEmpName());
                emailSS.setText("Store Supervisor");
                //Set fragment
                InventoryList fragmentSS = new InventoryList();
                fragmentTran = getSupportFragmentManager().beginTransaction();
                fragmentTran.replace(R.id.frame, fragmentSS);
                fragmentTran.commit();
                break;
            case "SM":
                setContentView(R.layout.storesupervisor_activity_main);
                CircleImageView profileImageSM = (CircleImageView) findViewById(R.id.profile_image);
                TextView unameTextSM = (TextView) findViewById(R.id.unameText);
                TextView emailSM = (TextView) findViewById(R.id.email);
                new ItemListAdapter.DownloadImageTask(profileImageSM).execute("http://192.168.31.202/img/user/" + Setup.user.getEmpID() + ".jpg");
                unameTextSM.setText(Setup.user.getEmpName());
                emailSM.setText("Store Manager");
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
                        Setup.MODE = 1;
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
                                            if (jsonRequisitions.size() > 0) {
                                                List<JSONRequisition> reqList = new ArrayList<JSONRequisition>();
                                                for (JSONRequisition jsonReq : jsonRequisitions) {
                                                    if (jsonReq.getStatusID().equals(2)) {
                                                        reqList.add(jsonReq);
                                                    }
                                                }
                                                Log.i("URL", response.getUrl());
                                                Log.i("STATUS", String.valueOf(response.getStatus()));
                                                Log.i("REASON", response.getReason());
                                                Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                                if (jsonRequisitions.size() > 0) {
                                                    System.out.println("Sorting here");
                                                    Collections.sort(jsonRequisitions);
                                                    Setup.allRequisition = reqList;
                                                    RequisitionListAdapter.mRequisitions = reqList;
                                                    for (JSONRequisition jr : jsonRequisitions) {
                                                        System.out.println("ordered by Date" + jr.getDate() + " " + jr.getReqID());
                                                    }
                                                }
                                                RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                                fragmentTran = getSupportFragmentManager().beginTransaction();
                                                fragmentTran.replace(R.id.frame, reqListFrag).commit();

                                            } else {

                                                Toast.makeText(MainActivity.this, "We acknowledge you that you haven't made any requisition yet.Please made some requisition before you proceed.", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                            @Override
                                            public void failure (RetrofitError error){
                                                Log.i("GetRequisitionFail", error.toString() + " " + error.getUrl());
                                            }

                                    });
                                }
                                //load requisitionlist by EmpID
                                else{
                                    reqAPI.getRequisition(Setup.user.getEmpID(), new Callback<List<JSONRequisition>>() {
                                        @Override
                                        public void success(List<JSONRequisition> jsonRequisitions, Response response) {
                                            if (jsonRequisitions.size()>0){
                                            Log.i("URL", response.getUrl());
                                            Log.i("STATUS", String.valueOf(response.getStatus()));
                                            Log.i("REASON", response.getReason());
                                            Log.i("Size of requisition", String.valueOf(jsonRequisitions.size()));
                                            if(jsonRequisitions.size()>0){
                                                System.out.println("Sorting here");
                                                Collections.sort(jsonRequisitions);
                                                Setup.allRequisition = jsonRequisitions;
                                                RequisitionListAdapter.mRequisitions = jsonRequisitions;
                                                for(JSONRequisition jr : jsonRequisitions){
                                                    System.out.println("ordered by Date" + jr.getDate() + " " +jr.getReqID() );
                                                }
                                            }
                                            RequisitionListAdapter.mRequisitions = Setup.allRequisition;
                                            RequisitionListFragment reqListFrag = new RequisitionListFragment();
                                            fragmentTran = getSupportFragmentManager().beginTransaction();
                                            fragmentTran.replace(R.id.frame, reqListFrag).commit();
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "We acknowledge you that you haven't made any requisition yet.Please made some requisition before you proceed.", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                        @Override
                                        public void failure(RetrofitError error) {
                                            Log.i("GetRequisitionFail", error.toString()+ " " + error.getUrl());
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
                        Setup.MODE = 2;
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
                                    if(jsonReq.getDeptID()!=null) {
                                        if (jsonReq.getDeptID().toString().equals(Setup.user.getDeptID().toString())) {
                                            Log.i("Here what", String.valueOf(jsonReq.getReqID()));
                                            if (jsonReq.getStatusID().equals(1) || jsonReq.getStatusID().equals(2) || jsonReq.getStatusID().equals(5)) {
                                                reqList.add(jsonReq);
                                                Log.i("what here", String.valueOf(jsonReq.getReqID()));
                                            }

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
                                Log.i("GetRequisitionFail", error.toString()+ " " + error.getUrl());
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
                        //AdjVouList reportItemFrag = new AdjVouList();
                        ReportItemListFragment reportItemFrag = new ReportItemListFragment();
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

                    case R.id.logout:
                        //clear user
                        SharedPreferences pref =
                                PreferenceManager.getDefaultSharedPreferences
                                        (getApplicationContext());
                        SharedPreferences.Editor editor = pref.edit();
                        editor.putString("username", null);
                        editor.putString("password", null);
                        editor.remove("username");
                        editor.remove("password");
                        editor.commit();
                        finish();
                        System.exit(0);
                        return true;

                    case R.id.analytics:
                        ReportAPI rpAPI = restAdapter.create(ReportAPI.class);
                        rpAPI.getReports(new Callback<List<JSONReport>>() {
                            @Override
                            public void success(List<JSONReport> jsonReports, Response response) {
                                AnalyticsAdapter.mReports = jsonReports;
                                AnalyticsListFragment afrag = new AnalyticsListFragment();
                                fragmentTran = getSupportFragmentManager().beginTransaction();
                                fragmentTran.replace(R.id.frame, afrag).commit();
                            }

                            @Override
                            public void failure(RetrofitError error) {
                                Log.i("Fail getReports", error.toString());
                            }
                        });
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
            public void onDrawerOpened(View drawerView){
                    drawerisOpen = true;
                    super.onDrawerOpened(drawerView);
                }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(this.navigationView)){
            drawerLayout.closeDrawers();
            return;
        }
            actionBarDrawerToggle.syncState();
            if(onBackPressedListener != null){
                onBackPressedListener.doBack();
            }
            else {
                super.onBackPressed();
            }
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

    public interface OnBackPressedListener{
        void doBack();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener){
        this.onBackPressedListener = onBackPressedListener;
    }

    @Override
    protected void onDestroy() {
        onBackPressedListener = null;
        super.onDestroy();
    }
}
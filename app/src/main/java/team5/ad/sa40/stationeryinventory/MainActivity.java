package team5.ad.sa40.stationeryinventory;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Item;

public class MainActivity extends AppCompatActivity {

    public static ActionBarDrawerToggle actionBarDrawerToggle;
    android.support.v4.app.FragmentTransaction fragmentTran;

    public static List<Item> requestCart = new ArrayList<Item>();
    public static String user;

    //Call UI element with butter knife
    @Bind(R.id.toolbar) android.support.v7.widget.Toolbar toolbar;
    @Bind(R.id.navigation_view) NavigationView navigationView;
    @Bind(R.id.drawer) DrawerLayout drawerLayout;

    private CategoryFragment mCategoryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Check user role and redirect to respective layout
        switch (Setup.user.getRoleID()) {
            case "EM":
                setContentView(R.layout.activity_main);
                break;
            case "DD":
                setContentView(R.layout.empdelegate_activity_main);
                break;
            case "DR":
                setContentView(R.layout.emprep_activity_main);
                break;
            case "DH":
                setContentView(R.layout.depthead_activity_main);
                break;
            case "SC":
                setContentView(R.layout.storeclerk_activity_main);
                break;
            case "SS":
                setContentView(R.layout.storesupervisor_activity_main);
                break;
            case "SM":
                setContentView(R.layout.storesupervisor_activity_main);
                break;
            default:
                setContentView(R.layout.activity_main);
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
                        Toast.makeText(MainActivity.this, "Catalog is selected", Toast.LENGTH_SHORT).show();
                        mCategoryFragment = new CategoryFragment();
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, mCategoryFragment);
                        fragmentTransaction.commit();
                        return true;

                    case R.id.requisition:
                        RequisitionListFragment reqListFrag = new RequisitionListFragment();
                        Bundle args = new Bundle();
                        args.putString("User",user );
                        reqListFrag.setArguments(args);
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,reqListFrag).commit();
                        Toast.makeText(MainActivity.this, "Requisition is selected", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.cart:
                        Toast.makeText(MainActivity.this, "Request cart is selected", Toast.LENGTH_SHORT).show();
                        return true;

                    case R.id.Dept:
                        DepartmentInfoFragment deptFrag = new DepartmentInfoFragment();
                        fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame,deptFrag).commit();
                        return true;

                    case R.id.noti:
                        NotificationFragment notiFrag = new NotificationFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, notiFrag).addToBackStack("NOTI TAG").commit();

                    case R.id.setting:
                        SettingListFragment settingFragment = new SettingListFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        //FragmentTransaction fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, settingFragment);
                        fragmentTran.commit();
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
                        ReportItemSearchFragment reportItemFrag = new ReportItemSearchFragment();
                        fragmentTran = getSupportFragmentManager().beginTransaction();
                        fragmentTran.replace(R.id.frame, reportItemFrag);
                        fragmentTran.commit();
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
        //Set Fragment

        /*
        mCategoryFragment = new CategoryFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, mCategoryFragment);
        fragmentTransaction.commit();
        */

        InventoryList fragment = new InventoryList();
        fragmentTran = getSupportFragmentManager().beginTransaction();
        fragmentTran.replace(R.id.frame, fragment);
        fragmentTran.commit();

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
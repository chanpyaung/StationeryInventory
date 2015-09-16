package team5.ad.sa40.stationeryinventory.Fragment;


import android.app.Fragment;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.CollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.Department;
import team5.ad.sa40.stationeryinventory.Model.Employee;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;


/**
 * A simple {@link Fragment} subclass.
 */
public class DepartmentInfoFragment extends android.support.v4.app.Fragment implements MainActivity.OnBackPressedListener{

    @Bind(R.id.contactName) AutoCompleteTextView contactName;
    @Bind(R.id.telephone) EditText telephone;
    @Bind(R.id.fax) EditText fax;
    @Bind(R.id.deptHead) AutoCompleteTextView deptHead;
    @Bind(R.id.spinnerRep) Spinner representative;
    @Bind(R.id.spinnerCPoint) Spinner collectionPoint;
    MapView cpMap;
    GoogleMap map;
    Department dept;
    String[] col_names;
    String[] emp_names;
    ArrayList<CollectionPoint> collectionPoints;
    ArrayList<Employee> employees;
    CollectionPoint currentCol;
    Employee currentRep;
    Employee currentHead;
    Employee newEmpHead;
    Employee newRep;
    Employee currentContact;
    String Rflag = "true";
    String Hflag = "true";
    String Dflag = "true";
    String dH = "Null"; String dR = "Null"; String dC = "Null";
    CollectionPoint selected_colPt;

    public DepartmentInfoFragment() {
        // Required empty public constructor
        dept = new Department();
        collectionPoints = new ArrayList<>();
        employees = new ArrayList<>();
        currentHead = new Employee();
        currentRep = new Employee();
        newEmpHead = new Employee();
        currentContact = new Employee();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_department_info, container, false);
        ButterKnife.bind(this, view);
        if(!Setup.user.getRoleID().equals("EM")){
            setHasOptionsMenu(true);
        }

        ((MainActivity)getActivity()).setOnBackPressedListener(this);

        getActivity().setTitle("Department Info");

        if(Setup.user.getRoleID().equals("EM")){
            representative.setEnabled(false);
            collectionPoint.setEnabled(false);
            telephone.setEnabled(false);
            fax.setEnabled(false);
            deptHead.setEnabled(false);
            contactName.setEnabled(false);
        }
        if(Setup.user.getRoleID().equals("DR")){
            representative.setEnabled(false);
            telephone.setEnabled(false);
            fax.setEnabled(false);
            deptHead.setEnabled(false);
            contactName.setEnabled(false);
        }
//        telephone.setEnabled(false);
//        fax.setEnabled(false);
//        deptHead.setEnabled(false);
//        contactName.setEnabled(false);

        cpMap = (MapView) view.findViewById(R.id.mapview);
        cpMap.onCreate(savedInstanceState);
        cpMap.onResume();

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

        new AsyncTask<Void, Void, Department>(){
            @Override
            protected Department doInBackground(Void... params) {
                return Department.getDeptByID(Setup.user.getDeptID());//Should be Setup.user.DeptID
            }
            @Override
            protected void onPostExecute(Department result) {
                dept = result;
                Log.e("dept id", Setup.user.getDeptID());
                Log.e("dept name", dept.getDeptName());
                new AsyncTask<Void, Void, ArrayList<CollectionPoint>>(){
                    @Override
                    protected ArrayList<CollectionPoint> doInBackground(Void... params) {
                        return CollectionPoint.getAllCollectionPoints();
                    }
                    @Override
                    protected void onPostExecute(ArrayList<CollectionPoint> result) {
                        collectionPoints = result;
                        col_names = new String[collectionPoints.size()];
                        for(int i = 0; i < collectionPoints.size(); i++){
                            col_names[i] = collectionPoints.get(i).getColPt_name();
                            map.addMarker(new MarkerOptions()
                                    .position(new LatLng(collectionPoints.get(i).getColPt_lat(), collectionPoints.get(i).getColPt_long()))
                                    .title(collectionPoints.get(i).getColPt_name()));
                            if(collectionPoints.get(i).getColPt_id() == dept.getCpID()){
                                currentCol = collectionPoints.get(i);
                                selected_colPt = new CollectionPoint();
                                selected_colPt.setColPt_lat(currentCol.getColPt_lat());
                                selected_colPt.setColPt_long(currentCol.getColPt_long());
                                // Updates the location and zoom of the MapView
                                collectionPoint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        for (int i = 0; i < collectionPoints.size(); i++) {
                                            if (collectionPoint.getSelectedItem().toString().equals(collectionPoints.get(i).getColPt_name())) {
                                                selected_colPt = collectionPoints.get(i);
                                                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(selected_colPt.getColPt_lat(), selected_colPt.getColPt_long()), 15);
                                                map.animateCamera(cameraUpdate);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }
                        }
                        new AsyncTask<Void, Void, ArrayList<Employee>>(){
                            @Override
                            protected ArrayList<Employee> doInBackground(Void... params) {
                                return Employee.getEmployeeByDept(Setup.user.getDeptID());//Should be Setup.user.DeptID
                            }
                            @Override
                            protected void onPostExecute(ArrayList<Employee> result) {
                                employees = result;
                                emp_names = new String[employees.size()];
                                for(int i = 0; i < employees.size(); i++){
                                    emp_names[i] = employees.get(i).getEmpName();
                                }
                                setValues();
                            }
                        }.execute();
                    }
                }.execute();
            }
        }.execute();

        return  view;
    }

    public void setValues(){

        Log.e("Size of col", String.valueOf(collectionPoints.size()));
        Log.e("Name of dept", String.valueOf(dept.getDeptID()));
        Log.e("Size of emp", String.valueOf(employees.size()));
        if(dept != null && collectionPoints.size()>0) {
            Log.i("Current Col", col_names[0]);
            for(int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmpID() == dept.getContact()) {
                    contactName.setText(String.valueOf(employees.get(i).getEmpName()));
                    currentContact = employees.get(i);
                }
            }
            telephone.setText(String.valueOf(dept.getPhone()));
            fax.setText(String.valueOf(dept.getFax()));

            for(int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getRoleID().equals("DH")) {
                    currentHead = employees.get(i);
                }
                else if (employees.get(i).getRoleID().equals("DR")){
                    currentRep = employees.get(i);
                }
            }
            deptHead.setText(String.valueOf(currentHead.getEmpName()));
            Log.e("Current Rep", currentRep.getEmpName());
            Log.e("Current Head", currentHead.getEmpName());

            ArrayAdapter cAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,emp_names);
            contactName.setAdapter(cAdapter);
            contactName.setThreshold(1);

            ArrayAdapter hAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,emp_names);
            deptHead.setAdapter(hAdapter);
            deptHead.setThreshold(1);

            ArrayAdapter<String> col_collect = new ArrayAdapter<String>(DepartmentInfoFragment.this.getActivity(),android.R.layout.simple_spinner_item, col_names);
            col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            collectionPoint.setAdapter(col_collect);
            Log.i("Current Col", currentCol.getColPt_name());
            int index = col_collect.getPosition(currentCol.getColPt_name());
            collectionPoint.setSelection(index);

            ArrayAdapter<String> emp_collect = new ArrayAdapter<String>(DepartmentInfoFragment.this.getActivity(),android.R.layout.simple_spinner_item, emp_names);
            emp_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            representative.setAdapter(emp_collect);
            if(currentRep != null){
                int rindex = emp_collect.getPosition(currentRep.getEmpName());
                representative.setSelection(rindex);
            }

        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.getActivity().getMenuInflater().inflate(R.menu.fragment_requestcart_done_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_request_done){
            if(!contactName.getText().toString().trim().isEmpty() && !telephone.getText().toString().trim().isEmpty() && !fax.getText().toString().trim().isEmpty() && !deptHead.getText().toString().trim().isEmpty()){
            Log.i("DeptHead text", deptHead.getText().toString());
            for(int i = 0; i < employees.size(); i ++){
                if(deptHead.getText().toString().equals(employees.get(i).getEmpName())){
                    if(!deptHead.getText().equals(currentHead.getEmpName())) {
                        //change the current dept head role and add new emp role
                        newEmpHead = employees.get(i);
                        dH = "New";
                    }
                    else{
                        newEmpHead = currentHead;
                        dH = "Current";
                    }
                }
                if(representative.getSelectedItem().toString().equals(employees.get(i).getEmpName())){
                    if(!representative.getSelectedItem().toString().equals(currentRep.getEmpName())) {
                        //change the current rep role and add new emp role
                        newRep = employees.get(i);
                        dR = "New";
                    }
                    else{
                        newRep = currentRep;
                        dR = "Current";
                    }
                }
                if(contactName.getText().toString().equals(employees.get(i).getEmpName())){
                    Log.e("Current Contact", employees.get(i).getEmpName());
                    Log.e("Test contact", contactName.getText().toString());
                    currentContact = employees.get(i);
                    dC = "Not Null";
                }
            }
                if(!dH.equals("Null")){
                    if(!dC.equals("Null")){
                        dept.setDeptHead(newEmpHead.getEmpID());
                        dept.setDeptRep(newRep.getEmpID());
                        dept.setPhone(Integer.parseInt(telephone.getText().toString()));
                        dept.setFax(Integer.parseInt(fax.getText().toString()));
                        dept.setContact(currentContact.getEmpID());

                        for(int z = 0; z < collectionPoints.size(); z++){
                            if(collectionPoint.getSelectedItem().toString().equals(collectionPoints.get(z).getColPt_name())){
                                if(!collectionPoint.getSelectedItem().toString().equals(currentCol.getColPt_name())){
                                    //change the current collection point and add new collection
                                    dept.setCpID(collectionPoints.get(z).getColPt_id());
                                }
                            }
                        }
                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                return Department.updateDepartment(dept);
                            }
                            @Override
                            protected void onPostExecute(String result) {
                                Log.e("In Update Dept", result);
                                if(result.equals("true")){
                                    Dflag = "true";
                                    for(int z = 0; z < collectionPoints.size(); z++){
                                        if(collectionPoint.getSelectedItem().toString().equals(collectionPoints.get(z).getColPt_name())){
                                                currentCol = collectionPoints.get(z);
                                        }
                                    }
                                    if(!newEmpHead.getEmail().equals(currentHead.getEmail())) {
                                        new AsyncTask<Void, Void, String>() {
                                            @Override
                                            protected String doInBackground(Void... params) {
                                                return Employee.updateEmployeeRole(String.valueOf(newEmpHead.getEmpID()), "DH");
                                            }

                                            @Override
                                            protected void onPostExecute(String result) {
                                                if (result.equals("true")) {
                                                    new AsyncTask<Void, Void, String>() {
                                                        @Override
                                                        protected String doInBackground(Void... params) {
                                                            return Employee.updateEmployeeRole(String.valueOf(currentHead.getEmpID()), "EM");
                                                        }

                                                        @Override
                                                        protected void onPostExecute(String result) {
                                                            Log.e("In Update Head", result);
                                                            if (!result.trim().equals("true")) {
                                                                Hflag = result.trim();
                                                            } else {
                                                                currentHead = newEmpHead;
                                                                newEmpHead = null;
                                                                dH = "Null";
                                                            }
                                                        }
                                                    }.execute();
                                                }
                                            }
                                        }.execute();
                                    }
                                    if(!currentRep.getEmail().equals(newRep.getEmail())){
                                        new AsyncTask<Void, Void, String>() {
                                            @Override
                                            protected String doInBackground(Void... params) {
                                                if(!newRep.getEmail().equals(currentHead.getEmail())){
                                                    return Employee.updateEmployeeRole(String.valueOf(newRep.getEmpID()), "DR");
                                                }
                                                else
                                                {
                                                    return "false";
                                                }
                                            }
                                            @Override
                                            protected void onPostExecute(String result) {
                                                Log.e("True or not", result);
                                                if (result.equals("true")){
                                                    new AsyncTask<Void, Void, String>() {
                                                        @Override
                                                        protected String doInBackground(Void... params) {
                                                            return Employee.updateEmployeeRole(String.valueOf(currentRep.getEmpID()), "EM");
                                                        }
                                                        @Override
                                                        protected void onPostExecute(String result) {
                                                            Log.e("In Update Rep", result);
                                                            Log.e("Count", String.valueOf(result.toCharArray().length));
                                                            if(!result.equals("true")){
                                                                Rflag = result;
                                                            }
                                                            else{
                                                                currentRep = newRep;
                                                                newRep = null; dR = "Null";
                                                            }
                                                        }
                                                    }.execute();
                                                }
                                            }
                                        }.execute();
                                    }
                                }
                                else{
                                    Dflag = "false";
                                }
                            }
                        }.execute();
                    }
                    else{
                        Dflag = "CNull";
                    }
                }
                else{
                    Dflag = "DNull";
                }
            }
            else{
                Dflag = "none";
            }
            if(Dflag.equals("false")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Fail")
                        .setMessage("The department info cannot be updated!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(Dflag.equals("none")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Fail")
                        .setMessage("The input fields cannot be null!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(Dflag.equals("DNull")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Fail")
                        .setMessage("The department head cannot be found!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else if(Dflag.equals("CNull")){
                new AlertDialog.Builder(getActivity())
                        .setTitle("Fail")
                        .setMessage("The contact person cannot be found!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
            else{
                new AlertDialog.Builder(getActivity())
                        .setTitle("Success")
                        .setMessage("The department info is updated!")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doBack() {
        android.support.v4.app.Fragment frag = new DepartmentInfoFragment();
        getFragmentManager().beginTransaction().replace(R.id.frame, frag).commit();
    }
}

package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import team5.ad.sa40.stationeryinventory.Model.Employee;


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
    String[] col_names;
    String[] emp_names;
    ArrayList<CollectionPoint> collectionPoints;
    ArrayList<Employee> employees;
    String currentCol;
    String currentRep;

    public DepartmentInfoFragment() {
        // Required empty public constructor
        dept = new Department();
        collectionPoints = new ArrayList<>();
        employees = new ArrayList<>();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_department_info, container, false);
        ButterKnife.bind(this, view);

        new AsyncTask<Void, Void, Department>(){
            @Override
            protected Department doInBackground(Void... params) {
                return Department.getDeptByID("ENGL");//Should be Setup.user.DeptID
            }
            @Override
            protected void onPostExecute(Department result) {
                dept = result;
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
                            if(collectionPoints.get(i).getColPt_id() == dept.getCpID()){
                                currentCol = collectionPoints.get(i).getColPt_name();
                            }
                        }
                        new AsyncTask<Void, Void, ArrayList<Employee>>(){
                            @Override
                            protected ArrayList<Employee> doInBackground(Void... params) {
                                return Employee.getEmployeeByDept("ENGL");//Should be Setup.user.DeptID
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

    public void setValues(){

        Log.e("Size of col", String.valueOf(collectionPoints.size()));
        Log.e("Name of dept", String.valueOf(dept.getDeptID()));
        if(dept != null && collectionPoints.size()>0) {
            Log.i("Current Col", col_names[0]);
            for(int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getEmpID() == dept.getContact()) {
                    contactName.setText(String.valueOf(employees.get(i).getEmpName()));
                }
            }
            telephone.setText(String.valueOf(dept.getPhone()));
            fax.setText(String.valueOf(dept.getFax()));

            for(int i = 0; i < employees.size(); i++) {
                if (employees.get(i).getRoleID() == "DH") {
                    deptHead.setText(String.valueOf(employees.get(i).getEmpName()));
                }
                else if (employees.get(i).getRoleID() == "DR"){
                    currentRep = employees.get(i).getEmpName();
                }
            }
            ArrayAdapter<String> col_collect = new ArrayAdapter<String>(DepartmentInfoFragment.this.getActivity(),android.R.layout.simple_spinner_item, col_names);
            col_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            collectionPoint.setAdapter(col_collect);
            Log.i("Current Col", currentCol);
            int index = col_collect.getPosition(currentCol);
            collectionPoint.setSelection(index);

            ArrayAdapter<String> emp_collect = new ArrayAdapter<String>(DepartmentInfoFragment.this.getActivity(),android.R.layout.simple_spinner_item, emp_names);
            emp_collect.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            representative.setAdapter(emp_collect);
            //Log.i("Current Rep", currentRep);
            int rindex = emp_collect.getPosition(currentRep);
            representative.setSelection(rindex);

        }
    }
}

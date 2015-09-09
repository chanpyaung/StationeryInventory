package team5.ad.sa40.stationeryinventory;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import team5.ad.sa40.stationeryinventory.Model.Item;

import static team5.ad.sa40.stationeryinventory.Model.Constants.ITEM_CODE;
import static team5.ad.sa40.stationeryinventory.Model.Constants.ITEM_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemSearchFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.search_itemCode) EditText searchItemCode;
    @Bind(R.id.search_itemName) EditText searchItemName;
    @Bind(R.id.spinner_item_category) Spinner spinnerCateogry;
    @Bind(R.id.search_result_list) ListView searchResultList;
    @Bind(R.id.btnSearchItem) Button btnSearchItem;

    private ArrayList<HashMap<String, String>> list;
    private List<Item> mitemList;
    ReportItemAdapter rpAdaper;
    FragmentActivity fragAcivity;

    private String[] categories = {"All Categories","Clip","Envelope","Eraser","Exercise","File","Pen","Puncher",
            "Pad","Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Tparency","Tray"};

    public ReportItemSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_item_search, container, false);
        ButterKnife.bind(this,view);

        mitemList = Item.initializeData();
        //mitemList = CategoryItem.getAllCategoryItems();
        list = new ArrayList<HashMap<String, String>>();

        for(Item i : mitemList){
            HashMap<String, String > temp = new HashMap<String, String>();
            temp.put(ITEM_CODE, i.getItemID());
            temp.put(ITEM_NAME, i.getItemName());
            list.add(temp);
        }
        fragAcivity  = getActivity();
        rpAdaper = new ReportItemAdapter(fragAcivity, list);
        //load spinner
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,categories);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCateogry.setAdapter(FiltersAdapter);
        rpAdaper.SetOnItemClickListener(new ReportItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ReportItemSearchFragment.this.getActivity(), "click at "+ position, Toast.LENGTH_SHORT).show();
            }
        });
        return  view;
    }

    @OnClick(R.id.btnSearchItem) void search(){
        //connect to server and handled JSON here
        //Connect adapter
        list.clear();
        String itemcode = searchItemCode.getText().toString();
        String itemname = searchItemName.getText().toString();
        System.out.println("========"+itemcode+"===="+itemname);
        Log.i(itemcode,itemname);
        for(Item i : mitemList){
            Log.i(i.getItemID(), i.getItemName());
            if(itemcode.equals(i.getItemID()) || itemname.equals(i.getItemName())){
                Log.i("SearchItemCode", searchItemCode.getText().toString()+" item value "+i.getItemID());
                Log.i("SearchItemName", searchItemName.getText().toString() + " item value " + i.getItemName());
                HashMap<String, String > temp = new HashMap<String, String>();
                temp.put(ITEM_CODE, i.getItemID());
                temp.put(ITEM_NAME, i.getItemName());
                list.add(temp);

            }
        }
        Log.i("list", list.toString());
        rpAdaper = new ReportItemAdapter(fragAcivity, list);
        searchResultList.setAdapter(rpAdaper);
        searchResultList.setClickable(true);
        searchResultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ReportItemSearchFragment.this.getActivity().getBaseContext(), "Hello from position "+position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("Click at"," "+position);
        Toast.makeText(ReportItemSearchFragment.this.getActivity(), "Click at "+ position, Toast.LENGTH_SHORT).show();
    }
}

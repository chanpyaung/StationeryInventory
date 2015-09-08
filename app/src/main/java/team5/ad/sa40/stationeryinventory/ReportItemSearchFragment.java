package team5.ad.sa40.stationeryinventory;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

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
public class ReportItemSearchFragment extends android.support.v4.app.Fragment{

    @Bind(R.id.search_itemCode) EditText searchItemCode;
    @Bind(R.id.search_itemName) EditText searchItemName;
    @Bind(R.id.spinner_item_category) Spinner spinnerCateogry;
    @Bind(R.id.search_result_list) ListView searchResultList;
    @Bind(R.id.btnSearchItem) Button btnSearchItem;

    private ArrayList<HashMap<String, String>> list;
    private List<Item> mitemList;
    ReportItemAdapter rpAdaper;

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

        mitemList = CategoryItem.getAllCategoryItems();
        list = new ArrayList<HashMap<String, String>>();

        for(Item i : mitemList){
            HashMap<String, String > temp = new HashMap<String, String>();
            temp.put(ITEM_CODE, i.getItemID());
            temp.put(ITEM_NAME, i.getItemName());
            list.add(temp);
        }
        rpAdaper = new ReportItemAdapter(this.getActivity(), list);
        //load spinner
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,categories);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerCateogry.setAdapter(FiltersAdapter);
        return  view;
    }

    @OnClick(R.id.btnSearchItem) void search(){
        //connect to server and handled JSON here
        //Connect adapter
        searchResultList.setAdapter(rpAdaper);
    }
}

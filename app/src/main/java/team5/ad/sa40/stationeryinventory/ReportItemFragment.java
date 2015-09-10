package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemFragment extends android.support.v4.app.Fragment implements View.OnClickListener {


    @Bind(R.id.itemCode) TextView itemCode;
    @Bind(R.id.itemName) TextView itemName;
    @Bind(R.id.availableQty) TextView availableQty;
    @Bind(R.id.roLvl) TextView reOrderLvl;
    @Bind(R.id.reported_qty) EditText reportedQtyField;
    @Bind(R.id.reasonSpinner) Spinner reasonSpinner;
    @Bind(R.id.remark_text) EditText remark;
    @Bind(R.id.add2Adj) Button addtoAdjustment;
    @Bind(R.id.itemImg) ImageView itemImg;


    private JSONItem reportItem;
    private String[] reasons = {"Damaged", "Oversight", "Adhoc"};
    private int reportedQty = 0;
    private double itemPrice = 0;
    private String reasonSelected = "Damaged";

    public ReportItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        Bundle args = getArguments();
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_report_item, container, false);
        ButterKnife.bind(this,view);
        reportItem = new JSONItem();

        if(args != null){
            reportItem.setItemID(args.getString("ITEMCODE"));
            if(args.getString("ITEMPRICE") != "") {
                itemPrice = Double.parseDouble(args.getString("ITEMPRICE"));
                itemCode.setText(args.getString("ITEMCODE"));
                itemName.setText(args.getString("ITEMNAME"));
                availableQty.setText(Integer.toString(args.getInt("STOCK")));
                reOrderLvl.setText(Integer.toString(args.getInt("ROLVL")));
            }
            else {
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                InventoryAPI invAPI = restAdapter.create(InventoryAPI.class);

                invAPI.getItemDetails(reportItem.getItemID(), new Callback<JSONItem>() {
                    @Override
                    public void success(JSONItem jsonItem, Response response) {
                        Log.i("Result :", jsonItem.toString());
                        Log.i("First item: ", jsonItem.getItemID().toString());
                        Log.i("Response: ", response.getBody().toString());
                        System.out.println("Response Status " + response.getStatus());

                        itemCode.setText(jsonItem.getItemID());
                        itemName.setText(jsonItem.getItemName());
                        availableQty.setText(String.valueOf(jsonItem.getStock()));
                        reOrderLvl.setText(String.valueOf(jsonItem.getRoLvl()));
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error: ", error.toString());
                    }
                });
            }
        }

        //load image
        new InventoryDetails.DownloadImageTask(itemImg)
                .execute("http://192.168.31.202/img/Item_120/" + reportItem.getItemID() + ".jpg");

        //load spinner
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,reasons);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        reasonSpinner.setAdapter(FiltersAdapter);

        reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                reasonSelected = reasons[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //set onclickListener for button
        addtoAdjustment.setOnClickListener(this);

        //Validation for qty input to report
        reportedQtyField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String in = reportedQtyField.getText().toString();
                Log.i("actual qty:", in);
                if (in == null || in == "") {
                    in = "0";
                }
                int input;
                try {
                    input = Integer.parseInt(in);
                } catch (Exception e) {
                    input = 0;
                }

                if (input > Integer.parseInt(availableQty.getText().toString()) || input < 0) {
                    reportedQtyField.setError("Value cannot be greater than qty needed");
                    Log.e("error:", "actualQty > Request Qty");
                    View focusView = null;
                    focusView = reportedQtyField;
                } else {
                    reportedQty = input;
                }
            }
        });

        reportedQtyField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!(reportedQtyField.hasFocus())) {
                    String in = reportedQtyField.getText().toString();
                    Log.i("actual qty:", in);
                    if (in == null || in == "") {
                        in = "0";
                    }
                    int input;
                    try {
                        input = Integer.parseInt(in);
                    } catch (Exception e) {
                        input = 0;
                    }

                    if (input > Integer.parseInt(availableQty.getText().toString()) || input < 0) {
                        reportedQtyField.setError("Value cannot be greater than qty needed");
                        Log.e("error:", "actualQty > Request Qty");
                        View focusView = null;
                        focusView = reportedQtyField;
                    } else {
                        reportedQty = input;
                    }
                }
            }
        });

        return  view;

    }

    @Override
    public void onClick(View v){
        if(reportedQty == 0) {
            reportedQty = Integer.parseInt(reportedQtyField.getText().toString());
        }

        JSONAdjustmentDetail mReportItem = new JSONAdjustmentDetail();
        mReportItem.setItemID(itemCode.getText().toString());
        mReportItem.setQuantity(reportedQty);
        mReportItem.setReason(reasonSelected);
        mReportItem.setRemark(remark.getText().toString());
        mReportItem.setPrice(itemPrice);

        //save items temporarily to list of items to report together to generate adjustment voucher.
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity().getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(mReportItem);
        Set<String> jsonArray = appSharedPrefs.getStringSet("ReportItemList", new HashSet<String>());
        Log.i("SharedPref-json array:", jsonArray.toString());
        if(jsonArray.size() > 0) {
            String[] reportItems = jsonArray.toArray(new String[jsonArray.size()]);
            List<JSONAdjustmentDetail> reportItemList = new ArrayList<JSONAdjustmentDetail>();
            for(int i=0; i<reportItems.length; i++) {
                JSONAdjustmentDetail obj = gson.fromJson(reportItems[i], JSONAdjustmentDetail.class);
                reportItemList.add(obj);
            }
            reportItems[reportItems.length] = json;
        }
        else {
            jsonArray.add(json);
        }
        Log.i("NEW json array:", jsonArray.toString());
        prefsEditor.putStringSet("ReportItemList", jsonArray);
        prefsEditor.commit();

        ReportItemListFragment fragment = new ReportItemListFragment();
        android.support.v4.app.FragmentTransaction fragTran = getFragmentManager().beginTransaction();
        fragTran.replace(R.id.frame, fragment).addToBackStack("TAG").commit();

        Toast.makeText(ReportItemFragment.this.getActivity(), "Added to adjustment voucher list", Toast.LENGTH_SHORT).show();
    }


}

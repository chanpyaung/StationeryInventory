package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import team5.ad.sa40.stationeryinventory.Model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportItemFragment extends android.support.v4.app.Fragment {


    @Bind(R.id.itemCode) TextView itemCode;
    @Bind(R.id.itemName) TextView itemName;
    @Bind(R.id.qty) TextView availableQty;
    @Bind(R.id.reOrderLvl) TextView reOrderLvl;
    @Bind(R.id.reported_qty) EditText reportedQty;
    @Bind(R.id.reasonSpinner) Spinner reasonSpinner;
    @Bind(R.id.remark_text) EditText remark;
    @Bind(R.id.add2Adj) Button addtoAdjustment;


    private List<Item> mitemList;
    private String[] reasons = {"DAMAGE", "GIFT", "BROKEN", "MISSING"};
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

        mitemList = Item.initializeData();
        //mitemList = CategoryItem.getAllCategoryItems();

        if(args != null){
            String itemID = args.getString("ITEMCODE");
            for(Item i : mitemList){
                if(i.getItemID() == itemID){
                    itemCode.setText(i.getItemID());
                    itemName.setText(i.getItemName());
                    availableQty.setText(i.getStock());
                    reOrderLvl.setText(i.getRoLvl());
                }
            }
        }

        //load spinner
        ArrayAdapter<String> FiltersAdapter = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_item,reasons);
        FiltersAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        reasonSpinner.setAdapter(FiltersAdapter);



        return  view;

    }

    @OnClick(R.id.add2Adj) void addtoAdjustment(){
        Toast.makeText(ReportItemFragment.this.getActivity(), "ADDED to ADJUSTMENT VOUCHER", Toast.LENGTH_SHORT).show();
    }


}

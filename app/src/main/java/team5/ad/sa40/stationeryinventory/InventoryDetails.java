package team5.ad.sa40.stationeryinventory;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.ItemPrice;
import team5.ad.sa40.stationeryinventory.Model.Supplier;


public class InventoryDetails extends android.support.v4.app.Fragment {

    private Item item;
    private String itemID = "";
    private List<Supplier> supplierList;
    private List<String> supplierNum = new ArrayList<String>();
    public String[] categories;
    private String[] filters = {"View All","Low Stock","Available"};
    public List<Item> allInv;

    //item details
    @Bind(R.id.inv_detail_image) ImageView itemImage;
    @Bind(R.id.inv_detail_itemID) TextView itemIDField;
    @Bind(R.id.inv_detail_itemName) TextView itemNameField;
    @Bind(R.id.inv_detail_category) TextView itemCategoryField;
    @Bind(R.id.inv_detail_bin) TextView itemBinField;
    @Bind(R.id.inv_detail_uom) TextView itemUOMField;
    @Bind(R.id.inv_detail_status) TextView itemStatusField;
    @Bind(R.id.inv_detail_stockQty) TextView itemStockQtyField;
    @Bind(R.id.inv_detail_roLvl) TextView itemROLvlField;
    @Bind(R.id.inv_detail_roQty) TextView itemROQtyField;

    //supplier details
    @Bind(R.id.inv_detail_supplier1) TextView supplier1Field;
    @Bind(R.id.inv_detail_supplier2) TextView supplier2Field;
    @Bind(R.id.inv_detail_supplier3) TextView supplier3Field;
    @Bind(R.id.inv_detail_price1) TextView itemPrice1Field;
    @Bind(R.id.inv_detail_price2) TextView itemPrice2Field;
    @Bind(R.id.inv_detail_price3) TextView itemPrice3Field;

    //buttons:
    @Bind(R.id.stockCardBtn) Button viewStockCardBtn;
    @Bind(R.id.reportItemBtn) Button reportItemBtn;
    @Bind(R.id.callBtn1) ImageButton callSupplier1Btn;
    @Bind(R.id.callBtn2) ImageButton callSupplier2Btn;
    @Bind(R.id.callBtn3) ImageButton callSupplier3Btn;

    public InventoryDetails() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.fragment_inventory_details, container, false);
        ButterKnife.bind(this, view);
        Setup s = new Setup();
        CategoryItem ci = new CategoryItem();
        categories = CategoryItem.categories;
        allInv = new ArrayList<Item>();
        allInv = Item.initializeData();

        if (getArguments() != null) {
            Log.i("arguments: ", getArguments().toString());
            itemID = getArguments().getString("ItemID");
        }

        for(int i=0; i<allInv.size(); i++) {
            if(allInv.get(i).getItemID() == itemID) {
                item = allInv.get(i);
                Log.i("Item details of:",item.getItemID().toString());
                Log.i("Item stock:",Integer.toString(item.getStock()));
            }
        }

        //populate item detail fields
        itemIDField.setText(item.getItemID());
        itemNameField.setText(item.getItemName());
        String category = categories[item.getItemCatID()];
        itemCategoryField.setText(category);
        itemBinField.setText(item.getBin());
        itemUOMField.setText(item.getUOM());
        itemStockQtyField.setText(Integer.toString(item.getStock()));
        itemROLvlField.setText(Integer.toString(item.getRoLvl()));
        itemROQtyField.setText(Integer.toString(item.getRoQty()));
        itemImage.setImageResource(R.drawable.logo_200);

        if(item.getStock()<item.getRoLvl()) {
            itemStatusField.setText("Low");
            itemStatusField.setTextColor(getResources().getColor(R.color.PrimaryInvertedColor));
        }

        //populate supplier info
        supplierList = Supplier.initializeData();
        List<ItemPrice> itemPrices= item.getItemPriceList();
        itemPrice1Field.setText(formatPrice(Double.parseDouble(itemPrices.get(0).get("Price").toString())));
        supplier1Field.setText(itemPrices.get(0).get("SupplierID").toString());
        itemPrice2Field.setText(formatPrice(Double.parseDouble(itemPrices.get(1).get("Price").toString())));
        supplier2Field.setText(itemPrices.get(1).get("SupplierID").toString());
        itemPrice3Field.setText(formatPrice(Double.parseDouble(itemPrices.get(2).get("Price").toString())));
        supplier3Field.setText(itemPrices.get(2).get("SupplierID").toString());
        for(int i=0; i<supplierList.size(); i++) {
            for(int j=0; j<itemPrices.size(); j++) {
                if (supplierList.get(i).get("SupplierID") == itemPrices.get(j).get("SupplierID")){
                    supplierNum.add(supplierList.get(i).get("Phone").toString());
                }
            }
        }

        //set buttons on click listener
        viewStockCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ItemID", item.getItemID());
                Log.i("selected itemID: ", item.getItemID());

                InventoryStockCard fragment = new InventoryStockCard();
                fragment.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
            }
        });
        reportItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("ItemID", item.getItemID());
                Log.i("selected itemID: ", item.getItemID());
                /*
                ReportItemFragment fragment = new ReportItemFragment();
                fragment.setArguments(args);
                android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();*/
            }
        });
        callSupplier1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(0));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });
        callSupplier2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(1));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });
        callSupplier3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(2));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });


        return view;
    }

    public String formatPrice(Double input) {
        String s = "";
        DecimalFormat formatter = new DecimalFormat("###.00");
        s = "$" + formatter.format(input);
        return s;
    }

}

package team5.ad.sa40.stationeryinventory;


import android.content.ClipData;
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
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.InventoryAPI;
import team5.ad.sa40.stationeryinventory.API.SupplierAPI;
import team5.ad.sa40.stationeryinventory.Model.Item;
import team5.ad.sa40.stationeryinventory.Model.ItemPrice;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONItemPrice;
import team5.ad.sa40.stationeryinventory.Model.JSONStockCard;
import team5.ad.sa40.stationeryinventory.Model.JSONSupplier;
import team5.ad.sa40.stationeryinventory.Model.Supplier;


public class InventoryDetails extends android.support.v4.app.Fragment {

    public JSONItem item;
    private String itemID = "";
    public List<JSONItemPrice> itemPrices;
    private HashMap<String,String> supplierNum = new HashMap<String,String>();
    public String[] categories;
    private List<TextView> ItemPriceFields;
    private List<TextView> SupplierFields;

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
        categories = CategoryItem.categories;
        itemID = item.getItemID();
        ItemPriceFields = new ArrayList<TextView>();
        ItemPriceFields.add(itemPrice1Field);
        ItemPriceFields.add(itemPrice2Field);
        ItemPriceFields.add(itemPrice3Field);
        SupplierFields = new ArrayList<TextView>();
        SupplierFields.add(supplier1Field);
        SupplierFields.add(supplier2Field);
        SupplierFields.add(supplier3Field);

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

        //get price & supplier info
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
        InventoryAPI invAPI = restAdapter.create(InventoryAPI.class);
        invAPI.getItemPrice(itemID, new Callback<List<JSONItemPrice>>() {
            @Override
            public void success(List<JSONItemPrice> jsonItems, Response response) {
                Log.i("Response: ", response.getBody().toString());
                System.out.println("Response Status " + response.getStatus());
                if (jsonItems.size() > 0) {
                    Log.i("Result :", jsonItems.toString());
                    Log.i("First item: ", jsonItems.get(0).getItemID().toString());
                    System.out.println("SIZE:::::" + InvListAdapter.mJSONItems.size());
                }
                itemPrices = jsonItems;

                //populate price info
                for(int i=0; i<itemPrices.size() && i<3; i++) {
                    if(itemPrices.get(i) != null) {
                        ItemPriceFields.get(i).setText(formatPrice(Double.parseDouble(String.valueOf(itemPrices.get(i).getPrice()))));
                        SupplierFields.get(i).setText(itemPrices.get(i).getSupplierID());
                    }
                    else {
                        ItemPriceFields.get(i).setText("$0.00");
                        SupplierFields.get(i).setText("");
                    }
                }

                //populate supplier phone no.
                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                SupplierAPI supAPI = restAdapter.create(SupplierAPI.class);

                for(int j=0;j<itemPrices.size();j++) {
                    supAPI.getSupplierDetails(itemPrices.get(j).getSupplierID(), new Callback<JSONSupplier>() {
                        @Override
                        public void success(JSONSupplier jsonItem, Response response) {
                            if(jsonItem != null) {
                                Log.i("Result :", jsonItem.toString());
                                Log.i("supplier detail: ", jsonItem.getSupplierID().toString());
                                Log.i("Response: ", response.getBody().toString());
                                System.out.println("Response Status " + response.getStatus());
                            }
                            supplierNum.put(jsonItem.getSupplierID(), String.valueOf(jsonItem.getPhone()));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.i("Error: ", error.toString());
                        }
                    });
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("Error: ", error.toString());
            }
        });

        //set buttons on click listener
        viewStockCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                InventoryAPI invAPI = restAdapter.create(InventoryAPI.class);
                invAPI.getStockCard(itemID, new Callback<List<JSONStockCard>>() {
                    @Override
                    public void success(List<JSONStockCard> jsonItems, Response response) {
                        Log.i("Response: ", response.getBody().toString());
                        System.out.println("Response Status " + response.getStatus());
                        if (jsonItems.size() > 0) {
                            Log.i("Result :", jsonItems.toString());
                            Log.i("First item: ", jsonItems.get(0).getItemID().toString());
                            System.out.println("SIZE:::::" + InvListAdapter.mJSONItems.size());
                        }

                        SCListAdapter.mJSONStockCard = jsonItems;

                        Bundle args = new Bundle();
                        args.putString("ItemID", item.getItemID());
                        Log.i("selected itemID: ", item.getItemID());
                        args.putString("ItemName", item.getItemName());
                        args.putInt("StockQty", item.getStock());
                        args.putInt("ROLvl", item.getRoLvl());

                        InventoryStockCard fragment = new InventoryStockCard();
                        fragment.setArguments(args);
                        android.support.v4.app.FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("INVENTORYDETAILS1 TAG").commit();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("Error: ", error.toString());
                    }
                });
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
                fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("INVENTORYDETAILS2 TAG").commit();
                */
            }
        });
        callSupplier1Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(supplier1Field.getText()));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });
        callSupplier2Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(supplier2Field.getText()));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });
        callSupplier3Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:"+ supplierNum.get(supplier3Field.getText()));
                Intent i = new Intent(Intent.ACTION_DIAL, uri);
                startActivity(i);
            }
        });


        return view;
    }

    public String formatPrice(Double input) {
        String s = "";
        DecimalFormat formatter = new DecimalFormat("###0.00");
        s = "$" + formatter.format(input);
        return s;
    }

}

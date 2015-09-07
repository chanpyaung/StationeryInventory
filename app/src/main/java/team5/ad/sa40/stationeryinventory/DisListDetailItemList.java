package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Item;


/**
 * A simple {@link Fragment} subclass.
 */
public class DisListDetailItemList extends android.support.v4.app.Fragment {


    @Bind(R.id.item_table)TableLayout detail_table;
    public DisListDetailItemList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_dis_list_detail_item_list, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = this.getArguments();
        ArrayList<Item> items = (ArrayList<Item>)bundle.getSerializable("items");
        //table codes
        TableRow tr_head = new TableRow(this.getActivity());
        tr_head.setId(+10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this.getActivity());
        label_date.setId(+20);
        label_date.setText("Description");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(5, 5, 5, 5);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_weight_kg = new TextView(this.getActivity());
        label_weight_kg.setId(+21);// define id that must be unique
        label_weight_kg.setText("Quantity"); // set the text for the header
        label_weight_kg.setGravity(Gravity.CENTER);
        label_weight_kg.setTextColor(Color.WHITE); // set the color
        label_weight_kg.setPadding(5, 5, 5, 5); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        detail_table.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));


        for (int z=1; z<items.size()+1; z++){

            TableRow tr = new TableRow(this.getActivity());
            tr.setId(+(100+z));
            tr.setBackgroundColor(Color.LTGRAY);
            tr.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

            //Create two columns to add as table data
            // Create a TextView to add date
            TextView ladelName = new TextView(this.getActivity());
            ladelName.setId(+(200 + z));
            //ladelName.setTextColor(Color.WHITE);
            ladelName.setText(items.get(z - 1).getItemName());
            ladelName.setPadding(2, 0, 5, 0);
            tr.addView(ladelName);

            TextView labelQty = new TextView(this.getActivity());
            labelQty.setId(+(300+z));
            labelQty.setGravity(Gravity.CENTER);
            //labelQty.setTextColor(Color.WHITE);
            labelQty.setText(String.valueOf(items.get(z-1).getStock()));
            tr.addView(labelQty);

            // finally add this to the table row
            detail_table.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }


        return v;
    }

}

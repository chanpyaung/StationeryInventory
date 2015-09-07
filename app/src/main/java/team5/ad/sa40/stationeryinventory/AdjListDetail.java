package team5.ad.sa40.stationeryinventory;


import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.Adjustment;
import team5.ad.sa40.stationeryinventory.Model.AdjustmentDetail;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdjListDetail extends android.support.v4.app.Fragment {


    @Bind(R.id.txtAdjID) TextView txtAdjID;
    @Bind(R.id.txtAdjDate) TextView txtAdjDate;
    @Bind(R.id.txtEmpName) TextView txtEmpName;
    @Bind(R.id.txtEmpID) TextView txtEmpID;
    @Bind(R.id.txtStatus) TextView txtStatus;
    @Bind(R.id.txtTotalCost) TextView txtTotalCost;
    @Bind(R.id.item_table)TableLayout detail_table;

    public AdjListDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Bundle bundle = this.getArguments();
        Adjustment dis = (Adjustment) bundle.getSerializable("adjustment");
        View v;
        TextView txtStatusLabel;
        TextView txtAppName;
        TextView txtAppID;
        Button btnReject;
        Button btnApprove;

        if(dis.getStatus() != "Pending"){
            v = inflater.inflate(R.layout.fragment_adj_list_detail, container, false);
            ButterKnife.bind(this, v);
            txtAppID = (TextView) v.findViewById(R.id.txtAppID);
            txtAppName = (TextView) v.findViewById(R.id.txtAppName);
            txtStatusLabel = (TextView) v.findViewById(R.id.txtStatusLabel);
            txtAppID.setText(String.valueOf(dis.getApprovedBy()));
        }
        else{
            v = inflater.inflate(R.layout.fragment_adj_list_detail2, container, false);
            ButterKnife.bind(this, v);

            btnApprove = (Button) v.findViewById(R.id.btnApprove);
            btnReject = (Button) v.findViewById(R.id.btnReject);

            btnApprove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            btnReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        Log.i("Dis id is ", String.valueOf(dis.getAdjustmentID()));
        txtAdjID.setText(dis.getAdjustmentID());
        String string_date = Setup.parseDateToString(dis.getDate());
        txtStatus.setText(dis.getStatus());
        txtAdjDate.setText(string_date);
        txtEmpID.setText(String.valueOf(dis.getReportedBy()));
        txtTotalCost.setText(String.valueOf(dis.getTotalAmount()));

        ArrayList<AdjustmentDetail> items = getAllAdjustmentDetail();
        //table codes
        TableRow tr_head = new TableRow(this.getActivity());
        tr_head.setId(+10);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableLayout.LayoutParams(
                TableLayout.LayoutParams.FILL_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT));

        TextView label_date = new TextView(this.getActivity());
        label_date.setId(+20);
        label_date.setText("ItemCode");
        label_date.setTextColor(Color.WHITE);
        label_date.setPadding(3, 3, 3, 3);
        tr_head.addView(label_date);// add the column to the table row here

        TextView label_weight_kg = new TextView(this.getActivity());
        label_weight_kg.setId(+21);// define id that must be unique
        label_weight_kg.setText("Adjustment"); // set the text for the header
        label_weight_kg.setTextColor(Color.WHITE); // set the color
        label_weight_kg.setPadding(3, 3, 3, 3); // set the padding (if required)
        tr_head.addView(label_weight_kg); // add the column to the table row here

        TextView label_price = new TextView(this.getActivity());
        label_price.setId(+22);// define id that must be unique
        label_price.setText("Price"); // set the text for the header
        label_price.setTextColor(Color.WHITE); // set the color
        label_price.setPadding(3, 3, 3, 3); // set the padding (if required)
        tr_head.addView(label_price); // add the column to the table row here

        TextView label_reason = new TextView(this.getActivity());
        label_reason.setId(+23);// define id that must be unique
        label_reason.setText("Reason"); // set the text for the header
        label_reason.setTextColor(Color.WHITE); // set the color
        label_reason.setPadding(3, 3, 3, 3); // set the padding (if required)
        tr_head.addView(label_reason); // add the column to the table row here

        TextView label_remark = new TextView(this.getActivity());
        label_remark.setId(+24);// define id that must be unique
        label_remark.setText("Details"); // set the text for the header
        label_remark.setTextColor(Color.WHITE); // set the color
        label_remark.setPadding(3, 3, 3, 3); // set the padding (if required)
        tr_head.addView(label_remark); // add the column to the table row here


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
            ladelName.setText(items.get(z - 1).getItemID());
            //ladelName.setPadding(2, 0, 5, 0);
            tr.addView(ladelName);

            TextView labelQty = new TextView(this.getActivity());
            labelQty.setId(+(300 + z));
            labelQty.setGravity(Gravity.CENTER);
            //labelQty.setTextColor(Color.WHITE);
            labelQty.setText(String.valueOf(items.get(z - 1).getQuantity()));
            tr.addView(labelQty);

            TextView labelPrice = new TextView(this.getActivity());
            labelPrice.setId(+(400 + z));
            //labelQty.setTextColor(Color.WHITE);
            labelPrice.setText(String.valueOf(items.get(z - 1).getPrice()));
            tr.addView(labelPrice);

            TextView labelReason = new TextView(this.getActivity());
            labelReason.setId(+(500 + z));
            //labelQty.setTextColor(Color.WHITE);
            labelReason.setText(String.valueOf(items.get(z - 1).getReason()));
            tr.addView(labelReason);

            TextView labelRemark = new TextView(this.getActivity());
            labelRemark.setId(+(500 + z));
            //labelQty.setTextColor(Color.WHITE);
            labelRemark.setText(String.valueOf(items.get(z - 1).getRemark()));
            tr.addView(labelRemark);

            // finally add this to the table row
            detail_table.addView(tr, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.FILL_PARENT,
                    TableLayout.LayoutParams.WRAP_CONTENT));

        }

        return v;
    }

    public ArrayList<AdjustmentDetail> getAllAdjustmentDetail(){
        ArrayList<AdjustmentDetail> details = new ArrayList<>();

        for (int i=1; i< 6; i++){
            AdjustmentDetail adj = new AdjustmentDetail(i, "AD"+i, "Item"+i, 10 +i, i*(i+10), "Broken", "Remark");
            details.add(adj);
        }
        return details;
    }

}

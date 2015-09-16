package team5.ad.sa40.stationeryinventory.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.R;

import static team5.ad.sa40.stationeryinventory.Model.Constants.ITEM_CODE;
import static team5.ad.sa40.stationeryinventory.Model.Constants.ITEM_NAME;

/**
 * Created by johnmajor on 9/7/15.
 */
public class ReportItemAdapter extends BaseAdapter implements View.OnClickListener {

    @Bind(R.id.textItemCode) TextView itemCode;
    @Bind(R.id.textItemName) TextView itemName;
    @Bind(R.id.btnReportItem) Button btnReport;

    ReportItemAdapter.OnItemClickListener mItemClickListener;

    public ArrayList<HashMap<String, String>> list;
    Activity activity;
    FragmentActivity fragActivity;


    public ReportItemAdapter(FragmentActivity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity = activity;
        fragActivity = activity;
        this.list = list;
    }

    @Override
    public int getCount(){
        return  list.size();
    }

    @Override
    public Object getItem(int position){
        return list.get(position);
    }

    @Override
    public long getItemId(int position){
        return  0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = fragActivity.getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.reportitem_search_result, null);
            ButterKnife.bind(this, convertView);
        }
        HashMap<String, String> map = list.get(position);
        itemCode.setText(map.get(ITEM_CODE));
        itemName.setText(map.get(ITEM_NAME));
        btnReport.setOnClickListener(this);
        return  convertView;
    }

    @Override
    public void onClick(View v) {
        int i = 1;

        Log.i("Click "+i, "times");
        i++;
        Toast.makeText(ReportItemAdapter.this.activity, "Hello from btnReport", Toast.LENGTH_SHORT).show();
        ReportItemFragment rpItemFrag = new ReportItemFragment();
        Bundle args = new Bundle();
        args.putString("ITEMCODE", itemCode.getText().toString());
        args.putString("ITEMPRICE","");
        rpItemFrag.setArguments(args);
        android.support.v4.app.FragmentTransaction fragTran = fragActivity.getSupportFragmentManager().beginTransaction();
        fragTran.replace(R.id.frame, rpItemFrag).commit();

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}

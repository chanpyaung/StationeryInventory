package team5.ad.sa40.stationeryinventory;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

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


    public ReportItemAdapter(Activity activity, ArrayList<HashMap<String, String>> list){
        super();
        this.activity = activity;
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

        LayoutInflater inflater = activity.getLayoutInflater();
        if(convertView == null){
            convertView = inflater.inflate(R.layout.reportitem_search_result, null);
            ButterKnife.bind(this, convertView);
        }
        HashMap<String, String> map = list.get(position);
        itemCode.setText(map.get(ITEM_CODE));
        itemCode.setText(map.get(ITEM_NAME));
        convertView.setOnClickListener(this);
        return  convertView;
    }

    @Override
    public void onClick(View v) {

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }

}

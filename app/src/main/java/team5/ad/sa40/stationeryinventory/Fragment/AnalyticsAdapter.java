package team5.ad.sa40.stationeryinventory.Fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import team5.ad.sa40.stationeryinventory.Model.JSONReport;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * Created by johnmajor on 9/16/15.
 */
public class AnalyticsAdapter extends RecyclerView.Adapter<AnalyticsAdapter.ViewHolder>{

    static List<JSONReport> mReports;
    OnItemClickListener mItemClickListener;

    public AnalyticsAdapter() {
        super();
        if(mReports.size()>0){
            List<JSONReport> temp = mReports;
            Collections.sort(temp);
            mReports = temp;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        int count = i+1;
        JSONReport jsonReport = mReports.get(i);
        String idDisplay = "Report #";
        int id = jsonReport.getReportID();
        if(id<10) {
            idDisplay += "000" + String.valueOf(id);
        }
        else if(id<100) {
            idDisplay += "00" + String.valueOf(id);
        }
        else if(id<1000) {
            idDisplay += "0" + String.valueOf(id);
        }
        else if(id<10000) {
            idDisplay += String.valueOf(id);
        }
        viewHolder.reportItemCode.setText(idDisplay);
        viewHolder.reportDate.setText(Setup.parseJSONDateToString(jsonReport.getDate()));
        viewHolder.reportTitle.setText(jsonReport.getTitle());
    }

    @Override
    public int getItemCount() {

        return mReports.size();
    }

    public JSONReport removeItem(int position) {
        final JSONReport item = mReports.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, JSONReport item) {
        mReports.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final JSONReport model = mReports.remove(fromPosition);
        mReports.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<JSONReport> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<JSONReport> newItems) {
        for (int i = mReports.size() - 1; i >= 0; i--) {
            final JSONReport model = mReports.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<JSONReport> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final JSONReport item = newModels.get(i);
            if (!mReports.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<JSONReport> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final JSONReport model = newModels.get(toPosition);
            final int fromPosition = mReports.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.reportDate)
        TextView reportDate;
        @Bind(R.id.reportItemCode)TextView reportItemCode;
        @Bind(R.id.reportTitle)TextView reportTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mItemClickListener!=null){
                mItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener (final OnItemClickListener mItemClickListener){
        this.mItemClickListener = mItemClickListener;
    }


}

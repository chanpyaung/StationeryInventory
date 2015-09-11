package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.JSONAdjustmentDetail;

public class ReportItemListAdapter extends RecyclerView.Adapter<ReportItemListAdapter.ViewHolder> {

    public static List<JSONAdjustmentDetail> mJSONAdjustmentDetail;
    String[] mSCID;
    ReportItemListAdapter.OnItemClickListener mItemClickListener;

    public ReportItemListAdapter(List<JSONAdjustmentDetail> items){
        super();
        mJSONAdjustmentDetail = new ArrayList<JSONAdjustmentDetail>();
        if(items.size() > 0) {
            mJSONAdjustmentDetail = items;
        }
        mSCID = new String[mJSONAdjustmentDetail.size()];
        Log.i("Size of list", String.valueOf(mJSONAdjustmentDetail.size()));
        Setup s = new Setup();
        if(mJSONAdjustmentDetail.size() > 0) {
            for (int i = 0; i < mJSONAdjustmentDetail.size(); i++) {
                String temp = String.valueOf(mJSONAdjustmentDetail.get(i).getAdjSN());
                mSCID[i] = temp;
            }
            Log.i("First of string ", mSCID[0]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.report_item_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        JSONAdjustmentDetail item = mJSONAdjustmentDetail.get(i);
        viewHolder.itemCode.setText(item.getItemID());
        viewHolder.itemQty.setText(formatChangeInQty(item.getQuantity()));
        viewHolder.itemReason.setText(item.getReason());
    }

    @Override
    public int getItemCount() {

        return mJSONAdjustmentDetail.size();
    }

    public JSONAdjustmentDetail removeItem(int position) {
        final JSONAdjustmentDetail item = mJSONAdjustmentDetail.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, JSONAdjustmentDetail item) {
        mJSONAdjustmentDetail.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final JSONAdjustmentDetail model = mJSONAdjustmentDetail.remove(fromPosition);
        mJSONAdjustmentDetail.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<JSONAdjustmentDetail> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<JSONAdjustmentDetail> newItems) {
        for (int i = mJSONAdjustmentDetail.size() - 1; i >= 0; i--) {
            final JSONAdjustmentDetail model = mJSONAdjustmentDetail.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<JSONAdjustmentDetail> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final JSONAdjustmentDetail item = newModels.get(i);
            if (!mJSONAdjustmentDetail.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<JSONAdjustmentDetail> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final JSONAdjustmentDetail model = newModels.get(toPosition);
            final int fromPosition = mJSONAdjustmentDetail.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemCode;
        public TextView itemQty;
        public TextView itemReason;

        public ViewHolder(final View itemView){
            super(itemView);
            itemCode = (TextView) itemView.findViewById(R.id.reportItemCode);
            itemQty = (TextView) itemView.findViewById(R.id.reportQty);
            itemReason = (TextView) itemView.findViewById(R.id.reportReason);

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

    public String formatChangeInQty (int qty) {
        String s = "";
        if(qty < 0) {
            s = String.valueOf(qty);
        }
        else {
            s = "+" + String.valueOf(qty);
        }
        return s;
    }
}

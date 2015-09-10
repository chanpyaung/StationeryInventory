package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Adjustment;

/**
 * Created by student on 6/9/15.
 */
public class AdjListGridAdapter extends RecyclerView.Adapter<AdjListGridAdapter.ViewHolder>{
    List<Adjustment> mAdjustments;
    AdjListGridAdapter.OnItemClickListener mItemClickListener;

    public AdjListGridAdapter(String type){
        super();
        mAdjustments = new ArrayList<>();
        //Place for adding the JSON list if elseif else
        if (type != "Search") {
            mAdjustments = Adjustment.getAllAdjustments();
        }

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ret_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Adjustment disItem = mAdjustments.get(i);
        viewHolder.adjNoLabel.setText("Voucher #");
        viewHolder.noNeedLabel1.setText("");
        viewHolder.noNeedLable2.setText("");
        viewHolder.adjNo.setText(String.valueOf(disItem.getAdjustmentID()));
        String string_date = Setup.parseDateToString(disItem.getDate());
        viewHolder.adjDate.setText(string_date);
        viewHolder.adjStatus.setText(disItem.getStatus());

    }
    @Override
    public int getItemCount() {

        return mAdjustments.size();
    }

    public Adjustment removeItem(int position) {
        final Adjustment item = mAdjustments.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Adjustment item) {
        mAdjustments.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Adjustment model = mAdjustments.remove(fromPosition);
        mAdjustments.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Adjustment> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Adjustment> newItems) {
        for (int i = mAdjustments.size() - 1; i >= 0; i--) {
            final Adjustment model = mAdjustments.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Adjustment> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Adjustment item = newModels.get(i);
            if (!mAdjustments.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Adjustment> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Adjustment model = newModels.get(toPosition);
            final int fromPosition = mAdjustments.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView adjNo;
        public TextView adjDate;
        public TextView adjStatus;
        public TextView adjNoLabel;
        public TextView noNeedLabel1;
        public TextView noNeedLable2;

        public ViewHolder(View itemView){
            super(itemView);

            noNeedLabel1 = (TextView) itemView.findViewById(R.id.ret_ifEmpIDNeeded);
            noNeedLable2 = (TextView) itemView.findViewById(R.id.reportItemName);
            adjNoLabel = (TextView) itemView.findViewById(R.id.reportItemCode);
            adjNo = (TextView) itemView.findViewById(R.id.ret_id);
            adjDate = (TextView) itemView.findViewById(R.id.ret_date);
            adjStatus = (TextView) itemView.findViewById(R.id.ret_status);
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

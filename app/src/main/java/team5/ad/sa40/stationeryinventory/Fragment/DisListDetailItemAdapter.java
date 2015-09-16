package team5.ad.sa40.stationeryinventory.Fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.CustomDisbursementDetail;
import team5.ad.sa40.stationeryinventory.R;

/**
 * Created by student on 10/9/15.
 */
public class DisListDetailItemAdapter extends RecyclerView.Adapter<DisListDetailItemAdapter.ViewHolder> {

    List<CustomDisbursementDetail> mItems;
    DisListDetailItemAdapter.OnItemClickListener mItemClickListener;

    public DisListDetailItemAdapter(ArrayList<CustomDisbursementDetail> items){
        super();
        mItems = items;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.disitem_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CustomDisbursementDetail disItem = mItems.get(i);
        Log.e("size in bind custom", String.valueOf(mItems.size()));
        viewHolder.txtItemName.setText(mItems.get(i).getItemName());
        viewHolder.txtQty.setText(String.valueOf(mItems.get(i).getItemQty()));
    }
    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public CustomDisbursementDetail removeItem(int position) {
        final CustomDisbursementDetail item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, CustomDisbursementDetail item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final CustomDisbursementDetail model = mItems.remove(fromPosition);
        mItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<CustomDisbursementDetail> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<CustomDisbursementDetail> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final CustomDisbursementDetail model = mItems.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CustomDisbursementDetail> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final CustomDisbursementDetail item = newModels.get(i);
            if (!mItems.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CustomDisbursementDetail> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CustomDisbursementDetail model = newModels.get(toPosition);
            final int fromPosition = mItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtItemName;
        public TextView txtQty;

        public ViewHolder(View itemView){
            super(itemView);

            txtItemName = (TextView)itemView.findViewById(R.id.itemName);
            txtQty = (TextView) itemView.findViewById(R.id.itemQty);
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

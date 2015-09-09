package team5.ad.sa40.stationeryinventory;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.JSONItem;
import team5.ad.sa40.stationeryinventory.Model.JSONJSONItem;

public class InvListAdapter extends RecyclerView.Adapter<InvListAdapter.ViewHolder> {

    List<JSONJSONItem> mJSONItems;
    String[] retId;
    InvListAdapter.OnJSONItemClickListener mJSONItemClickListener;

    public InvListAdapter(List<JSONJSONItem> JSONItemList){
        super();
        mJSONItems = JSONItemList;
        //mJSONItems = CategoryJSONItem.getAllCategoryJSONItems();
        Collections.sort(mJSONItems);
        retId = new String[mJSONItems.size()];
        Log.i("Size of list", String.valueOf(mJSONItems.size()));
        Setup s = new Setup();
        for(int i = 0; i < mJSONItems.size(); i++){
            String temp = String.valueOf(mJSONItems.get(i).getJSONItemID());
            retId[i] = temp;
        }
        Log.i("First of string ", retId[0]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.inv_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        JSONItem JSONItem = mJSONItems.get(i);

        viewHolder.JSONItemID.setText(JSONItem.getItemID());
        viewHolder.JSONItemName.setText(JSONItem.getItemName());

        //format status:
        if(JSONItem.getStock() < JSONItem.getRoLvl()) {
            viewHolder.JSONItemStatus.setText("Low");
            viewHolder.JSONItemStatus.setTextColor(Color.RED);
        }

        //format category icon:
        int categoryID = JSONItem.getJSONItemCatID();
        switch(categoryID) {
            case 1:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv1);
                break;
            case 2:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv2);
                break;
            case 3:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv3);
                break;
            case 4:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv4);
                break;
            case 5:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv5);
                break;
            case 6:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv6);
                break;
            case 7:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv7);
                break;
            case 8:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv8);
                break;
            case 9:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv9);
                break;
            case 10:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv10);
                break;
            case 11:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv11);
                break;
            case 12:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv12);
                break;
            case 13:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv13);
                break;
            case 14:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv14);
                break;
            case 15:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv15);
                break;
            case 16:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv16);
                break;
            case 17:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv17);
                break;
            case 18:
                viewHolder.JSONItemCatIcon.setImageResource(R.drawable.icon_inv18);
                break;

        }
    }

    @Override
    public int getJSONItemCount() {

        return mJSONItems.size();
    }

    public JSONItem removeJSONItem(int position) {
        final JSONItem JSONItem = mJSONItems.remove(position);
        notifyJSONItemRemoved(position);
        return JSONItem;
    }

    public void addJSONItem(int position, JSONItem JSONItem) {
        mJSONItems.add(position, JSONItem);
        notifyJSONItemInserted(position);
    }

    public void moveJSONItem(int fromPosition, int toPosition) {
        final JSONItem model = mJSONItems.remove(fromPosition);
        mJSONItems.add(toPosition, model);
        notifyJSONItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<JSONItem> JSONItems) {
        applyAndAnimateRemovals(JSONItems);
        applyAndAnimateAdditions(JSONItems);
        applyAndAnimateMovedJSONItems(JSONItems);
    }

    private void applyAndAnimateRemovals(List<JSONItem> newJSONItems) {
        for (int i = mJSONItems.size() - 1; i >= 0; i--) {
            final JSONItem model = mJSONItems.get(i);
            if (!newJSONItems.contains(model)) {
                removeJSONItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<JSONItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final JSONItem JSONItem = newModels.get(i);
            if (!mJSONItems.contains(JSONItem)) {
                addJSONItem(i, JSONItem);
            }
        }
    }

    private void applyAndAnimateMovedJSONItems(List<JSONItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final JSONItem model = newModels.get(toPosition);
            final int fromPosition = mJSONItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveJSONItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView JSONItemID;
        public TextView JSONItemName;
        public TextView JSONItemStatus;
        public ImageView JSONItemCatIcon;

        public ViewHolder(View JSONItemView){
            super(JSONItemView);
            JSONItemID = (TextView) JSONItemView.findViewById(R.id.inv_JSONItemCode);
            JSONItemName = (TextView) JSONItemView.findViewById(R.id.inv_JSONItemName);
            JSONItemStatus = (TextView) JSONItemView.findViewById(R.id.inv_status);
            JSONItemCatIcon = (ImageView) JSONItemView.findViewById(R.id.inv_icon);

            JSONItemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mJSONItemClickListener!=null){
                mJSONItemClickListener.onJSONItemClick(v, getAdapterPosition());

            }
        }
    }
    public interface OnJSONItemClickListener {
        public void onJSONItemClick(View view, int position);
    }

    public void SetOnJSONItemClickListener (final OnJSONItemClickListener mJSONItemClickListener){
        this.mJSONItemClickListener = mJSONItemClickListener;
    }
}

package team5.ad.sa40.stationeryinventory;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.JSONItem;

public class InvListAdapter extends RecyclerView.Adapter<InvListAdapter.ViewHolder> {

    public static List<JSONItem> mJSONItems;
    String[] retId;
    InvListAdapter.OnItemClickListener mItemClickListener;

    public InvListAdapter(List<JSONItem> JSONItemList){
        super();
        mJSONItems = new ArrayList<JSONItem>();
        if(JSONItemList.size() >0) {
            mJSONItems = JSONItemList;
            Collections.sort(mJSONItems);
        }
        else {
            mJSONItems = new ArrayList<JSONItem>();
        }
        retId = new String[mJSONItems.size()];
        if(mJSONItems.size() >0) {
            Log.i("Size of list", String.valueOf(mJSONItems.size()));
            Setup s = new Setup();
            for (int i = 0; i < mJSONItems.size(); i++) {
                String temp = String.valueOf(mJSONItems.get(i).getItemID());
                retId[i] = temp;
            }
            Log.i("First of string ", retId[0]);
        }
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
        int categoryID = JSONItem.getItemCatID();
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
    public int getItemCount() {

        return mJSONItems.size();
    }

    public JSONItem removeJSONItem(int position) {
        final JSONItem JSONItem = mJSONItems.remove(position);
        notifyItemRemoved(position);
        return JSONItem;
    }

    public void addJSONItem(int position, JSONItem JSONItem) {
        mJSONItems.add(position, JSONItem);
        notifyItemInserted(position);
    }

    public void moveJSONItem(int fromPosition, int toPosition) {
        final JSONItem model = mJSONItems.remove(fromPosition);
        mJSONItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
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

        public ViewHolder(View ItemView){
            super(ItemView);
            JSONItemID = (TextView) ItemView.findViewById(R.id.reportItemCode);
            JSONItemName = (TextView) ItemView.findViewById(R.id.reportItemName);
            JSONItemStatus = (TextView) ItemView.findViewById(R.id.reportReason);
            JSONItemCatIcon = (ImageView) ItemView.findViewById(R.id.inv_icon);

            ItemView.setOnClickListener(this);
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

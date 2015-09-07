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

import team5.ad.sa40.stationeryinventory.Model.Item;

public class InvListAdapter extends RecyclerView.Adapter<InvListAdapter.ViewHolder> {

    List<Item> mItems;
    String[] retId;
    InvListAdapter.OnItemClickListener mItemClickListener;

    public InvListAdapter(){
        super();
        //mItems = Item.getAllItems();
        mItems = Item.initializeData();
        Collections.sort(mItems);
        retId = new String[mItems.size()];
        Log.i("Size of list", String.valueOf(mItems.size()));
        Setup s = new Setup();
        for(int i = 0; i < mItems.size(); i++){
            String temp = String.valueOf(mItems.get(i).getItemID());
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
        Item item = mItems.get(i);

        viewHolder.itemID.setText(item.getItemID());
        viewHolder.itemName.setText(item.getItemName());

        //format status:
        if(item.getStock() < item.getRoLvl()) {
            viewHolder.itemStatus.setText("Low");
            viewHolder.itemStatus.setTextColor(Color.RED);
        }

        //format category icon:
        int categoryID = item.getItemCatID();
        switch(categoryID) {
            case 1:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv1);
                break;
            case 2:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv2);
                break;
            case 3:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv3);
                break;
            case 4:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv4);
                break;
            case 5:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv5);
                break;
            case 6:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv6);
                break;
            case 7:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv7);
                break;
            case 8:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv8);
                break;
            case 9:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv9);
                break;
            case 10:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv10);
                break;
            case 11:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv11);
                break;
            case 12:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv12);
                break;
            case 13:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv13);
                break;
            case 14:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv14);
                break;
            case 15:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv15);
                break;
            case 16:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv16);
                break;
            case 17:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv17);
                break;
            case 18:
                viewHolder.itemCatIcon.setImageResource(R.drawable.icon_inv18);
                break;

        }
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public Item removeItem(int position) {
        final Item item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Item item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Item model = mItems.remove(fromPosition);
        mItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Item> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Item> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final Item model = mItems.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Item> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Item item = newModels.get(i);
            if (!mItems.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Item> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Item model = newModels.get(toPosition);
            final int fromPosition = mItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemID;
        public TextView itemName;
        public TextView itemStatus;
        public ImageView itemCatIcon;

        public ViewHolder(View itemView){
            super(itemView);
            itemID = (TextView) itemView.findViewById(R.id.inv_itemCode);
            itemName = (TextView) itemView.findViewById(R.id.inv_itemName);
            itemStatus = (TextView) itemView.findViewById(R.id.inv_status);
            itemCatIcon = (ImageView) itemView.findViewById(R.id.inv_icon);

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

package team5.ad.sa40.stationeryinventory;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Retrieval;

public class RetListAdapter extends RecyclerView.Adapter<RetListAdapter.ViewHolder> {

    List<Retrieval> mRetrievals;
    String[] retId;
    RetListAdapter.OnItemClickListener mItemClickListener;

    public RetListAdapter(){
        super();
        //mRetrievals = Retrieval.getAllRetrievals();
        mRetrievals = Retrieval.initializeData();
        retId = new String[mRetrievals.size()];
        Log.i("Size of list", String.valueOf(mRetrievals.size()));
        Setup s = new Setup();
        for(int i = 0; i < mRetrievals.size(); i++){
            String temp = String.valueOf(mRetrievals.get(i).getRetID());
            retId[i] = temp;
        }
        Log.i("First of string ", retId[0]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ret_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Retrieval ret = mRetrievals.get(i);

        //set display of retrieval id to "0004" for eg.
        String idDisplay = "";
        int id = ret.getRetID();
        if(id<10) {
            idDisplay = "000" + String.valueOf(id);
        }
        else if(id<100) {
            idDisplay = "00" + String.valueOf(id);
        }
        else if(id<1000) {
            idDisplay = "0" + String.valueOf(id);
        }
        else if(id<10000) {
            idDisplay = String.valueOf(id);
        }
        viewHolder.retId.setText(idDisplay);

        //format to date only:
        viewHolder.retDate.setText(Setup.parseDateToString(ret.getDate()));

        viewHolder.retStatus.setText(ret.getStatus());
        if(viewHolder.retStatus.getText().equals("Pending")) {
            viewHolder.retStatus.setTextColor(Color.RED);
        }
        viewHolder.retIfEmpIDNeeded.setText("");
        viewHolder.retEmpID.setText("");
    }

    @Override
    public int getItemCount() {

        return mRetrievals.size();
    }

    public Retrieval removeItem(int position) {
        final Retrieval item = mRetrievals.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Retrieval item) {
        mRetrievals.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Retrieval model = mRetrievals.remove(fromPosition);
        mRetrievals.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Retrieval> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Retrieval> newItems) {
        for (int i = mRetrievals.size() - 1; i >= 0; i--) {
            final Retrieval model = mRetrievals.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Retrieval> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Retrieval item = newModels.get(i);
            if (!mRetrievals.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Retrieval> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Retrieval model = newModels.get(toPosition);
            final int fromPosition = mRetrievals.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView retId;
        public TextView retDate;
        public TextView retStatus;
        public TextView retIfEmpIDNeeded;
        public TextView retEmpID;

        public ViewHolder(View itemView){
            super(itemView);
            retId = (TextView) itemView.findViewById(R.id.ret_id);
            retDate = (TextView) itemView.findViewById(R.id.ret_date);
            retStatus = (TextView) itemView.findViewById(R.id.ret_status);
            retIfEmpIDNeeded = (TextView) itemView.findViewById(R.id.ret_ifEmpIDPresent);
            retEmpID = (TextView) itemView.findViewById(R.id.ret_EmpID);

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

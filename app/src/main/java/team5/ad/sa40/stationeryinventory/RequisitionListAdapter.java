package team5.ad.sa40.stationeryinventory;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Requisition;

/**
 * Created by johnmajor on 9/5/15.
 */
public class RequisitionListAdapter extends RecyclerView.Adapter<RequisitionListAdapter.ViewHolder> {

    List<Requisition> mRequisitions;
    String[] retId;
    RequisitionListAdapter.OnItemClickListener mItemClickListener;

    public RequisitionListAdapter(){
        super();
        //mRequisitions = Requisition.getAllRetrievals();
        mRequisitions = Requisition.initializeData();
        retId = new String[mRequisitions.size()];
        Log.i("Size of list", String.valueOf(mRequisitions.size()));
        Setup s = new Setup();
        for(int i = 0; i < mRequisitions.size(); i++){
            String temp = String.valueOf(mRequisitions.get(i).getRetID());
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
        Requisition req = mRequisitions.get(i);

        //set display of retrieval id to "0004" for eg.
        String idDisplay = "";
        int id = req.getReqID();
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
        viewHolder.retHeader.setText("Requisition #");
        viewHolder.retId.setText(idDisplay);

        //format to date only:
        viewHolder.retDate.setText(Setup.parseDateToString(req.getDate()));

        if (req.getStatusID()==1){
            viewHolder.retStatus.setText("Approved");
        }
        else if (req.getStatusID()==2){
            viewHolder.retStatus.setText("Rejected");
        }
        else if (req.getStatusID()==3){
            viewHolder.retStatus.setText("Processed");
        }
        else if (req.getStatusID()==4){
            viewHolder.retStatus.setText("Collected");
        }

        if(viewHolder.retStatus.getText().equals("Rejected")) {
            viewHolder.retStatus.setTextColor(Color.RED);
        }
        else if(viewHolder.retStatus.getText().equals("Collected")){
            viewHolder.retStatus.setTextColor(Color.BLUE);
        }
        else if(viewHolder.retStatus.getText().equals("Processed")){
            viewHolder.retStatus.setTextColor(Color.MAGENTA);
        }
        viewHolder.retIfEmpIDNeeded.setText("");
        viewHolder.retEmpID.setText(req.getDeptID());
    }

    @Override
    public int getItemCount() {

        return mRequisitions.size();
    }

    public Requisition removeItem(int position) {
        final Requisition item = mRequisitions.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Requisition item) {
        mRequisitions.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Requisition model = mRequisitions.remove(fromPosition);
        mRequisitions.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Requisition> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Requisition> newItems) {
        for (int i = mRequisitions.size() - 1; i >= 0; i--) {
            final Requisition model = mRequisitions.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Requisition> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Requisition item = mRequisitions.get(i);
            if (!mRequisitions.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Requisition> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Requisition model = newModels.get(toPosition);
            final int fromPosition = mRequisitions.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView retHeader;
        public TextView retId;
        public TextView retDate;
        public TextView retStatus;
        public TextView retIfEmpIDNeeded;
        public TextView retEmpID;

        public ViewHolder(View itemView){
            super(itemView);
            retHeader = (TextView) itemView.findViewById(R.id.sc_date);
            retId = (TextView) itemView.findViewById(R.id.ret_id);
            retDate = (TextView) itemView.findViewById(R.id.ret_date);
            retStatus = (TextView) itemView.findViewById(R.id.ret_status);
            retIfEmpIDNeeded = (TextView) itemView.findViewById(R.id.ret_ifEmpIDNeeded);
            retEmpID = (TextView) itemView.findViewById(R.id.inv_itemName);

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

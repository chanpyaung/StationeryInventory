package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Requisition;
import team5.ad.sa40.stationeryinventory.Model.RequisitionDetail;

/**
 * Created by johnmajor on 9/6/15.
 */
public class RequisitionFormAdapter extends RecyclerView.Adapter<RequisitionFormAdapter.ViewHolder> {

    List<RequisitionDetail> mRequisitionDetails;
    String[] reqDetailID;
    String mReqForms = "";
    RequisitionFormAdapter.OnItemClickListener mItemClickListener;
    String mStatus = "";

    public RequisitionFormAdapter(int ReqID, String status){
        super();
        mRequisitionDetails = Requisition.initializeDataDetails(ReqID);
        mStatus = status;
        List<Requisition> req = Requisition.initializeData();
        for(int i=0; i<req.size(); i++) {
            String idDisplay = "";
            int id = req.get(i).getReqID();
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
            if(i == (req.size() - 1)) {
                mReqForms = mReqForms + idDisplay;
            }
            else {
                mReqForms = mReqForms + idDisplay + ", ";
            }
        }
        Requisition r = new Requisition();
        //r.getRetrieval(RetID);
        //mReqForms = r.getReqForms();
        //mRetrievalDetails = r.getItems();

        reqDetailID = new String[mRequisitionDetails.size()];
        Log.i("Size of list", String.valueOf(mRequisitionDetails.size()));
        Setup s = new Setup();
        for(int i = 0; i < mRequisitionDetails.size(); i++){
            String temp = String.valueOf(mRequisitionDetails.get(i).get("itemID"));
            reqDetailID[i] = temp;
        }
        Log.i("First of string ", reqDetailID[0]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.requisition_detail_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RequisitionDetail ret = mRequisitionDetails.get(i);
        viewHolder.itemId.setText(ret.get("itemID").toString());
        viewHolder.itemName.setText(ret.get("itemName").toString());
        viewHolder.requestQty.setText(ret.get("RequestQty").toString());
        if(mStatus == "Retrieved") {
            viewHolder.actualQty.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {

        return mRequisitionDetails.size();
    }

    public RequisitionDetail removeItem(int position) {
        final RequisitionDetail item = mRequisitionDetails.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, RequisitionDetail item) {
        mRequisitionDetails.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final RequisitionDetail model = mRequisitionDetails.remove(fromPosition);
        mRequisitionDetails.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<RequisitionDetail> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<RequisitionDetail> newItems) {
        for (int i = mRequisitionDetails.size() - 1; i >= 0; i--) {
            final RequisitionDetail model = mRequisitionDetails.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<RequisitionDetail> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final RequisitionDetail item = newModels.get(i);
            if (!mRequisitionDetails.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<RequisitionDetail> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final RequisitionDetail model = newModels.get(toPosition);
            final int fromPosition = mRequisitionDetails.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemId;
        public TextView itemName;
        public TextView requestQty;
        public TextView actualQty;

        public ViewHolder(final View itemView){
            super(itemView);
            itemId = (TextView) itemView.findViewById(R.id.reportItemCode);
            itemName = (TextView) itemView.findViewById(R.id.itemName);
            requestQty = (TextView) itemView.findViewById(R.id.qty_needed);
            actualQty = (TextView) itemView.findViewById(R.id.qty_actual);

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

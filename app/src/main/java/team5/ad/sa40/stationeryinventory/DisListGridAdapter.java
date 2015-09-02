package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by student on 2/9/15.
 */
public class DisListGridAdapter extends RecyclerView.Adapter<DisListGridAdapter.ViewHolder> {

    List<Disbursement> mdisbursements;
    String[] disNo;
    DisListGridAdapter.OnItemClickListener mItemClickListener;

    public DisListGridAdapter(){
        super();
        mdisbursements = Disbursement.getAllDisbursement();
        Log.i("Size of list", String.valueOf(mdisbursements.size()));
        disNo = new String[mdisbursements.size()];
        for(int i = 0; i < mdisbursements.size(); i++){
            String temp = String.valueOf(mdisbursements.get(i).getDisbursementId());
            disNo[i] = temp;
        }
        Log.i("First of list ", mdisbursements.get(0).getDisbursementStatus());
        Log.i("First of string ", disNo[0]);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dis_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Disbursement disItem = mdisbursements.get(i);
        viewHolder.disNo.setText(String.valueOf(disItem.getDisbursementId()));
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy");
        String string_date = format.format(disItem.getDisbursementDate());
        viewHolder.disDate.setText(string_date);
        viewHolder.disStatus.setText(disItem.getDisbursementStatus());
    }
    @Override
    public int getItemCount() {

        return mdisbursements.size();
    }

    public Disbursement removeItem(int position) {
        final Disbursement item = mdisbursements.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Disbursement item) {
        mdisbursements.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Disbursement model = mdisbursements.remove(fromPosition);
        mdisbursements.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Disbursement> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Disbursement> newItems) {
        for (int i = mdisbursements.size() - 1; i >= 0; i--) {
            final Disbursement model = mdisbursements.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Disbursement> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Disbursement item = newModels.get(i);
            if (!mdisbursements.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Disbursement> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Disbursement model = newModels.get(toPosition);
            final int fromPosition = mdisbursements.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView disNo;
        public TextView disDate;
        public TextView disStatus;

        public ViewHolder(View itemView){
            super(itemView);
            disNo = (TextView) itemView.findViewById(R.id.dis_no_value);
            disDate = (TextView) itemView.findViewById(R.id.dis_date_value);
            disStatus = (TextView) itemView.findViewById(R.id.dis_status_value);
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

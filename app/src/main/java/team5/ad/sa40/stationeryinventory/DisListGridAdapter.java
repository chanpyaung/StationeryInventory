package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Disbursement;

/**
 * Created by student on 2/9/15.
 */
public class DisListGridAdapter extends RecyclerView.Adapter<DisListGridAdapter.ViewHolder> {

    List<Disbursement> mdisbursements;
    DisListGridAdapter.OnItemClickListener mItemClickListener;
    String user_dept;

    public DisListGridAdapter(String part){
        super();
        user_dept = part;
        mdisbursements = new ArrayList<>();
        //Place for adding the JSON list if elseif else
        if (user_dept != "Search"){
            mdisbursements = Disbursement.getAllDisbursement();
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;
        if(user_dept == "Dept") {
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.ret_row, viewGroup, false);
        }
        else{
            v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.dis_row, viewGroup, false);
        }
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Disbursement disItem = mdisbursements.get(i);
        if (user_dept == "Dept") {
            viewHolder.disNoLabel.setText("Disbursement #");
            viewHolder.noNeedLabel1.setText("");
            viewHolder.noNeedLable2.setText("");
            viewHolder.disNo.setText(String.valueOf(disItem.getDisbursementId()));
            String string_date = Setup.parseDateToString(disItem.getDisbursementDate());
            viewHolder.disDate.setText(string_date);
            viewHolder.disStatus.setText(disItem.getDisbursementStatus());
        }
        else{
            viewHolder.txtdisNo.setText(String.valueOf(disItem.getDisbursementId()));
            String string_date = Setup.parseDateToString(disItem.getDisbursementDate());
            viewHolder.txtdisDate.setText(string_date);
            viewHolder.txtdisStatus.setText(disItem.getDisbursementStatus());
            viewHolder.txtDept.setText(disItem.getDeptID());
            viewHolder.txtCol.setText("Stationary");
        }

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
        public TextView disNoLabel;
        public TextView noNeedLabel1;
        public TextView noNeedLable2;

        public TextView txtdisNo;
        public TextView txtdisDate;
        public TextView txtdisStatus;
        public TextView txtDept;
        public TextView txtCol;

        public ViewHolder(View itemView){
            super(itemView);

            if(user_dept == "Dept"){
                noNeedLabel1 = (TextView) itemView.findViewById(R.id.ret_ifEmpIDNeeded);
                noNeedLable2 = (TextView) itemView.findViewById(R.id.ret_EmpID);
                disNoLabel = (TextView) itemView.findViewById(R.id.inv_itemCode);
                disNo = (TextView) itemView.findViewById(R.id.ret_id);
                disDate = (TextView) itemView.findViewById(R.id.ret_date);
                disStatus = (TextView) itemView.findViewById(R.id.ret_status);
            }
            else{
                txtdisNo = (TextView) itemView.findViewById(R.id.txtDisNo);
                txtdisDate = (TextView) itemView.findViewById(R.id.txtDisDate);
                txtDept = (TextView) itemView.findViewById(R.id.txtDept);
                txtdisStatus = (TextView) itemView.findViewById(R.id.txtStatus);
                txtCol = (TextView) itemView.findViewById(R.id.txtCP);
            }
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

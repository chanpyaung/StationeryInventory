package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Delegate;

/**
 * Created by student on 7/9/15.
 */
public class DelegateGridAdapter extends RecyclerView.Adapter<DelegateGridAdapter.ViewHolder> {
    List<Delegate> mDelegates;
    DelegateGridAdapter.OnItemClickListener mItemClickListener;
    public DelegateGridAdapter(){
        super();
        mDelegates = new ArrayList<>();
        //Place for adding the JSON list if elseif else
        mDelegates = Delegate.getAllDelegation();

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v;

        v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.dis_row, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Delegate del = mDelegates.get(i);
        viewHolder.txtNoNeedLabel1.setText("");
        viewHolder.txtNoNeedLabel2.setText("");
        viewHolder.txtNoNeedLabel3.setText("");
        viewHolder.txtEmpLabel.setText("Employee Name:");
        viewHolder.txtEmpName.setText(del.getEmpName());
        viewHolder.txtStartLabel.setText("Start Date:");
        viewHolder.txtStartDate.setText(Setup.parseDateToString(del.getStartDate()));
        viewHolder.txtEndLabel.setText("End Date:");
        viewHolder.txtEndDate.setText(Setup.parseDateToString(del.getEndDate()));
//        Adjustment disItem = mAdjustments.get(i);
//        viewHolder.adjNoLabel.setText("Voucher #");
//        viewHolder.noNeedLabel1.setText("");
//        viewHolder.noNeedLable2.setText("");
//        viewHolder.adjNo.setText(String.valueOf(disItem.getAdjustmentID()));
//        String string_date = Setup.parseDateToString(disItem.getDate());
//        viewHolder.adjDate.setText(string_date);
//        viewHolder.adjStatus.setText(disItem.getStatus());

    }
    @Override
    public int getItemCount() {

        return mDelegates.size();
    }

    public Delegate removeItem(int position) {
        final Delegate item = mDelegates.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Delegate item) {
        mDelegates.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Delegate model = mDelegates.remove(fromPosition);
        mDelegates.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Delegate> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Delegate> newItems) {
        for (int i = mDelegates.size() - 1; i >= 0; i--) {
            final Delegate model = mDelegates.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Delegate> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Delegate item = newModels.get(i);
            if (!mDelegates.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Delegate> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Delegate model = newModels.get(toPosition);
            final int fromPosition = mDelegates.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView txtNoNeedLabel2;
        public TextView txtEmpLabel;
        public TextView txtEmpName;
        public TextView txtNoNeedLabel1;
        public TextView txtNoNeedLabel3;
        public TextView txtEndDate;
        public TextView txtStartDate;
        public TextView txtStartLabel;
        public TextView txtEndLabel;

        public ViewHolder(View itemView){
            super(itemView);

            txtNoNeedLabel3 = (TextView) itemView.findViewById(R.id.textView);
            txtNoNeedLabel2 = (TextView) itemView.findViewById(R.id.txtDisNo);
            txtEmpLabel = (TextView) itemView.findViewById(R.id.txtDisDate);
            txtEndDate = (TextView) itemView.findViewById(R.id.txtDept);
            txtNoNeedLabel1 = (TextView) itemView.findViewById(R.id.txtStatus);
            txtStartDate = (TextView) itemView.findViewById(R.id.txtCP);
            txtStartLabel = (TextView) itemView.findViewById(R.id.ret_date);
            txtEndLabel = (TextView) itemView.findViewById(R.id.ret_EmpID);
            txtEmpName = (TextView) itemView.findViewById(R.id.ret_id);

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

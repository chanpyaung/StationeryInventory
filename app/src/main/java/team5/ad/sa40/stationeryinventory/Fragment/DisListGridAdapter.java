package team5.ad.sa40.stationeryinventory.Fragment;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.JSONCollectionPoint;
import team5.ad.sa40.stationeryinventory.Model.JSONDisbursement;
import team5.ad.sa40.stationeryinventory.R;
import team5.ad.sa40.stationeryinventory.Utilities.Setup;

/**
 * Created by student on 2/9/15.
 */
public class DisListGridAdapter extends RecyclerView.Adapter<DisListGridAdapter.ViewHolder> {

    List<JSONDisbursement> mdisbursements;
    DisListGridAdapter.OnItemClickListener mItemClickListener;
    String user_dept;
    List<JSONCollectionPoint> mCollectionPoints;

    public DisListGridAdapter(String part, List<JSONDisbursement> dis, List<JSONCollectionPoint> col){
        super();
        user_dept = part;
        mdisbursements = dis;
        mCollectionPoints = col;

        if(dis.size() > 0){
            List<JSONDisbursement> temp = dis;
            Collections.sort(temp);
            dis = temp;
        }
        //Place for adding the JSON list if elseif else
        if (user_dept != "Search"){

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
        JSONDisbursement disItem = mdisbursements.get(i);
        Log.e("size in bind", String.valueOf(mdisbursements.size()));
        if (user_dept == "Dept") {
            viewHolder.disNoLabel.setText("Disbursement #");
            viewHolder.noNeedLabel1.setText("");
            viewHolder.noNeedLable2.setText("");
            viewHolder.disNo.setText(String.valueOf(disItem.getDisID()));
            String string_date = Setup.parseJSONDateToString(disItem.getDate());
            viewHolder.disDate.setText(string_date);
            viewHolder.disStatus.setText(disItem.getStatus());
//            if(disItem.getStatus().equals("PENDING")){
//                viewHolder.disStatus.setTextColor(Color.RED);
//            }
        }
        else{
            viewHolder.txtdisNo.setText(String.valueOf(disItem.getDisID()));
            String string_date = Setup.parseJSONDateToString(disItem.getDate());
            viewHolder.txtdisDate.setText(string_date);
            viewHolder.txtdisStatus.setText(disItem.getStatus());
            viewHolder.txtDept.setText(disItem.getDeptID());
            viewHolder.txtCol.setText(getColPtNameFromDis(disItem.getCPID()));
//            if(disItem.getStatus().equals("PENDING")){
//                viewHolder.disStatus.setTextColor(Color.RED);
//            }
        }

    }
    @Override
    public int getItemCount() {

        return mdisbursements.size();
    }

    public JSONDisbursement removeItem(int position) {
        final JSONDisbursement item = mdisbursements.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, JSONDisbursement item) {
        mdisbursements.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final JSONDisbursement model = mdisbursements.remove(fromPosition);
        mdisbursements.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<JSONDisbursement> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<JSONDisbursement> newItems) {
        for (int i = mdisbursements.size() - 1; i >= 0; i--) {
            final JSONDisbursement model = mdisbursements.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<JSONDisbursement> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final JSONDisbursement item = newModels.get(i);
            if (!mdisbursements.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<JSONDisbursement> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final JSONDisbursement model = newModels.get(toPosition);
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
                noNeedLable2 = (TextView) itemView.findViewById(R.id.reportItemName);
                disNoLabel = (TextView) itemView.findViewById(R.id.reportItemCode);
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

    public String getColPtNameFromDis(int colID){
        String selected_colPt = "";
        for (int i = 0; i < mCollectionPoints.size(); i++){
            if (colID == mCollectionPoints.get(i).getCPID()){
                selected_colPt = mCollectionPoints.get(i).getCPName();
            }
        }
        return selected_colPt;
    }
}

package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.StockCard;

public class SCListAdapter extends RecyclerView.Adapter<SCListAdapter.ViewHolder> {

    List<StockCard> mStockCard;
    String[] mSCID;
    SCListAdapter.OnItemClickListener mItemClickListener;

    public SCListAdapter(String itemID){
        super();
        mStockCard = StockCard.initializeData(itemID);

        mSCID = new String[mStockCard.size()];
        Log.i("Size of list", String.valueOf(mStockCard.size()));
        Setup s = new Setup();
        for(int i = 0; i < mStockCard.size(); i++){
            String temp = String.valueOf(mStockCard.get(i).getStockCardSN());
            mSCID[i] = temp;
        }
        Log.i("First of string ", mSCID[0]);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.stockcard_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        StockCard sc = mStockCard.get(i);
        viewHolder.SCDate.setText(Setup.parseDateToString(sc.getDate()));
        viewHolder.SCDesc.setText(sc.getDescription().toString());
        viewHolder.SCChange.setText(String.format("%d", sc.getQty()));
        viewHolder.SCBalance.setText(Integer.toString(sc.getBalance()));
    }

    @Override
    public int getItemCount() {

        return mStockCard.size();
    }

    public StockCard removeItem(int position) {
        final StockCard item = mStockCard.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, StockCard item) {
        mStockCard.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final StockCard model = mStockCard.remove(fromPosition);
        mStockCard.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<StockCard> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<StockCard> newItems) {
        for (int i = mStockCard.size() - 1; i >= 0; i--) {
            final StockCard model = mStockCard.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<StockCard> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final StockCard item = newModels.get(i);
            if (!mStockCard.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<StockCard> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final StockCard model = newModels.get(toPosition);
            final int fromPosition = mStockCard.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView SCDate;
        public TextView SCDesc;
        public TextView SCChange;
        public TextView SCBalance;

        public ViewHolder(final View itemView){
            super(itemView);
            SCDate = (TextView) itemView.findViewById(R.id.sc_date);
            SCDesc = (TextView) itemView.findViewById(R.id.sc_trx);
            SCChange = (TextView) itemView.findViewById(R.id.sc_change);
            SCBalance = (TextView) itemView.findViewById(R.id.sc_balance);

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

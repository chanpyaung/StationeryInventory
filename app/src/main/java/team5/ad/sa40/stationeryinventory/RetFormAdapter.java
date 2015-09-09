package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import team5.ad.sa40.stationeryinventory.Model.Requisition;
import team5.ad.sa40.stationeryinventory.Model.Retrieval;
import team5.ad.sa40.stationeryinventory.Model.RetrievalDetail;

public class RetFormAdapter extends RecyclerView.Adapter<RetFormAdapter.ViewHolder> {

    List<RetrievalDetail> mRetrievalDetails;
    Retrieval mRetrieval;
    String[] retDetailId;
    String mReqForms = "";
    RetFormAdapter.OnItemClickListener mItemClickListener;
    String mStatus = "";
    List<String> req;

    public RetFormAdapter(int RetID, String status){
        super();
        mRetrieval = Retrieval.getRetrieval(RetID, status);
        Log.i("mRetrieval: ", Integer.toString(mRetrieval.getRetID()));
        mRetrievalDetails = new ArrayList<RetrievalDetail>();
        if(mRetrieval.getItems() != null) {
            mRetrievalDetails = mRetrieval.getItems();
        }
        Log.i("mRetrievalDetails: ", mRetrieval.getItems().toString());
        mStatus = mRetrieval.getStatus();
        Log.i("mStatus: ", mRetrieval.getStatus());

        if(mRetrieval.getReqForms() != null) {
            req = mRetrieval.getReqForms();
            Log.i("req: ", mRetrieval.getReqForms().toString());
            for (int i = 0; i < req.size(); i++) {
                String idDisplay = "";
                int id = Integer.parseInt(req.get(i));
                if (id < 10) {
                    idDisplay = "000" + String.valueOf(id);
                } else if (id < 100) {
                    idDisplay = "00" + String.valueOf(id);
                } else if (id < 1000) {
                    idDisplay = "0" + String.valueOf(id);
                } else if (id < 10000) {
                    idDisplay = String.valueOf(id);
                }
                if (i == (req.size() - 1)) {
                    mReqForms = mReqForms + idDisplay;
                } else {
                    mReqForms = mReqForms + idDisplay + ", ";
                }
            }
        }
        else {
            req = new ArrayList<String>();
        }

        if(mRetrievalDetails.size()>0) {
            retDetailId = new String[mRetrievalDetails.size()];
            Log.i("Size of list", String.valueOf(mRetrievalDetails.size()));
            Setup s = new Setup();
            for (int i = 0; i < mRetrievalDetails.size(); i++) {
                String temp = String.valueOf(mRetrievalDetails.get(i).get("itemID"));
                retDetailId[i] = temp;
            }
            Log.i("First of string ", retDetailId[0]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.ret_detail_row, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        RetrievalDetail ret = mRetrievalDetails.get(i);
        viewHolder.itemId.setText(ret.get("itemID").toString());
        viewHolder.itemName.setText(ret.get("itemName").toString());
        viewHolder.requestQty.setText(ret.get("RequestQty").toString());
        viewHolder.bin.setText(ret.get("Bin").toString());
        viewHolder.actualQty.setText(ret.get("ActualQty").toString());
        if(mStatus.equals("RETRIEVED")) {
            viewHolder.actualQty.setEnabled(false);
        }
    }

    @Override
    public int getItemCount() {

        return mRetrievalDetails.size();
    }

    public RetrievalDetail removeItem(int position) {
        final RetrievalDetail item = mRetrievalDetails.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, RetrievalDetail item) {
        mRetrievalDetails.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final RetrievalDetail model = mRetrievalDetails.remove(fromPosition);
        mRetrievalDetails.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<RetrievalDetail> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<RetrievalDetail> newItems) {
        for (int i = mRetrievalDetails.size() - 1; i >= 0; i--) {
            final RetrievalDetail model = mRetrievalDetails.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<RetrievalDetail> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final RetrievalDetail item = newModels.get(i);
            if (!mRetrievalDetails.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<RetrievalDetail> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final RetrievalDetail model = newModels.get(toPosition);
            final int fromPosition = mRetrievalDetails.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public class ViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView itemId;
        public TextView itemName;
        public TextView bin;
        public TextView requestQty;
        public EditText actualQty;

        public ViewHolder(final View itemView){
            super(itemView);
            itemId = (TextView) itemView.findViewById(R.id.inv_itemCode);
            itemName = (TextView) itemView.findViewById(R.id.ret_detail_itemName);
            bin = (TextView) itemView.findViewById(R.id.ret_detail_bin);
            requestQty = (TextView) itemView.findViewById(R.id.ret_detail_reqId);
            actualQty = (EditText) itemView.findViewById(R.id.ret_detail_actualQty);

            itemView.setOnClickListener(this);
            actualQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!(actualQty.hasFocus())) {
                        String in = actualQty.getText().toString();
                        Log.i("actual qty:", in);
                        if (in == null || in == "") {
                            in = "0";
                        }
                        int input;
                        try {
                            input = Integer.parseInt(in);
                        } catch (Exception e) {
                            input = 0;
                        }

                        if (input > Integer.parseInt(requestQty.getText().toString())) {
                            actualQty.setError("Value cannot be greater than qty needed");
                            Log.e("error:", "actualQty > Request Qty");
                            View focusView = null;
                            focusView = actualQty;
                        } else {
                            for (int i = 0; i < mRetrievalDetails.size(); i++) {
                                if (mRetrievalDetails.get(i).get("itemID").toString() == itemId.getText().toString()) {
                                    mRetrievalDetails.get(i).put("ActualQty", input);
                                }
                            }
                        }
                    }
                }
            });
            actualQty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String in = actualQty.getText().toString();
                    Log.i("actual qty:", in);
                    if (in == null || in == "") {
                        in = "0";
                    }
                    int input;
                    try {
                        input = Integer.parseInt(in);
                    } catch (Exception e) {
                        input = 0;
                    }

                    if (input > Integer.parseInt(requestQty.getText().toString())) {
                        actualQty.setError("Value cannot be greater than qty needed");
                        Log.e("error:", "actualQty > Request Qty");
                        View focusView = null;
                        focusView = actualQty;
                    } else {
                        for (int i = 0; i < mRetrievalDetails.size(); i++) {
                            if (mRetrievalDetails.get(i).get("itemID").toString() == itemId.getText().toString()) {
                                mRetrievalDetails.get(i).put("ActualQty", input);
                            }
                        }
                    }
                }
            });
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

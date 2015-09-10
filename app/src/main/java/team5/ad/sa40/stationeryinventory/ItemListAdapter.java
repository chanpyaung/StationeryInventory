package team5.ad.sa40.stationeryinventory;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.gson.JsonObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import team5.ad.sa40.stationeryinventory.API.RequestCartAPI;
import team5.ad.sa40.stationeryinventory.Model.JSONItem;

/**
 * Created by johnmajor on 9/3/15.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    public static List<JSONItem> myItemlist;
    List<JSONItem> cartItemList;

    OnItemClickListener mItemClickListener;

    public ItemListAdapter() {
        super();
        cartItemList = new ArrayList<JSONItem>();
        myItemlist = CategoryFragment.itemsbyCategory;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        JSONItem mitem = myItemlist.get(i);
        System.out.println("OnBindViewHolder" + myItemlist.get(i));
        viewHolder.itemName.setText(mitem.getItemName());
        viewHolder.uom.setText(mitem.getUOM());
        new DownloadImageTask(viewHolder.itemImage).execute("http://192.168.31.202/img/Item_120/" + mitem.getItemID() + ".jpg");

    }

    @Override
    public int getItemCount() {
        System.out.println("Size "+myItemlist.size());
        return myItemlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView itemImage;
        TextView itemName;
        TextView uom;
        EditText qty;
        int qtyAmt;
        public FloatingActionButton fab;

        public ViewHolder(final View itemView) {
            super(itemView);
            itemImage = (CircleImageView)itemView.findViewById(R.id.item_image);
            itemName = (TextView)itemView.findViewById(R.id.item_Name_text);
            uom = (TextView)itemView.findViewById(R.id.UOM);
            qty = (EditText)itemView.findViewById(R.id.qtyText);
            qty.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.length()>0){

                        qtyAmt = Integer.parseInt(s.toString());
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            fab = (FloatingActionButton)itemView.findViewById(R.id.add_fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //Build JSON to pass
                    int empID = Setup.user.getEmpID();
                    final int qty = qtyAmt;
                    String itemID = myItemlist.get(getAdapterPosition()).getItemID();
                    final JsonObject reqItem = new JsonObject();
                    reqItem.addProperty("EmpID", empID);
                    reqItem.addProperty("ItemID", itemID);
                    reqItem.addProperty("Qty", qty);

                    //retrofit
                    RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Setup.baseurl).build();
                    final RequestCartAPI rqCartAPI = restAdapter.create(RequestCartAPI.class);
                    rqCartAPI.addtoCart(reqItem, new Callback<Boolean>()  {
                        @Override
                        public void success(Boolean aBoolean, Response response) {
                            System.out.println("Retrofit Success " + aBoolean);
                            if (aBoolean=true) {
                                MainActivity.requestCart.add(myItemlist.get(getAdapterPosition()));
                                Toast.makeText(itemView.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                                System.out.println(MainActivity.requestCart.toArray());
                            }
                        }
                        @Override
                        public void failure(RetrofitError error) {
                            Toast.makeText(itemView.getContext(), "Failed to add to request cart.", Toast.LENGTH_SHORT).show();
                            System.out.println("Retrofit failed " + error.toString());
                        }
                    });
                }
            });
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

    public JSONItem removeItem(int position) {
        final JSONItem item = myItemlist.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, JSONItem item) {
        myItemlist.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final JSONItem model = myItemlist.remove(fromPosition);
        myItemlist.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<JSONItem> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<JSONItem> newItems) {
        for (int i = myItemlist.size() - 1; i >= 0; i--) {
            final JSONItem model = myItemlist.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<JSONItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final JSONItem item = newModels.get(i);
            if (!myItemlist.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<JSONItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final JSONItem model = newModels.get(toPosition);
            final int fromPosition = myItemlist.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(CircleImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import team5.ad.sa40.stationeryinventory.Model.Item;

/**
 * Created by johnmajor on 9/3/15.
 */
public class ItemListAdapter extends RecyclerView.Adapter<ItemListAdapter.ViewHolder> {

    Item myItem, myItem1, myItem2, myItem3, myItem4, myItem5, myItem6, myItem7, myItem8, myItem9;
    List<Item> myItemlist;
    List<Item> cartItemList;

    OnItemClickListener mItemClickListener;

    public ItemListAdapter() {
        super();
        myItem = new Item("002211144433EE","Clip A", 1, 30, 50, "pcs", 400, "SHELF3L3");
        myItem1 = new Item("EE123489393","Clip B", 1, 20, 50, "pcs", 200, "SHELF3L3");
        myItem2 = new Item("3332122fAAA","Clip C", 1, 30, 50, "pcs", 30, "SHELF3L3");
        myItem3 = new Item("FE345678902","Clip D", 1, 43, 22, "pcs", 69, "SHELF3L3");
        myItem4 = new Item("BBW2567843CCC","Clip E", 1, 12, 64, "pcs", 77, "SHELF3L3");
        myItem5 = new Item("EEC9876543","Clip F", 1, 30, 50, "pcs", 33, "SHELF3L3");
        myItem6 = new Item("SDFG765434322345","Clip G", 1, 30, 50, "pcs", 22, "SHELF3L3");
        myItem7 = new Item("YHN9876543","Clip H", 1, 59, 100, "pcs", 98, "SHELF3L3");
        myItem8 = new Item("CVH78323456789","Clip I", 1, 33, 50, "pcs", 84, "SHELF3L3");
        myItem9 = new Item("MMM987654234","Clip J", 1, 44, 50, "pcs", 43, "SHELF3L3");
        myItemlist = new ArrayList<Item>();
        cartItemList = new ArrayList<Item>();
        Item[] itemList = new Item[]{myItem, myItem1, myItem2,myItem3,myItem4,myItem5,myItem6,myItem7,myItem8,myItem9};
        for (Item i: itemList){
            myItemlist.add(i);
        }

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

        Item mitem = myItemlist.get(i);
        viewHolder.itemName.setText(mitem.getItemName());
        viewHolder.uom.setText(mitem.getUOM());

    }

    @Override
    public int getItemCount() {
        return myItemlist.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        CircleImageView itemImage;
        TextView itemName;
        TextView uom;
        public FloatingActionButton fab;

        public ViewHolder(View itemView) {
            super(itemView);
            itemImage = (CircleImageView)itemView.findViewById(R.id.item_image);
            itemName = (TextView)itemView.findViewById(R.id.item_Name_text);
            uom = (TextView)itemView.findViewById(R.id.UOM);
            fab = (FloatingActionButton)itemView.findViewById(R.id.add_fab);
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartItemList.add(myItemlist.get(getAdapterPosition()));
                    MainActivity.requestCart.add(myItemlist.get(getAdapterPosition()));
                    Log.i("Cart Item: ", myItemlist.get(getAdapterPosition()).getItemName());
                    Toast.makeText(v.getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
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

    public Item removeItem(int position) {
        final Item item = myItemlist.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, Item item) {
        myItemlist.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Item model = myItemlist.remove(fromPosition);
        myItemlist.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<Item> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<Item> newItems) {
        for (int i = myItemlist.size() - 1; i >= 0; i--) {
            final Item model = myItemlist.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Item> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Item item = newModels.get(i);
            if (!myItemlist.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Item> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Item model = newModels.get(toPosition);
            final int fromPosition = myItemlist.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }
}

package team5.ad.sa40.stationeryinventory;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by johnmajor on 9/1/15.
 */
public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

    List<CategoryItem> mItems;
    String[] categoryNames = new String[]{"Clip", "Envelope", "Eraser", "Exercise", "File", "Pen", "Puncher", "Pad", "Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Transparency","Tray"};
    int[] thumbnail = new int[]{R.drawable.stapler, R.drawable.clip};

    OnItemClickListener mItemClickListener;

    public GridAdapter() {
        super();
        mItems = new ArrayList<CategoryItem>();
        for (String s : categoryNames){
            CategoryItem catItem = new CategoryItem();
            catItem.setCatName(s);
            catItem.setCatThumbnail(thumbnail[0]);
            mItems.add(catItem);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CategoryItem catItem = mItems.get(i);
        viewHolder.catName.setText(catItem.getCatName());
        viewHolder.imgThumbnail.setImageResource(catItem.getCatThumbnail());
    }

    @Override
    public int getItemCount() {

        return mItems.size();
    }

    public CategoryItem removeItem(int position) {
        final CategoryItem item = mItems.remove(position);
        notifyItemRemoved(position);
        return item;
    }

    public void addItem(int position, CategoryItem item) {
        mItems.add(position, item);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final CategoryItem model = mItems.remove(fromPosition);
        mItems.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    public void animateTo(List<CategoryItem> items) {
        applyAndAnimateRemovals(items);
        applyAndAnimateAdditions(items);
        applyAndAnimateMovedItems(items);
    }

    private void applyAndAnimateRemovals(List<CategoryItem> newItems) {
        for (int i = mItems.size() - 1; i >= 0; i--) {
            final CategoryItem model = mItems.get(i);
            if (!newItems.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<CategoryItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final CategoryItem item = newModels.get(i);
            if (!mItems.contains(item)) {
                addItem(i, item);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<CategoryItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final CategoryItem model = newModels.get(toPosition);
            final int fromPosition = mItems.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imgThumbnail;
        public TextView catName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            catName = (TextView)itemView.findViewById(R.id.cat_name);
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

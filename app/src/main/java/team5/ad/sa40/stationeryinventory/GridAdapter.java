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
    String[] categoryNames = new String[]{"Clap", "Envelope", "Eraser", "Exercise", "File", "Pen", "Puncher", "Pad", "Paper","Ruler","Scissors","Tape","Sharpener","Shorthand","Stapler","Tacks","Tparency","Tray"};
    int[] thumbnail = new int[]{R.drawable.stapler, R.drawable.clip};
    public GridAdapter() {
        super();
        mItems = new ArrayList<CategoryItem>();
        for (String s : categoryNames){
            CategoryItem catItem = new CategoryItem();
            catItem.setCatName(s);
            catItem.setCatThumbnail(thumbnail[1]);
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

    class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imgThumbnail;
        public TextView catName;

        public ViewHolder(View itemView) {
            super(itemView);
            imgThumbnail = (ImageView)itemView.findViewById(R.id.img_thumbnail);
            catName = (TextView)itemView.findViewById(R.id.cat_name);
        }
    }


}

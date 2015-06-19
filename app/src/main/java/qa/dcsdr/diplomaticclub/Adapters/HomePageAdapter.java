package qa.dcsdr.diplomaticclub.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import qa.dcsdr.diplomaticclub.Items.HomePageEntry;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/14/2015.
 */
public class HomePageAdapter extends RecyclerView.Adapter<HomePageAdapter.CategoryViewHolder> {

    private final VolleySingleton volleySingleton;
    private final ImageLoader imageLoader;
    List<HomePageEntry> entries;
    Context context;

    public HomePageAdapter(List<HomePageEntry> entries, Context context) {
        this.entries = entries;
        this.context=context;
        volleySingleton= VolleySingleton.getsInstance();
        imageLoader=volleySingleton.getImageLoader();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_category, parent, false);
        CategoryViewHolder holder = new CategoryViewHolder(view);
        return holder;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }
    
    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        holder.categoryTitle.setText(entries.get(position).getCategoryTitle());
        holder.categoryImage.setDefaultImageResId(entries.get(position).getImageId());
//        holder.categoryImage.setImageBitmap(
//                decodeSampledBitmapFromResource(context.getResources(),
//                          entries.get(position).getImageId(), 300, 120));
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView categoryTitle;
        NetworkImageView categoryImage;

        CategoryViewHolder(View itemView) {
            super(itemView);
            categoryTitle = (TextView) itemView.findViewById(R.id.hpCategoryTitle);
            categoryImage = (NetworkImageView) itemView.findViewById(R.id.hpCategoryImage);
        }
    }

}
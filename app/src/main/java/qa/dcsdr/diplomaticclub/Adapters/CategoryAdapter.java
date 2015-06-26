package qa.dcsdr.diplomaticclub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

import qa.dcsdr.diplomaticclub.Activities.DisplayArticleListActivity;
import qa.dcsdr.diplomaticclub.Items.CategoryDictionary;
import qa.dcsdr.diplomaticclub.Items.CategoryEntry;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/13/2015.
 * This is the adapter for the categories that lists all the sub-categories.
 */
public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolderCategories> {

    private LayoutInflater inflater;
    private List<CategoryEntry> data = Collections.emptyList();
    private Context context;
    private final CategoryDictionary categoryDictionary;

    public CategoryAdapter(Context context, List<CategoryEntry> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.categoryDictionary = new CategoryDictionary(context);
    }

    @Override
    public ViewHolderCategories onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.category_card, parent, false);
        return new ViewHolderCategories(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolderCategories holder, final int position) {
        final CategoryEntry current = data.get(position);
        Picasso.with(context).load(current.getCategoryImageId()).placeholder(R.drawable.loading_image).
                error(R.drawable.default_art_image).into(holder.getCategoryImage());
        holder.getCategoryTitle().setText(current.getCategoryTitle());
        LinearLayout ln = holder.getSubCategoryList();
        for (int i = 0; i < current.getSubCategories().size(); i++) {
            TextView textView = new TextView(context);
            textView.setText(current.getSubCategories().get(i));
            textView.setPadding(70, 25, 70, 25);
            textView.setTextColor(context.getResources().getColor(R.color.colorSecondaryText));
            textView.setClickable(true);
            textView.setWidth(ln.getWidth());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            textView.setBackgroundResource(R.drawable.custom_bg);
            final int finalI = i;
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DisplayArticleListActivity.class);
                    intent.putExtra("CAT_TITLE", current.getSubCategories().get(finalI));
                    intent.putExtra(context.getString(R.string.PARENT_CLASS_TAG), context.getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                    String url = categoryDictionary.getUrl(current.getSubCategories().get(finalI));
                    intent.putExtra("URL", url);
                    context.startActivity(intent);
                }
            });
            ln.addView(textView);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolderCategories extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView categoryImage;
        private TextView categoryTitle;
        private Button openSubCategories;
        private LinearLayout subCategoryList;

        public ImageView getCategoryImage() {
            return categoryImage;
        }

        public TextView getCategoryTitle() {
            return categoryTitle;
        }

        public LinearLayout getSubCategoryList() {
            return subCategoryList;
        }

        public ViewHolderCategories(View itemView, int viewType) {
            super(itemView);
            categoryImage = (ImageView) itemView.findViewById(R.id.categoryImage);
            categoryTitle = (TextView) itemView.findViewById(R.id.categoryTitle);
            subCategoryList = (LinearLayout) itemView.findViewById(R.id.subCategoryList);
            openSubCategories = (Button) itemView.findViewById(R.id.rollDownSubCategories);
            openSubCategories.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemView.animate();
            if (this.subCategoryList.getVisibility() == View.VISIBLE) {
                this.subCategoryList.setVisibility(View.GONE);
                this.openSubCategories.setText(context.getString(R.string.EXPAND));
            } else {
                this.subCategoryList.setVisibility(View.VISIBLE);
                this.openSubCategories.setText(context.getString(R.string.COLLAPSE));
            }
        }
    }

}
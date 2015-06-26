package qa.dcsdr.diplomaticclub.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Activities.DisplayArticleListActivity;
import qa.dcsdr.diplomaticclub.Items.Author;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.Ellipsizer;

/**
 * Created by Tamim on 6/11/2015.
 * This is the adapter for displaying the authors.
 */
public class AuthorAdapter extends RecyclerView.Adapter<AuthorAdapter.ArticleViewHolder> {

    private ArrayList<Author> authorList = new ArrayList<>();
    private Context context;
    private ClickListener clickListener;
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private Activity activity;
    private View view;

    public void setAuthorList(ArrayList<Author> authorList) {
        this.authorList = authorList;
        notifyItemRangeChanged(0, authorList.size());
    }

    public AuthorAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();
        this.context = context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.author_card, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {
        final Author currentAuthor = authorList.get(position);
        holder.authorTitle.setText(currentAuthor.getTitle());
        holder.authorDescription.setText(Ellipsizer.ellipsize(currentAuthor.getDescription(), 50));
        String urlTN = currentAuthor.getPhoto();
        holder.authorImage.setDefaultImageResId(R.drawable.author_ph);
        holder.authorImage.setErrorImageResId(R.drawable.author_ph);
        holder.authorImage.setImageUrl(urlTN, imageLoader);

        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(v, currentAuthor);
            }
        });

        holder.showPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayArticleListActivity.class);
                intent.putExtra(context.getString(R.string.PARENT_CLASS_TAG), context.getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                intent.putExtra("CAT_TITLE", currentAuthor.getTitle());
                String url = context.getString(R.string.SHOW_AUTHOR_ARTICLES_URL) + currentAuthor.getId();
                intent.putExtra("URL", url);
                context.startActivity(intent);
            }

        });

    }

    /**
     * Allows the full description of an author to be viewed
     * as a pop up.
     *
     * @param anchorView
     * @param author
     */
    public void showPopup(View anchorView, final Author author) {

        final View popupView = activity.getLayoutInflater().inflate(R.layout.pop_up_author, null);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.authorsFL);
        frameLayout.getForeground().setAlpha(220);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setAnimationStyle(R.style.Animation);
        TextView desc = (TextView) popupView.findViewById(R.id.authorDescription);
        TextView title = (TextView) popupView.findViewById(R.id.authorTitle);

        Button b = (Button) popupView.findViewById(R.id.close);
        Button showPapers = (Button) popupView.findViewById(R.id.showPapersAuthor);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        showPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(context, DisplayArticleListActivity.class);
                intent.putExtra(context.getString(R.string.PARENT_CLASS_TAG), context.getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                intent.putExtra("CAT_TITLE", author.getTitle());
                String url = context.getString(R.string.SHOW_AUTHOR_ARTICLES_URL) + author.getId();
                intent.putExtra("URL", url);
                context.startActivity(intent);
            }

        });

        desc.setText(author.getDescription());
        title.setText(author.getTitle());

        popupWindow.setFocusable(true);
        // Dismiss when when touched outside
        popupWindow.setBackgroundDrawable(new ColorDrawable());

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                FrameLayout frameLayout = (FrameLayout) view.findViewById(R.id.authorsFL);
                frameLayout.getForeground().setAlpha(0);
            }
        });

        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);

    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public void setView(View view) {
        this.view = view;
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NetworkImageView authorImage;
        TextView authorTitle;
        TextView authorDescription;
        Button readMore;
        Button showPapers;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            authorImage = (NetworkImageView) itemView.findViewById(R.id.authorImage);
            authorTitle = (TextView) itemView.findViewById(R.id.authorTitle);
            authorDescription = (TextView) itemView.findViewById(R.id.authorDescription);
            readMore = (Button) itemView.findViewById(R.id.readMoreAuthor);
            showPapers = (Button) itemView.findViewById(R.id.showPapersAuthor);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

}
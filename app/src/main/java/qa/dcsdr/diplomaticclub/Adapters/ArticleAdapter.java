package qa.dcsdr.diplomaticclub.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/11/2015.
 * This is the adapter for displaying articles.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private ArrayList<Article> articleList = new ArrayList<>();
    private boolean[] isImageSaved;
    private Context context;
    private ClickListener clickListener;
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;
    private boolean isBookmark;

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
        this.articleList.add(0,new Article(0));
        this.isImageSaved = new boolean[articleList.size()];
        Arrays.fill(this.isImageSaved, false);
        notifyItemRangeChanged(0, articleList.size());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArticleAdapter(Context context, boolean isBookmark) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();
        this.context = context;
        this.isBookmark = isBookmark;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int resource = viewType == 0 ? R.layout.article_blank_card : R.layout.article_card;
        View view = layoutInflater.inflate(resource,parent,false);
        return new ArticleViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 ? 0 : 1);
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        final Article currentArticle = articleList.get(position);
        holder.listItemU.setText(currentArticle.getTitle());
        holder.authorTV.setText(currentArticle.getAuthor());
        holder.summary.setText(currentArticle.getSumAbstract());
        if (isBookmark && !currentArticle.getTitle().equals("N/A")) {
            holder.listIconViewU.setVisibility(View.GONE);
            try {
                File nf = new File(context.getFilesDir(),currentArticle.getTitle());
                Picasso.with(context).load(nf).placeholder(R.drawable.loading_image).
                        error(R.drawable.default_art_image).into(holder.localImage);
                holder.localImage.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                holder.localImage.setVisibility(View.GONE);
                holder.listIconViewU.setVisibility(View.VISIBLE);
            }
        }
        else
            holder.listIconViewU.setImageUrl(currentArticle.getPhoto(), imageLoader);
        if (!isImageSaved[position]){
            loadImage(articleList.get(position).
                    getPhoto(), articleList.get(position).getTitle());
            isImageSaved[position] = true;
        }
    }

    private void removeBookmark(int position) {
        File f = context.getDir(context.getResources().getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
        File nf = new File(f, articleList.get(position).getId() + "");
        boolean a = false;
        boolean b = false;
        if (nf.exists())
            a = nf.delete();
        File nf1 = new File(f, articleList.get(position).getId() + "_content");
        if (nf1.exists())
            b = nf1.delete();
        if (a && b) {
            articleList.remove(position);
            notifyItemRemoved(position);
            if (articleList.size() == 1) {
                View rootView = ((Activity) context).getWindow().
                        getDecorView().findViewById(android.R.id.content);
                LinearLayout noBookmarksFound = (LinearLayout)
                        rootView.findViewById(R.id.noBookmarksLayout);
                noBookmarksFound.setVisibility(View.VISIBLE);
            }
            Toast.makeText(context, context.getString(R.string.BOOKMARK_REMOVED),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void loadImage(String url, final String title) {
        File nf = new File(context.getFilesDir(),title);
        if (nf.exists())
            return;
        if (url != null && !url.equals("N/A")) {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        FileOutputStream fo = context.openFileOutput(title, Context.MODE_PRIVATE);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (Exception e) {
                    }
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return articleList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        NetworkImageView listIconViewU;
        ImageView localImage;
        TextView listItemU;
        TextView authorTV;
        TextView summary;
        Button readMore;
        Button share;
        Button removeBookmark;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            listIconViewU = (NetworkImageView) itemView.findViewById(R.id.PlistIconViewU);
            listIconViewU.setDefaultImageResId(R.drawable.loading_image);
            listIconViewU.setErrorImageResId(R.drawable.default_art_image);
            localImage = (ImageView) itemView.findViewById(R.id.imageLocal);
            listItemU = (TextView) itemView.findViewById(R.id.PlistItemU);
            authorTV = (TextView) itemView.findViewById(R.id.PauthorTV);
            summary = (TextView) itemView.findViewById(R.id.article_abstract);
            readMore = (Button) itemView.findViewById(R.id.readMoreButton);
            share = (Button) itemView.findViewById(R.id.shareArticle);
            removeBookmark = (Button) itemView.findViewById(R.id.deleteBookmark);
            readMore.setOnClickListener(this);
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, articleList.get(getAdapterPosition()).getLink());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);
                }
            });
            if (isBookmark) {
                removeBookmark.setVisibility(View.VISIBLE);
                removeBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeBookmark(getAdapterPosition());
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getAdapterPosition());
        }
    }

}

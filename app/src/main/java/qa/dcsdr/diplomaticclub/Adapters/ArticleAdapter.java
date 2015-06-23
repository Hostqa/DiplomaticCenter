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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
        this.isImageSaved = new boolean[articleList.size()];
        Arrays.fill(this.isImageSaved, false);
        notifyItemRangeChanged(0, articleList.size());
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public ArticleAdapter(Context context, boolean isBookmark) {
        layoutInflater = layoutInflater.from(context);
        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();
        this.context = context;
        this.isBookmark = isBookmark;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.article_card, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, final int position) {
        final Article currentArticle = articleList.get(position);

        holder.listItemU.setText(currentArticle.getTitle());
        holder.authorTV.setText(currentArticle.getAuthor());
        holder.summary.setText(currentArticle.getSumAbstract());
        String urlTN = currentArticle.getPhoto();
        holder.listIconViewU.setImageUrl(urlTN, imageLoader);

//        if (!isImageSaved[position]) {
//            try {
//                context.openFileInput(articleList.get(position).getTitle());
//            } catch (FileNotFoundException e) {
//                Toast.makeText(context, "IMAGE SAVING", Toast.LENGTH_SHORT).show();
//                loadImage(articleList.get(position).
//                        getPhoto(), articleList.get(position).getTitle());
//            }
//            isImageSaved[position] = true;
//        }


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

            if (articleList.size() == 0) {
                View rootView = ((Activity) context).getWindow().getDecorView().findViewById(android.R.id.content);
                LinearLayout noBookmarksFound = (LinearLayout) rootView.findViewById(R.id.noBookmarksLayout);
                noBookmarksFound.setVisibility(View.VISIBLE);

            }
            Toast.makeText(context, context.getString(R.string.BOOKMARK_REMOVED),
                    Toast.LENGTH_SHORT).show();
        }

    }


    private void loadImage(String url, final String title) {
        if (url != null && url != "N/A") {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    String fileName = title;//no .png or .jpg needed
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        FileOutputStream fo = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (Exception e) {
                        fileName = null;
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
        TextView listItemU;
        TextView authorTV;
        TextView summary;
        Button readMore;
        Button share;
        Button removeBookmark;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            listIconViewU = (NetworkImageView) itemView.findViewById(R.id.PlistIconViewU);
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
                    sendIntent.putExtra(Intent.EXTRA_TEXT, articleList.get(getPosition()).getLink());
                    sendIntent.setType("text/plain");
                    context.startActivity(sendIntent);

                }
            });
            if (isBookmark) {
                removeBookmark.setVisibility(View.VISIBLE);
                removeBookmark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeBookmark(getPosition());
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null)
                clickListener.itemClicked(v, getPosition());
        }
    }


}

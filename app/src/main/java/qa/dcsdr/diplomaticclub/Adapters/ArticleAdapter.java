package qa.dcsdr.diplomaticclub.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;

/**
 * Created by Tamim on 6/11/2015.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private ArrayList<Article> articleList=new ArrayList<>();
    private Context context;
    private ClickListener clickListener;
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
        notifyItemRangeChanged(0, articleList.size());
    }

    public void setClickListener (ClickListener clickListener) {
        this.clickListener=clickListener;
    }

    public ArticleAdapter(Context context) {
        layoutInflater = layoutInflater.from(context);
        volleySingleton=VolleySingleton.getsInstance();
        imageLoader=volleySingleton.getImageLoader();
        this.context=context;
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.article_card, parent, false);
        ArticleViewHolder viewHolder = new ArticleViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ArticleViewHolder holder, int position) {
        final Article currentArticle = articleList.get(position);
        holder.listItemU.setText(currentArticle.getTitle());
        holder.authorTV.setText(currentArticle.getAuthor());
        holder.summary.setText(currentArticle.getSumAbstract());
        String urlTN = currentArticle.getPhoto();
        holder.listIconViewU.setImageUrl(urlTN, imageLoader);
        try {
            context.openFileInput(currentArticle.getTitle());
        } catch (FileNotFoundException e) {
            loadImage(urlTN, holder, currentArticle.getTitle());
        }
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, currentArticle.getLink());
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });
    }

    private void loadImage(String url, final ArticleViewHolder viewHolderPublication, final String title) {
        if (url!=null && url!="N/A")
        {
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
                public void onErrorResponse(VolleyError error) {}

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

        public ArticleViewHolder(View itemView) {
            super(itemView);
            listIconViewU= (NetworkImageView) itemView.findViewById(R.id.PlistIconViewU);
            listItemU = (TextView) itemView.findViewById(R.id.PlistItemU);
            authorTV = (TextView) itemView.findViewById(R.id.PauthorTV);
            summary = (TextView) itemView.findViewById(R.id.article_abstract);
            readMore = (Button) itemView.findViewById(R.id.readMoreButton);
            share = (Button) itemView.findViewById(R.id.shareArticle);
            readMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener!=null)
                clickListener.itemClicked(v,getPosition());
        }
    }


}

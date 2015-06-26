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

import qa.dcsdr.diplomaticclub.Activities.DisplayArticleListActivity;
import qa.dcsdr.diplomaticclub.Items.Author;
import qa.dcsdr.diplomaticclub.Items.ClickListener;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.Ellipsizer;

/**
 * Created by Tamim on 6/11/2015.
 */
public class AuthorAdapter  extends RecyclerView.Adapter<AuthorAdapter.ArticleViewHolder>  {

    private ArrayList<Author> authorList=new ArrayList<>();
    private Context context;
    private ClickListener clickListener;
    private LayoutInflater layoutInflater;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public void setAuthorList(ArrayList<Author> authorList) {
        this.authorList = authorList;
        notifyItemRangeChanged(0, authorList.size());
    }

    public AuthorAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        volleySingleton=VolleySingleton.getsInstance();
        imageLoader=volleySingleton.getImageLoader();
        this.context=context;
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
        holder.authorDescription.setText(Ellipsizer.ellipsize(currentAuthor.getDescription(),100));
        String urlTN = currentAuthor.getPhoto();
        holder.authorImage.setDefaultImageResId(R.drawable.author_ph);
        holder.authorImage.setErrorImageResId(R.drawable.author_ph);
        holder.authorImage.setImageUrl(urlTN, imageLoader);
        try {
            context.openFileInput(currentAuthor.getTitle());
        } catch (FileNotFoundException e) {
            loadImage(urlTN, holder, currentAuthor.getTitle());
        }
        holder.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.readMore.getText().equals(context.getString(R.string.READ_MORE))) {
                    holder.authorDescription.setText(currentAuthor.getDescription());
                    holder.readMore.setText(context.getString(R.string.READ_LESS));
                }
                else {
                    holder.authorDescription.setText(Ellipsizer.ellipsize(currentAuthor.getDescription(), 100));
                    holder.readMore.setText(context.getString(R.string.READ_MORE));
                }
            }
        });

        holder.showPapers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DisplayArticleListActivity.class);
                intent.putExtra(context.getString(R.string.PARENT_CLASS_TAG), context.getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                intent.putExtra("CAT_TITLE", currentAuthor.getTitle());
                String url = context.getString(R.string.SHOW_AUTHOR_ARTICLES_URL)+currentAuthor.getId();
                intent.putExtra("URL", url);
                context.startActivity(intent);
            }

        });
    }

    private void loadImage(String url, final ArticleViewHolder viewHolderPublication, final String title) {
        if (url!=null && !url.equals("N/A"))
        {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        FileOutputStream fo = context.openFileOutput(title, Context.MODE_PRIVATE);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (Exception e) {}
                }
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    class ArticleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        NetworkImageView authorImage;
        TextView authorTitle;
        TextView authorDescription;
        Button readMore;
        Button showPapers;

        public ArticleViewHolder(View itemView) {
            super(itemView);
            authorImage= (NetworkImageView) itemView.findViewById(R.id.authorImage);
            authorTitle = (TextView) itemView.findViewById(R.id.authorTitle);
            authorDescription = (TextView) itemView.findViewById(R.id.authorDescription);
            readMore = (Button) itemView.findViewById(R.id.readMoreAuthor);
            showPapers = (Button) itemView.findViewById(R.id.showPapersAuthor);
        }

        @Override
        public void onClick(View v) {
            if (clickListener!=null)
                clickListener.itemClicked(v,getAdapterPosition());
        }
    }

}
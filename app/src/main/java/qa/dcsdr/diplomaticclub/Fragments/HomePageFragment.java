package qa.dcsdr.diplomaticclub.Fragments;

/**
 * Created by Tamim on 6/16/2015.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import qa.dcsdr.diplomaticclub.Activities.ArticleReader;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.Ellipsizer;


public class HomePageFragment extends Fragment {

    Article article;
    TextView featuredCategory;
    NetworkImageView featuredImage;
    TextView featuredTitle;
    TextView featuredAuthor;
    String category;
    Button readMore;
    Button featuredShare;
    private ArrayList<Article> articleList;
    private int position;
    private ImageLoader imageLoader;
    private boolean[] isImageSaved;

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        VolleySingleton volleySingleton;
        volleySingleton=VolleySingleton.getsInstance();
        imageLoader=volleySingleton.getImageLoader();

        article = articleList.get(position);

        View view =  inflater.inflate(R.layout.fragment_home_page,container,false);
        featuredCategory = (TextView) view.findViewById(R.id.featuredCategory);
        featuredImage = (NetworkImageView) view.findViewById(R.id.featuredImage);
        featuredTitle= (TextView) view.findViewById(R.id.featuredTitle);
        featuredAuthor= (TextView) view.findViewById(R.id.featuredAuthor);

        readMore = (Button) view.findViewById(R.id.featuredReadMore);
        featuredShare = (Button) view.findViewById(R.id.featuredShare);

        featuredCategory.setText(category);
        featuredAuthor.setText(article.getAuthor());
        featuredImage.setDefaultImageResId(R.drawable.loading_image);
        featuredImage.setErrorImageResId(R.drawable.default_art_image);
        featuredTitle.setText(Ellipsizer.ellipsize(article.getTitle(), 60));

        featuredImage.setImageUrl(article.getPhoto(), imageLoader);

//        try {
//            getActivity().openFileInput(article.getTitle());
//        } catch (FileNotFoundException e) {
//            loadImage(article.getPhoto(), article.getTitle());
//        }

        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadImage(articleList.get(position).
                        getPhoto(), articleList.get(position).getTitle());

            }
        });

        featuredShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, article.getLink());
                sendIntent.setType("text/plain");
                getActivity().startActivity(sendIntent);
            }
        });

        return view;
    }


    private void loadImage(String url, final String title) {
        if (url!=null && url!="N/A")
        {
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    String fileName = title;//no .png or .jpg needed
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        FileOutputStream fo = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
                        fo.write(bytes.toByteArray());
                        fo.close();
                        Intent intent = new Intent(getActivity(), ArticleReader.class);
                        intent.putExtra("CAT_TITLE", category);
                        intent.putExtra("ARTICLE_LIST", articleList);
                        intent.putExtra("POSITION", position);
                        intent.putExtra("IS_HOME", 1);
                        intent.putExtra("URL", "HOME");
                        intent.putExtra(getString(R.string.PARENT_CLASS_TAG), getString(R.string.DISPLAY_FRAGMENT_TAG));
                        startActivity(intent);
                    } catch (Exception e) {
                        fileName = null;
                    }
                }

                @Override
                public void onErrorResponse(VolleyError error) {}

            });
        }
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
        this.isImageSaved = new boolean[articleList.size()];
        Arrays.fill(this.isImageSaved, false);
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
package qa.dcsdr.diplomaticclub.Fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import qa.dcsdr.diplomaticclub.Activities.ArticleReader;
import qa.dcsdr.diplomaticclub.Activities.HomePageActivity;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.Ellipsizer;

/**
 * Created by Tamim on 6/16/2015.
 * This is the fragment for the homepage.
 */
public class HomePageFragment extends Fragment {

    private Article article;
    private TextView featuredCategory;
    private NetworkImageView featuredImage;
    private TextView featuredTitle;
    private TextView featuredAuthor;
    private String category;
    private Button readMore;
    private Button featuredShare;
    private ArrayList<Article> articleList;
    private int position;
    private ImageLoader imageLoader;
    private boolean[] isImageSaved;
    private ImageButton left;
    private ImageButton right;
    private LinearLayout leftLayout;
    private LinearLayout rightLayout;


    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        left = (ImageButton) view.findViewById(R.id.left);
        right = (ImageButton) view.findViewById(R.id.right);

        leftLayout = (LinearLayout) view.findViewById(R.id.leftLayout);
        rightLayout = (LinearLayout) view.findViewById(R.id.rightLayout);

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePageActivity)getActivity()).setCurrentItem(category, true, true);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomePageActivity) getActivity()).setCurrentItem(category, true, false);
            }
        });

        if (position == 0) {
            leftLayout.setVisibility(View.GONE);
            left.setVisibility(View.GONE);
        }
        if (position == articleList.size()-1) {
            right.setVisibility(View.GONE);
            rightLayout.setVisibility(View.GONE);
        }

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
                    try {
                        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                        response.getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                        FileOutputStream fo = getActivity().openFileOutput(title, Context.MODE_PRIVATE);
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
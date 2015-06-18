package qa.dcsdr.diplomaticclub.Activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import qa.dcsdr.diplomaticclub.Fragments.NavigationDrawerFragment;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ArticleContent;


public class ArticleReader extends ActionBarActivity {
    private Toolbar toolbar;
    TextView articleTitle;
    TextView articleContents;
    TextView articleCategory;
    ImageView articleImage;
    TextView articleAuthor;
    TextView articleDate;
    Button nextArticle;
    Button prevArticle;
    ScrollView scrollView;
    View c;
    private Button retryButton;
    LinearLayout errorLayoutR;
    private TextView volleyError;
    LinearLayout readerProgressBar;
    Activity a;
    String category;

    private ArrayList<Article> articleList;
    private int position;
    private Handler mHandler = new Handler();

    ArticleContent ac;
    private String url;
    private Menu menu;
    private float defaultSize;

    public static String decompress(byte[] compressed) throws IOException {
        final int BUFFER_SIZE = 32;
        ByteArrayInputStream is = new ByteArrayInputStream(compressed);
        GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE);
        StringBuilder string = new StringBuilder();
        byte[] data = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = gis.read(data)) != -1) {
            string.append(new String(data, 0, bytesRead));
        }
        gis.close();
        is.close();
        return string.toString();
    }

    public void navigate(boolean isNext) {
        if (isNext) {
            this.position += 1;
            if (position == (articleList.size() - 1)) {
                nextArticle.setVisibility(View.GONE);
            }
        } else {
            this.position -= 1;
            if (position == 0) {
                prevArticle.setVisibility(View.GONE);
            }
        }
        if (position > 0 && prevArticle.getVisibility() == View.GONE) {
            prevArticle.setVisibility(View.VISIBLE);
        }
        if (position < (articleList.size() - 1) && nextArticle.getVisibility() == View.GONE) {
            nextArticle.setVisibility(View.VISIBLE);
        }
        Article current = articleList.get(position);
        articleTitle.setText(current.getTitle());
        articleAuthor.setText(current.getAuthor());
        articleDate.setText(current.getDate());
        try {
            ac.sendXmlRequest(c);
        } catch (Exception e) {
            e.printStackTrace();
            articleContents.setText("Error.");
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(openFileInput(current.getTitle()));
            articleImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        }, 200);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
        }
        a = this;
        setContentView(R.layout.activity_article_reader);
        setTitle("Diplomatic Center");
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();

        c = getWindow().getDecorView().findViewById(android.R.id.content);

        scrollView = (ScrollView) findViewById(R.id.articleScroll);
        articleList = (ArrayList<Article>) extras.get("ARTICLE_LIST");
        category = (String) extras.get("CAT_TITLE");
        position = (int) extras.get("POSITION");
        url = (String) extras.get("URL");

        ac = new ArticleContent(articleList.get(position).getId());

        Article current = articleList.get(position);

        articleTitle = (TextView) findViewById(R.id.articleTitle);
        articleContents = (TextView) findViewById(R.id.articleContents);
        articleCategory = (TextView) findViewById(R.id.category);
        articleAuthor = (TextView) findViewById(R.id.articleAuthor);
        articleDate = (TextView) findViewById(R.id.articleDate);
        articleImage = (ImageView) findViewById(R.id.articleImage);

        errorLayoutR = (LinearLayout) findViewById(R.id.errorLayoutR);
        volleyError = (TextView) findViewById(R.id.volleyErrorR);
        retryButton = (Button) findViewById(R.id.retryButtonR);
        readerProgressBar = (LinearLayout) findViewById(R.id.readerProgressBar);

        nextArticle = (Button) findViewById(R.id.next);
        prevArticle = (Button) findViewById(R.id.prev);

        articleCategory.setText(category);

        articleTitle.setText(current.getTitle());
        articleAuthor.setText(current.getAuthor());
        articleDate.setText(current.getDate());


        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retryButton.setVisibility(View.GONE);
                volleyError.setVisibility(View.GONE);
                readerProgressBar.setVisibility(View.VISIBLE);
                errorLayoutR.setVisibility(View.GONE);
                ac.sendXmlRequest(c);
            }
        });

        try {
            Log.d("IDDD", articleList.get(position).getId() + "");
            ac.sendXmlRequest(c);
        } catch (Exception e) {
            e.printStackTrace();
            articleContents.setText("Error.");
        }

        try {
            Bitmap bitmap = BitmapFactory.decodeStream(openFileInput(current.getTitle()));
            articleImage.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
        }

        defaultSize = articleContents.getTextSize();

        prevArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NAVIGATION!!!!!", "TEST111111111");
                if (position > 0) {
                    navigate(false);
                } else {
                    prevArticle.setVisibility(View.GONE);
                }

            }
        });

        if (articleList.size() == 1) {
            nextArticle.setVisibility(View.GONE);
            prevArticle.setVisibility(View.GONE);
        } else if (position == (articleList.size() - 1)) {
            nextArticle.setVisibility(View.GONE);
        } else if (position == 0) {
            prevArticle.setVisibility(View.GONE);
        }


        nextArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("NAVIGATION!!!!!", "TEST");
                if (position < (articleList.size() - 1)) {
                    navigate(true);
                } else {
                    nextArticle.setVisibility(View.GONE);
                }

            }
        });


        getSupportActionBar().setDisplayShowHomeEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, true);


    }

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent parentIntent = getIntent();

        String className = parentIntent.getStringExtra(getString(R.string.PARENT_CLASS_TAG));
        Intent newIntent = null;
        try {
            //you need to define the class with package name
            newIntent = new Intent(this, Class.forName("qa.dcsdr.diplomaticclub.Fragments." + className));

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_reader, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.top) {
            scrollView.smoothScrollTo(0, 0);
        } else if (id == R.id.bottom) {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else if (id == R.id.increase) {
            articleContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, articleContents.getTextSize() + 3);
        } else if (id == R.id.decrease) {
            if (articleContents.getTextSize()>30F)
                articleContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, articleContents.getTextSize() - 3);
        } else if (id == R.id.night_mode) {
            String currentMode = (String) menu.findItem(R.id.night_mode).getTitle();
            if (currentMode.equals(getString(R.string.NIGHT_MODE))) {
                articleTitle.setTextColor(getResources().getColor(R.color.colorWhite));
                articleContents.setTextColor(getResources().getColor(R.color.colorWhite));
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                nextArticle.setTextColor(getResources().getColor(R.color.colorWhite));
                prevArticle.setTextColor(getResources().getColor(R.color.colorWhite));
                articleAuthor.setTextColor(getResources().getColor(R.color.colorWhite));
                articleDate.setTextColor(getResources().getColor(R.color.colorWhite));
                menu.findItem(R.id.night_mode).setTitle(getString(R.string.DAY_MODE));
            } else {
                articleTitle.setTextColor(getResources().getColor(R.color.colorBlack));
                articleContents.setTextColor(getResources().getColor(R.color.colorBlack));
                scrollView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                nextArticle.setTextColor(getResources().getColor(R.color.colorPrimary));
                prevArticle.setTextColor(getResources().getColor(R.color.colorPrimary));
                articleAuthor.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                articleDate.setTextColor(getResources().getColor(R.color.colorPrimaryLight));
                menu.findItem(R.id.night_mode).setTitle(getString(R.string.NIGHT_MODE));
            }

        } else if (id == R.id.default_size) {
            articleContents.setTextSize(TypedValue.COMPLEX_UNIT_PX, defaultSize);
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setArticleListAndCurrentPosition(ArrayList<Article> articleList, int position) {
        this.articleList = articleList;
        this.position = position;
    }

    @Override
    public void onBackPressed() {
        Intent intent;

        if (this.getIntent().hasExtra("IS_HOME")) {
            finish();
            return;
        } else {
            intent = new Intent(a, DisplayArticleListActivity.class);
            intent.putExtra("CAT_TITLE", category);
            intent.putExtra(getString(R.string.PARENT_CLASS_TAG), getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
            intent.putExtra("URL", url);
        }
        startActivity(intent);
        finish();
    }
}

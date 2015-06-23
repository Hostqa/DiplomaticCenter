package qa.dcsdr.diplomaticclub.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Fragments.NavigationDrawerFragment;
import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.VolleySingleton;
import qa.dcsdr.diplomaticclub.R;
import qa.dcsdr.diplomaticclub.Tools.ArticleContent;
import qa.dcsdr.diplomaticclub.Tools.ContentDecrypter;


public class ArticleReader extends ActionBarActivity {

    private Toolbar toolbar;
    private TextView articleTitle;
    private TextView articleContents;
    private TextView articleCategory;
    private NetworkImageView articleImage;
    private TextView articleAuthor;
    private TextView articleDate;
    private Button nextArticle;
    private Button prevArticle;
    private ScrollView scrollView;
    private View c;
    private Button retryButton;
    private LinearLayout errorLayoutR;
    private TextView volleyError;
    private LinearLayout readerProgressBar;
    private Activity a;
    private String category;
    private ArrayList<Article> articleList;
    private int position;
    private Handler mHandler = new Handler();
    private ArticleContent ac;
    private String url;
    private Menu menu;
    private float defaultSize;
    private ImageLoader imageLoader;
    private VolleySingleton volleySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        a = this;

        setContentView(R.layout.activity_article_reader);
        setTitle(getResources().getString(R.string.title_activity_article_reader));
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar, true);
        Intent myIntent = getIntent();
        Bundle extras = myIntent.getExtras();
        articleList = (ArrayList<Article>) extras.get("ARTICLE_LIST");
        category = (String) extras.get("CAT_TITLE");
        position = (int) extras.get("POSITION");
        url = (String) extras.get("URL");
        Article current = articleList.get(position);
        ac = new ArticleContent(current.getId(), getResources().getString(R.string.SINGLE_ARTICLE_ID), this);
        articleTitle = (TextView) findViewById(R.id.articleTitle);
        articleContents = (TextView) findViewById(R.id.articleContents);
        articleCategory = (TextView) findViewById(R.id.category);
        articleAuthor = (TextView) findViewById(R.id.articleAuthor);
        articleDate = (TextView) findViewById(R.id.articleDate);
        articleImage = (NetworkImageView) findViewById(R.id.articleImage);
        c = getWindow().getDecorView().findViewById(android.R.id.content);
        scrollView = (ScrollView) findViewById(R.id.articleScroll);
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
        defaultSize = articleContents.getTextSize();

        volleySingleton = VolleySingleton.getsInstance();
        imageLoader = volleySingleton.getImageLoader();

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
        if (url.equals("LOCAL")) {
            retryButton.setVisibility(View.GONE);
            volleyError.setVisibility(View.GONE);
            readerProgressBar.setVisibility(View.GONE);
            errorLayoutR.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
            articleContents.setText(Html.fromHtml(new ContentDecrypter().decrypt
                    ((getSavedBookmark(articleList.get(position).getId())))));
        } else {

            try {

                File f = getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
                File nf = new File(f, articleList.get(position).getId() + "");
                if (nf.exists()) {
                    String content = getSavedBookmark(articleList.get(position).getId());
                    articleContents.setText(Html.fromHtml(new ContentDecrypter().decrypt((content))));
                    retryButton.setVisibility(View.GONE);
                    volleyError.setVisibility(View.GONE);
                    readerProgressBar.setVisibility(View.GONE);
                    errorLayoutR.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                } else {
                    ac.setIdAndUrl(getResources().getString(R.string.SINGLE_ARTICLE_ID), articleList.get(position).getId());
                    ac.sendXmlRequest(c);
                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_border);
                    menu.findItem(R.id.bookmark).setTitle(R.string.BOOKMARK);
                    if (this.menu.findItem(R.string.DELETE_BOOKMARK) != null)
                        this.menu.removeItem(R.string.DELETE_BOOKMARK);
                }


//                ac.sendXmlRequest(c);
            } catch (Exception e) {
                e.printStackTrace();
                articleContents.setText("Error.");
            }
        }
        // TODO: make sure image is always there
        articleImage.setImageUrl(current.getPhoto(), imageLoader);

//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(openFileInput(current.getTitle()));
//            articleImage.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//            articleImage.setImageUrl(current.getPhoto(), imageLoader);
//        }
        prevArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    navigate(false);
                } else {
                    prevArticle.setVisibility(View.GONE);
                }
            }
        });
        nextArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < (articleList.size() - 1)) {
                    navigate(true);
                } else {
                    nextArticle.setVisibility(View.GONE);
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

        scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            public boolean goingUp = false;
            private static final int HIDE_THRESHOLD = 20;
            private int scrolledDistance = 0;
            private boolean controlsVisible = true;
            private float current;

            private void onHide() {
                toolbar.animate().translationY(-toolbar.getHeight()).setInterpolator(new AccelerateInterpolator(2));
            }

            private void onShow() {
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void onScrollChanged() {
                float old = current;
                float y = scrollView.getScrollY();
                current = y;
                Rect scrollBounds = new Rect();
                scrollView.getHitRect(scrollBounds);
                boolean next = nextArticle.getLocalVisibleRect(scrollBounds);
                boolean prev = prevArticle.getLocalVisibleRect(scrollBounds);
                if (goingUp && (y < old)) {
                    if (articleImage.getLocalVisibleRect(scrollBounds) || next || prev) {
                        if (!controlsVisible) {
                            onShow();
                            toolbar.setAlpha(1);
                            controlsVisible = true;
                        } else {
                            if (y != 0 && !next && !prev && !goingUp)
                                toolbar.setAlpha((articleImage.getHeight() - y) / articleImage.getHeight());
                            else
                                toolbar.setAlpha(1);
                        }
                    }
                } else {
                    goingUp = false;
                    if (articleImage.getLocalVisibleRect(scrollBounds) || next || prev) {
                        if (!controlsVisible) {
                            onShow();
                            toolbar.setAlpha(1);
                            controlsVisible = true;
                        } else {
                            if (y != 0 && !next && !prev && !goingUp)
                                toolbar.setAlpha((articleImage.getHeight() - y) / articleImage.getHeight());
                            else
                                toolbar.setAlpha(1);
                        }
                    } else {
                        if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                            onHide();
                            controlsVisible = false;
                            scrolledDistance = 0;
                        } else if ((y < old) && !controlsVisible) {
                            goingUp = true;
                            toolbar.setAlpha(1);
                            onShow();
                            controlsVisible = true;
                            scrolledDistance = 0;
                        }
                    }
                    if ((controlsVisible && y > 0) || (!controlsVisible && y < 0)) {
                        scrolledDistance += y;
                    }
                }
            }
        });
    }

    @Override
    public Intent getSupportParentActivityIntent() {
        Intent parentIntent = getIntent();
        String className = parentIntent.getStringExtra(getString(R.string.PARENT_CLASS_TAG));
        Intent newIntent = null;
        try {
            newIntent = new Intent(this, Class.forName(getString(R.string.FRAGMENTS_ADDRESS) + className));
        } catch (ClassNotFoundException e) {
        }
        return newIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_article_reader, menu);
        this.menu = menu;
        if (getIntent().getExtras().get("URL").equals("LOCAL")) {
            menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark);
            menu.findItem(R.id.bookmark).setTitle(R.string.NO_BOOKMARK);
            MenuItem mi = this.menu.add(Menu.NONE, R.string.DELETE_BOOKMARK,
                    200, R.string.REMOVE_BOOKMARK);
            mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
        } else {

            File f = getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
            File nf = new File(f, articleList.get(position).getId() + "");
            if (nf.exists()) {
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark);
                menu.findItem(R.id.bookmark).setTitle(R.string.NO_BOOKMARK);
                MenuItem mi = this.menu.add(Menu.NONE, R.string.DELETE_BOOKMARK,
                        200, R.string.REMOVE_BOOKMARK);
                mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }

        }
        return true;
    }

    /*
           Allowing the user to change text size and adding Night Mode.
    */
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
            if (articleContents.getTextSize() > 30F)
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
        } else if (id == R.id.bookmark) {

            if (url.equals("LOCAL"))
                Toast.makeText(this, getString(R.string.ALREADY_BOOKMARKED), Toast.LENGTH_SHORT).show();
            else if (menu.findItem(R.id.bookmark).getTitle().equals(getResources().getString(R.string.NO_BOOKMARK)))
                Toast.makeText(this, getString(R.string.ALREADY_BOOKMARKED), Toast.LENGTH_SHORT).show();

            else {
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark);
                menu.findItem(R.id.bookmark).setTitle(R.string.NO_BOOKMARK);
                Toast.makeText(this, getString(R.string.BOOKMARK_ADDED), Toast.LENGTH_SHORT).show();
                bookmarkArticle();
                menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark);
                MenuItem mi = this.menu.add(Menu.NONE, R.string.DELETE_BOOKMARK,
                        200, R.string.REMOVE_BOOKMARK);
                mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
            }
            return true;
        } else if (id == R.string.DELETE_BOOKMARK) {
            removeBookmark();
        }
        return super.onOptionsItemSelected(item);
    }

    private void removeBookmark() {
        File bmDir = getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
        File[] f = bmDir.listFiles();
        for (int i = 0; i < f.length; i++) {
            if (f[i].getName().equals(articleList.get(position).getId() + "")) {
                boolean b = f[i].delete();
                if (b)
                    Toast.makeText(this, getString(R.string.BOOKMARK_REMOVED), Toast.LENGTH_SHORT).show();

            } else if (f[i].getName().equals(articleList.get(position).getId() + "_content")) {
                boolean b = f[i].delete();
                if (b)
                    Toast.makeText(this, getString(R.string.BOOKMARK_REMOVED), Toast.LENGTH_SHORT).show();
            }
        }
        onBackPressed();

    }

    private void bookmarkArticle() {
        ArticleContent toBookmark = new ArticleContent(articleList.get(position).getId(),
                getResources().getString(R.string.SINGLE_ARTICLE_ID), a);
        toBookmark.bookmarkArticle();
    }

    /*
        Allows navigation from one article to another.
    */
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
        if (url.equals("LOCAL")) {
            retryButton.setVisibility(View.GONE);
            volleyError.setVisibility(View.GONE);
            readerProgressBar.setVisibility(View.GONE);
            errorLayoutR.setVisibility(View.GONE);
            articleContents.setText(Html.fromHtml(new ContentDecrypter().decrypt
                    ((getSavedBookmark(articleList.get(position).getId())))));
        } else {
            try {
                File f = getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
                File nf = new File(f, articleList.get(position).getId() + "");
                if (nf.exists()) {
                    String content = getSavedBookmark(articleList.get(position).getId());
                    articleContents.setText(Html.fromHtml(new ContentDecrypter().decrypt((content))));
                    retryButton.setVisibility(View.GONE);
                    volleyError.setVisibility(View.GONE);
                    readerProgressBar.setVisibility(View.GONE);
                    errorLayoutR.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark);
                    menu.findItem(R.id.bookmark).setTitle(R.string.NO_BOOKMARK);
                    if (this.menu.findItem(R.string.DELETE_BOOKMARK) == null) {
                        MenuItem mi = this.menu.add(Menu.NONE, R.string.DELETE_BOOKMARK,
                                200, R.string.REMOVE_BOOKMARK);
                        mi.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                    }
                } else {
                    ac.setIdAndUrl(getResources().getString(R.string.SINGLE_ARTICLE_ID), articleList.get(position).getId());
                    ac.sendXmlRequest(c);
                    menu.findItem(R.id.bookmark).setIcon(R.drawable.ic_bookmark_border);
                    menu.findItem(R.id.bookmark).setTitle(R.string.BOOKMARK);
                    if (this.menu.findItem(R.string.DELETE_BOOKMARK) != null)
                        this.menu.removeItem(R.string.DELETE_BOOKMARK);
                }
            } catch (Exception e) {
                e.printStackTrace();
                articleContents.setText("Error.");
            }
        }

        articleImage.setImageUrl(current.getPhoto(), imageLoader);
//        try {
//            Bitmap bitmap = BitmapFactory.decodeStream(openFileInput(current.getTitle()));
//            articleImage.setImageBitmap(bitmap);
//        } catch (FileNotFoundException e) {
//        }
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                scrollView.smoothScrollTo(0, 0);
            }
        }, 200);
    }

    private String getSavedBookmark(int id) {
        File bmDir = getDir(getString(R.string.BOOKMARK_DIRECTORY), Context.MODE_PRIVATE);
        File[] f = bmDir.listFiles();
        // TODO: GET SPECIFIC FILE
        try {
            for (int i = 0; i < f.length; i++) {
                if (f[i].getName().equals(id + "_content")) {
                    FileInputStream fis = new FileInputStream(f[i]);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                    char[] b = new char[(int) f[i].length()];
                    br.read(b, 0, (int) f[i].length());
                    String c = new String(b);
                    return c;
                }

            }

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void onBackPressed() {
        Intent intent;
        if (this.getIntent().hasExtra("URL")) {
            if (this.getIntent().getExtras().get("URL").equals("LOCAL")) {
                intent = new Intent(a, DisplayArticleListActivity.class);
                intent.putExtra("CAT_TITLE", category);
                intent.putExtra(getString(R.string.PARENT_CLASS_TAG), getString(R.string.DISPLAY_FRAGMENT_PARENT_TAG));
                intent.putExtra("URL", url);
                startActivity(intent);
                finish();
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }

    }

}
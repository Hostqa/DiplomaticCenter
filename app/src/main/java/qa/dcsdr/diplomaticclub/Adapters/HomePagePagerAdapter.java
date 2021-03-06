package qa.dcsdr.diplomaticclub.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Fragments.HomePageFragment;
import qa.dcsdr.diplomaticclub.Items.Article;

/**
 * Created by Tamim on 6/16/2015.
 * This is the fragment adapter for the homepage.
 */
public class HomePagePagerAdapter extends FragmentStatePagerAdapter  {

    private ArrayList<Article> articleList = new ArrayList<>();

    public String getCategory() {
        return category;
    }

    private String category;

    public HomePagePagerAdapter(FragmentManager fm, ArrayList<Article> articleList) {
        super(fm);
        this.articleList=articleList;
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
    }

    @Override
    public Fragment getItem(int position) {
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArticleList(articleList);
        fragment.setPosition(position);
        fragment.setCategory(category);
        return fragment;
    }

    @Override
    public int getCount() {
        return articleList.size();
    }

    public void setCategory(String category) {
        this.category = category;
    }

}


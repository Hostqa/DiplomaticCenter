package qa.dcsdr.diplomaticclub.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Fragments.HomePageFragment;
import qa.dcsdr.diplomaticclub.Items.Article;

/**
 * Created by Tamim on 6/16/2015.
 */

public class HomePagePagerAdapter extends FragmentStatePagerAdapter  {

    private ArrayList<Article> articleList = new ArrayList<>();
    private String category;

    public HomePagePagerAdapter(FragmentManager fm, ArrayList<Article> articleList) {
        super(fm);
        this.articleList=articleList;
        // TODO Auto-generated constructor stub
    }

    public void setArticleList(ArrayList<Article> articleList) {
        this.articleList = articleList;
    }


    @Override
    public Fragment getItem(int position) {
        // TODO Auto-generated method stub
        HomePageFragment fragment = new HomePageFragment();
        fragment.setArticleList(articleList);
        fragment.setPosition(position);
//        fragment.setArticle(articleList.get(position));
        fragment.setCategory(category);
        return fragment;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        Log.d("COUNT", articleList.size() + "");
        return articleList.size();
    }


    public void setCategory(String category) {
        this.category = category;
    }
}


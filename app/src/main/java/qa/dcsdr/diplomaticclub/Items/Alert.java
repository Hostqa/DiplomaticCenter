package qa.dcsdr.diplomaticclub.Items;

/**
 * Created by Tamim on 8/4/2015.
 */
public class Alert {

    private String title;
    private int articleID;

    public Alert(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getArticleID() {
        return articleID;
    }

    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }
}

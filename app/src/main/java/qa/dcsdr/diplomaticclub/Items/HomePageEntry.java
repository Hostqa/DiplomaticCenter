package qa.dcsdr.diplomaticclub.Items;

/**
 * Created by Tamim on 6/14/2015.
 */
public class HomePageEntry {

    private int imageId;
    private String categoryTitle;

    public HomePageEntry(String categoryTitle, int imageId) {
        this.categoryTitle=categoryTitle;
        this.imageId=imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

}

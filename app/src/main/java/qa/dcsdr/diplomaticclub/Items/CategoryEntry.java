package qa.dcsdr.diplomaticclub.Items;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tamim on 6/13/2015.
 */
public class CategoryEntry {

    private List<String> subCategories = Collections.emptyList();
    private int categoryImageId;
    private String categoryTitle;

    public int getCategoryImageId() {
        return categoryImageId;
    }

    public void setCategoryImageId(int categoryImageId) {
        this.categoryImageId = categoryImageId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<String> subCategories) {
        this.subCategories = subCategories;
    }

}

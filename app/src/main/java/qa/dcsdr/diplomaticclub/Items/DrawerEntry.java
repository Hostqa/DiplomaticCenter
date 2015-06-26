package qa.dcsdr.diplomaticclub.Items;

/**
 * Created by Tamim on 6/7/2015.
 * This is a drawer entry with just a title.
 */
public class DrawerEntry {

    private String title;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        DrawerEntry de = (DrawerEntry) o;
        return de.title.equals(this.title);
    }
}

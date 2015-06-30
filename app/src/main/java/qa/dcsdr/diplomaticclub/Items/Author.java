package qa.dcsdr.diplomaticclub.Items;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Tamim on 6/17/2015.
 * This is the class that stores all the author information.
 */
public class Author implements Parcelable {

    private String title;
    private int id;
    private String photo;
    private String description;

    public static final Parcelable.Creator<Author> CREATOR
            = new Parcelable.Creator<Author>() {
        public Author createFromParcel(Parcel in) {
            return new Author(in);
        }
        public Author[] newArray(int size) {
            return new Author[size];
        }
    };

    public Author(){}

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(photo);
        dest.writeString(description);

    }

    public Author(Parcel in) {
        id = in.readInt();
        title = in.readString();
        photo = in.readString();
        description = in.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
package qa.dcsdr.diplomaticclub.Items;

import android.os.Parcel;
import android.os.Parcelable;

import qa.dcsdr.diplomaticclub.Tools.Ellipsizer;

/**
 * Created by Tamim on 6/11/2015.
 */
public class Article implements Parcelable {

    int id;
    String title;
    String link;
    String photo;
    String description;
    String date;
    String author;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article(Parcel in) {
        id = in.readInt();
        title = in.readString();
        link = in.readString();
        photo = in.readString();
        description = in.readString();
        date = in.readString();
        author = in.readString();
    }

    public Article(int id) {
        this.id = id;
        this.title = "N/A";
        this.link = "N/A";
        this.photo = "N/A";
        this.description = "N/A";
        this.date = "N/A";
        this.author = "N/A";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String toString() {
        String str = "";
        str += getTitle() + "\n" +
                "Date: " + getDate();
        return str;
    }

    public String getSumAbstract() {
        return Ellipsizer.ellipsize(description, 60);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(photo);
        dest.writeString(description);
        dest.writeString(date);
        dest.writeString(author);
    }

    public static final Parcelable.Creator<Article> CREATOR
            = new Parcelable.Creator<Article>() {
        public Article createFromParcel(Parcel in) {
            return new Article(in);
        }

        public Article[] newArray(int size) {
            return new Article[size];
        }
    };

}

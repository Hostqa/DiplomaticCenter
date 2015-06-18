package qa.dcsdr.diplomaticclub.Tools;

import android.support.v4.app.FragmentActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Article;

public class ParseArticle {

    private String data;
    private FragmentActivity fa;
    private ArrayList<Article> articles;

    public ParseArticle(String xmlData) {
        this.data = xmlData;
        this.articles = new ArrayList<Article>();
    }

    public ParseArticle(String xmlData, FragmentActivity activity) {
        this.data=xmlData;
        this.articles = new ArrayList<Article>();
        this.fa=activity;
    }



    public ArrayList<Article> getArticles() {
        return articles;
    }

    public boolean processXml() {
        boolean status = true;
        Article currentArticle = null;
        boolean inEntry = false;
        String textValue = "";

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();

            String item="item";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
//                        Log.d("SPLIT", tag);
                        String[] s = tag.toLowerCase().split("-");
//                        Log.d("SPLIT", s.length+"");
                        int id = Integer.parseInt(s[1]);
//                        Log.d("SPLIT: ID", id+"");
                        currentArticle = new Article(Integer.parseInt(s[1]));
                        currentArticle.setId(id);
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inEntry) {
                        if (tag.toLowerCase().contains(item.toLowerCase())) {
                            if (currentArticle.getTitle()!="N/A"&&
                                    currentArticle.getAuthor()!="N/A")
                                articles.add(currentArticle);
                            inEntry = false;
                        }
                        if (tag.equalsIgnoreCase("title"))
                            currentArticle.setTitle(textValue);
                        else if (tag.equalsIgnoreCase("link"))
                            currentArticle.setLink(textValue);
                        else if (tag.equalsIgnoreCase("photo"))
                            currentArticle.setPhoto(textValue);
                        else if (tag.equalsIgnoreCase("description"))
                            currentArticle.setDescription(textValue);
                        else if (tag.equalsIgnoreCase("date"))
                            currentArticle.setDate(textValue);
                        else if (tag.equalsIgnoreCase("writer"))
                            currentArticle.setAuthor(textValue);
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }
}
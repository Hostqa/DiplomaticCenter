package qa.dcsdr.diplomaticclub.Tools;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Article;

public class ParseSearch {

    private Context context;
    private String data;
    private ArrayList<Article> articles;


    public ParseSearch(String xmlData, Context context) {
        this.data = xmlData;
        this.articles = new ArrayList<Article>();
        this.context=context;
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public boolean processXml() {
        boolean status = true;
        Article currentArticle = null;
        boolean inEntry = false;
        boolean inSearch = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();
            String item="item";
            String search="search";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                        String[] s = tag.toLowerCase().split("-");
                        int id = Integer.parseInt(s[1]);
                        currentArticle = new Article(Integer.parseInt(s[1]));
                        currentArticle.setId(id);
                    } else if (tag.toLowerCase().contains(search.toLowerCase())) {
                        inSearch = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inSearch) {
                        if (inEntry) {
                            if (tag.toLowerCase().contains(item.toLowerCase())) {
                                    articles.add(currentArticle);
                                inEntry = false;
                            }
                            if (tag.equalsIgnoreCase("title")) {
                                currentArticle.setTitle(textValue);
                            }
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
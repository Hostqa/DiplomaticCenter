package qa.dcsdr.diplomaticclub.Tools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Article;
import qa.dcsdr.diplomaticclub.Items.Author;

/**
 * Created by Tamim on 6/26/2015.
 * This is the parsing factory that does all the XML parsing for the app.
 */
public class ParsingFactory {

    final int AUTHOR = 0;

    private String data;

    private ArrayList<Article> articles;
    private ArrayList<Author> authors;

    public ParsingFactory(String xmlData, int type) {
        this.data = xmlData;
        if (type == AUTHOR)
            this.authors = new ArrayList<>();
        else {
            this.articles = new ArrayList<>();
        }
    }

    public ArrayList<Article> getArticles() {
        return articles;
    }

    public ArrayList<Author> getAuthors() {
        return authors;
    }

    public boolean processSearchOrFeaturedXml(boolean isFeatured) {
        boolean status = true;
        Article currentArticle = null;
        boolean inEntry = false;
        boolean inKey = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();
            String item = "item";
            String key = isFeatured ? "post" : "search";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                        String[] s = tag.toLowerCase().split("-");
                        int id = Integer.parseInt(s[1]);
                        currentArticle = new Article(Integer.parseInt(s[1]));
                        currentArticle.setId(id);
                    } else if (tag.toLowerCase().contains(key.toLowerCase())) {
                        inKey = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inKey) {
                        if (inEntry) {
                            if (tag.toLowerCase().contains(item.toLowerCase())) {
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
                }
                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }
        return status;
    }

    public boolean processAuthorSearchXml() {
        boolean status = true;
        Author currentAuthor = null;
        boolean inEntry = false;
        boolean inKey = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();
            String item = "item";
            String key = "search";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                        currentAuthor = new Author();
                    } else if (tag.toLowerCase().contains(key.toLowerCase())) {
                        inKey = true;
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();
                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inKey) {
                        if (inEntry) {
                            if (tag.toLowerCase().contains(item.toLowerCase())) {
                                authors.add(currentAuthor);
                                inEntry = false;
                            }
                            if (tag.equalsIgnoreCase("writer"))
                                currentAuthor.setTitle(textValue);
                            else if (tag.equalsIgnoreCase("writerPic"))
                                currentAuthor.setPhoto(textValue);
                            else if (tag.equalsIgnoreCase("writerDesc"))
                                currentAuthor.setDescription(textValue);
                            else if (tag.equalsIgnoreCase("writerID"))
                                currentAuthor.setId(Integer.valueOf(textValue));
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

    public boolean processAuthorXml() {
        boolean status = true;
        Author currentAuthor = null;
        boolean inEntry = false;
        String textValue = "";
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(this.data));
            int eventType = xpp.getEventType();
            String item = "item";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                        currentAuthor = new Author();
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inEntry) {
                        if (tag.toLowerCase().contains(item.toLowerCase())) {
                            authors.add(currentAuthor);
                            inEntry = false;
                        }
                        if (tag.equalsIgnoreCase("writerName"))
                            currentAuthor.setTitle(textValue);
                        else if (tag.equalsIgnoreCase("writerPic"))
                            currentAuthor.setPhoto(textValue);
                        else if (tag.equalsIgnoreCase("writerDesc"))
                            currentAuthor.setDescription(textValue);
                        else if (tag.equalsIgnoreCase("writerID"))
                            currentAuthor.setId(Integer.valueOf(textValue));
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
            String item = "item";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tag = xpp.getName();
                if (eventType == XmlPullParser.START_TAG) {
                    if (tag.toLowerCase().contains(item.toLowerCase())) {
                        inEntry = true;
                        String[] s = tag.toLowerCase().split("-");
                        int id = Integer.parseInt(s[1]);
                        currentArticle = new Article(Integer.parseInt(s[1]));
                        currentArticle.setId(id);
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    textValue = xpp.getText();

                } else if (eventType == XmlPullParser.END_TAG) {
                    if (inEntry) {
                        if (tag.toLowerCase().contains(item.toLowerCase())) {
                            if (!currentArticle.getTitle().equals("N/A") &&
                                    !currentArticle.getAuthor().equals("N/A"))
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

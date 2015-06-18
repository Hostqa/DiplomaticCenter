package qa.dcsdr.diplomaticclub.Tools;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

import qa.dcsdr.diplomaticclub.Items.Author;

public class ParseAuthor {

    private String data;
    private ArrayList<Author> authors;

    public ParseAuthor(String xmlData) {
        this.data = xmlData;
        this.authors = new ArrayList<Author>();
    }


    public ArrayList<Author> getAuthors () {
        return authors;
    }

    public boolean processXml() {
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

            String item="item";
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
}
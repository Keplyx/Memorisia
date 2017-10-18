package com.clubinfo.insat.memorisia.xmlparsers;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class ModuleXmlParser {
    
    public String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "text");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "text");
        return name;
    }
    
    public boolean readNotifications(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "notifications");
        String notifications = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "notifications");
        return Boolean.parseBoolean(notifications);
    }
    
    // For the tags title and summary, extracts their text values.
    public String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
    
}

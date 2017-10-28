package com.clubinfo.insat.memorisia.xmlparsers;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

/**
 * Class providing general methods to parse a {@link com.clubinfo.insat.memorisia.modules.Module Module} xml file
 */
public class ModuleXmlParser {
    
    /**
     * Reads the string property of a given tag
     *
     * @param parser Reference to the xml parser
     * @param tag    Tag to read the property from
     * @return String representing the property
     */
    public String readStringProperty(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return value;
    }
    
    /**
     * Reads the boolean property of a given tag
     *
     * @param parser Reference to the xml parser
     * @param tag    Tag to read the property from
     * @return boolean representing the property
     */
    public boolean readBooleanProperty(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, tag);
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, tag);
        return Boolean.parseBoolean(value);
    }
    
    /**
     * Gets the text of the next parsing event
     *
     * @param parser Reference to the xml parser
     * @return String of the read text
     */
    public String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    /**
     * Skips the next tag
     *
     * @param parser Reference to the xml parser
     */
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

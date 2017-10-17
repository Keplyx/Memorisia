package com.clubinfo.insat.memorisia;


import android.util.Log;
import android.util.Xml;

import com.clubinfo.insat.memorisia.modules.OptionModule;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ModuleXmlParser {
    
    public static String OPTION_START_TAG = "optionsList";
    public static String OPTION_MODULE_START_TAG = "option";
    public static String WORK_START_TAG = "worksList";
    public static String WORK_MODULE_START_TAG = "works";
    
    public List<OptionModule> parse(InputStream in, int type) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            if (parser.next() == XmlPullParser.END_DOCUMENT)
                return new ArrayList<>();
//            parser.nextTag();
            return getModulesOfType(readList(parser), type);
        } finally {
            in.close();
        }
    }
    
    private List<OptionModule> getModulesOfType(List<OptionModule> list, int type) {
        if (type < 0 || list.size() == 0)
            return list;
        List<OptionModule> modules = new ArrayList<>();
        for (OptionModule m : list) {
            if (m.getType() == type)
                modules.add(m);
        }
        return modules;
    }
    
    private List<OptionModule> readList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<OptionModule> entries = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, OPTION_START_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the module tag
            if (name.equals(OPTION_MODULE_START_TAG)) {
                entries.add(readModule(parser));
            }
        }
        Log.w("test", "Entries: " + entries.size());
        return entries;
    }
    
    
    // Parses the contents of a module. If it encounters a name, logo... hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private OptionModule readModule(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, OPTION_MODULE_START_TAG);
        int[] attributes = readModuleAttributes(parser);
        //int[] attributes = new int[] {0, 0};
        String name = null;
        String color = null;
        String logo = null;
        boolean notifications = false;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            if (parserName.equals("text")) {
                name = readName(parser);
            } else if (parserName.equals("color")) {
                color = readColor(parser);
            } else if (parserName.equals("logo")) {
                logo = readLogo(parser);
            } else if (parserName.equals("notifications")) {
                notifications = readNotifications(parser);
            } else {
                skip(parser);
            }
        }
        return new OptionModule(attributes[0], attributes[1], name, logo, color, notifications);
    }
    
    private int[] readModuleAttributes(XmlPullParser parser) throws IOException, XmlPullParserException {
        String idString = "";
        String typeString = "";
        parser.require(XmlPullParser.START_TAG, null, OPTION_MODULE_START_TAG);
        String tag = parser.getName();
        if (tag.equals(OPTION_MODULE_START_TAG)) {
            idString = parser.getAttributeValue(null, "id");
            typeString = parser.getAttributeValue(null, "type");
        }
        return new int[]{Integer.parseInt(idString), Integer.parseInt(typeString)};
    }
    
    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "text");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "text");
        return name;
    }
    
    private String readColor(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "color");
        String color = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "color");
        return color;
    }
    
    private String readLogo(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "logo");
        String logo = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "logo");
        return logo;
    }
    
    private boolean readNotifications(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "notifications");
        String notifications = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "notifications");
        return Boolean.parseBoolean(notifications);
    }
    
    // For the tags title and summary, extracts their text values.
    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }
    
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
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

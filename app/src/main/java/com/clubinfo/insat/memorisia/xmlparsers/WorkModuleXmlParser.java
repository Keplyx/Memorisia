package com.clubinfo.insat.memorisia.xmlparsers;


import android.util.Log;
import android.util.Xml;

import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WorkModuleXmlParser extends ModuleXmlParser {
    
    public static String WORK_START_TAG = "worksList";
    public static String WORK_MODULE_START_TAG = "work";
    
    public List<WorkModule> parse(InputStream in, int agenda, int subject, int workType) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(in, null);
            if (parser.next() == XmlPullParser.END_DOCUMENT)
                return new ArrayList<>();
//            parser.nextTag();
            return getModulesOfType(readList(parser), agenda, subject, workType);
        } finally {
            in.close();
        }
    }
    
    private List<WorkModule> getModulesOfType(List<WorkModule> list, int agenda, int subject, int workType) {
        if (subject < 0 || list.size() == 0)
            return list;
        List<WorkModule> modules = new ArrayList<>();
        for (WorkModule m : list) {
            if (m.getSubjectId() == subject)
                modules.add(m);
        }
        return modules;
    }
    
    private List<WorkModule> readList(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<WorkModule> entries = new ArrayList<>();
        parser.require(XmlPullParser.START_TAG, null, WORK_START_TAG);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the module tag
            if (name.equals(WORK_MODULE_START_TAG)) {
                entries.add(readModule(parser));
            }
        }
        Log.w("test", "Entries: " + entries.size());
        return entries;
    }
    
    // Parses the contents of a module. If it encounters a name, priority... hands them off
    // to their respective "read" methods for processing. Otherwise, skips the tag.
    private WorkModule readModule(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, WORK_MODULE_START_TAG);
        int[] attributes = readModuleAttributes(parser);
        //int[] attributes = new int[] {0, 0};
        String name = null;
        int priority = 0;
        boolean state = false;
        boolean notifications = false;
        
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            if (parserName.equals("text")) {
                name = readStringProperty(parser, "text");
            } else if (parserName.equals("priority")) {
                priority = readPriority(parser);
            } else if (parserName.equals("state")) {
                state = readBooleanProperty(parser, "state");
            } else if (parserName.equals("notifications")) {
                notifications = readBooleanProperty(parser, "notifications");
            } else {
                skip(parser);
            }
        }
        return new WorkModule(attributes[0], attributes[1], attributes[2], attributes[3], priority,name, notifications, state);
    }
    
    private int[] readModuleAttributes(XmlPullParser parser) throws IOException, XmlPullParserException {
        String idString = "";
        String agendaString = "";
        String subjectString = "";
        String worktypeString = "";
        parser.require(XmlPullParser.START_TAG, null, WORK_MODULE_START_TAG);
        String tag = parser.getName();
        if (tag.equals(WORK_MODULE_START_TAG)) {
            idString = parser.getAttributeValue(null, "id");
            agendaString = parser.getAttributeValue(null, "agendaId");
            subjectString = parser.getAttributeValue(null, "subjectId");
            worktypeString = parser.getAttributeValue(null, "workTypeId");
        }
        return new int[]{Integer.parseInt(idString), Integer.parseInt(agendaString),
                Integer.parseInt(subjectString), Integer.parseInt(worktypeString)};
    }
    
    public boolean readState(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "state");
        String state = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "state");
        return Boolean.parseBoolean(state);
    }
    
    private int readPriority(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "priority");
        String prio = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "priority");
        return Integer.parseInt(prio);
    }
}

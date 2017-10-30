package com.clubinfo.insat.memorisia.xmlparsers;


import android.util.Log;
import android.util.Xml;

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
    
    /**
     * Parses the works save file and extracts a {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list
     *
     * @param in       {@link java.io.InputStream InputStream} to use
     * @param agenda   Agenda id to search for. -1 to search for all
     * @param subject  Subject id to search for. -1 to search for all
     * @param workType Work type id to search for. -1 to search for all
     * @return {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list of the parsed file
     */
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
    
    
    /**
     * Gets only the {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule}s of the given
     * agenda, subject and work type a in a list
     *
     * @param list     {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list to search in
     * @param agenda   Agenda id to search for. -1 to search for all
     * @param subject  Subject id to search for. -1 to search for all
     * @param workType Work type id to search for. -1 to search for all
     * @return {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list of the given type
     */
    private List<WorkModule> getModulesOfType(List<WorkModule> list, int agenda, int subject, int workType) {
        if (subject < 0 || list.size() == 0)
            return list;
        List<WorkModule> modules = new ArrayList<>();
        for (WorkModule m : list) {
            if (m.getSubjectId() == subject && m.getAgendaId() == agenda && m.getWorkTypeId() == workType)
                modules.add(m);
        }
        return modules;
    }
    
    /**
     * Parses every modules and gets their data
     *
     * @param parser xml parser to use
     * @return {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} list of the parsed file
     */
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
        return entries;
    }
    
    /**
     * Parses the content of a module
     *
     * @param parser xml parser to use
     * @return {@link com.clubinfo.insat.memorisia.modules.WorkModule WorkModule} of the parsed module
     */
    private WorkModule readModule(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, WORK_MODULE_START_TAG);
        int[] attributes = new int[] {-1, -1, -1, -1};
        String name = "";
        int priority = 0;
        int[] date = new int[] {-1, -1, -1};
        int[] time = new int[] {-1, -1};
        boolean state = false;
        boolean notifications = false;
        
        attributes = readModuleAttributes(parser);
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String parserName = parser.getName();
            if (parserName.equals("text")) {
                    name = readStringProperty(parser, "text");
            } else if (parserName.equals("priority")) {
                    priority = readPriority(parser);
            } else if (parserName.equals("date")) {
                    date = readDate(parser);
            } else if (parserName.equals("time")) {
                    time = readTime(parser);
            } else if (parserName.equals("state")) {
                    state = readBooleanProperty(parser, "state");
            } else if (parserName.equals("notifications")) {
                    notifications = readBooleanProperty(parser, "notifications");
            } else {
                skip(parser);
            }
        }
        return new WorkModule(attributes[0], attributes[1], attributes[2], attributes[3], priority, date, time, name, notifications, state);
    }
    
    /**
     * Reads the attributes of a module (id, agenda id, subject id, and work type id)
     *
     * @param parser xml parser to use
     * @return array containing parsed data: id at index 0, agenda id at 1, subject id at 2, work type id at 3
     */
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
    
    /**
     * Reads the priority property of a module
     *
     * @param parser xml parser to use
     * @return Integer representation of the priority
     */
    private int readPriority(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "priority");
        String prio = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "priority");
        return Integer.parseInt(prio);
    }
    
    /**
     * Reads the date property of a module
     *
     * @param parser xml parser to use
     * @return Integer array representing the date
     */
    private int[] readDate(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "date");
        String date[] = readText(parser).split("/");
        parser.require(XmlPullParser.END_TAG, null, "date");
        return new int[] {Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2])};
    }
    
    /**
     * Reads the time property of a module
     *
     * @param parser xml parser to use
     * @return Integer array representing the time
     */
    private int[] readTime(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "time");
        String time[] = readText(parser).split(":");
        parser.require(XmlPullParser.END_TAG, null, "time");
        return new int[] {Integer.parseInt(time[0]), Integer.parseInt(time[1])};
    }
}

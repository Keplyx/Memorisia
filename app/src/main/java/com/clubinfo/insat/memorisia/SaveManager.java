package com.clubinfo.insat.memorisia;


import android.content.Context;
import android.util.Log;
import android.util.Xml;

import com.clubinfo.insat.memorisia.modules.Module;
import com.clubinfo.insat.memorisia.modules.OptionModule;
import com.clubinfo.insat.memorisia.modules.WorkModule;
import com.clubinfo.insat.memorisia.xmlparsers.OptionModuleXmlParser;
import com.clubinfo.insat.memorisia.xmlparsers.WorkModuleXmlParser;

import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    
    public static final int SUBJECT = 0;
    public static final int WORK_TYPE = 1;
    public static final int AGENDA = 2;
    private static final String MODULES_FILENAME = "modules.xml";
    private static final String WORKS_FILENAME = "works.xml";
    private Context context;
    
    public SaveManager(Context context) {
        this.context = context;
    }
    
    /**
     * Gets a new OptionModule list of the given type (subject, work type, or agenda).
     *
     * @param type Module type. Choose with constants.
     * @return OptionModule list of the given type
     */
    public List<OptionModule> getOptionModuleList(int type) {
        List<OptionModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + MODULES_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            FileInputStream fis = context.openFileInput(MODULES_FILENAME);
            modules = new OptionModuleXmlParser().parse(fis, type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    /**
     * Gets a new WorkModule list of the given subject, work type, and agenda.
     *
     * @param agenda   Agenda id
     * @param subject  Subject id
     * @param workType Work type id
     * @return WorkModule list of the given subject, work type, and agenda
     */
    public List<WorkModule> getWorkModuleList(int agenda, int subject, int workType) {
        List<WorkModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + WORKS_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            FileInputStream fis = context.openFileInput(WORKS_FILENAME);
            modules = new WorkModuleXmlParser().parse(fis, agenda, subject, workType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    /**
     * Gets a new WorkModule list of the given subjects, work types, and agendas.
     *
     * @param agendas   Agendas ids. null for every agenda
     * @param subjects  Subjects ids. null for every subject
     * @param workTypes Work types ids. null for every work type
     * @return WorkModule list of the given subjects, work types, and agendas
     */
    public List<WorkModule> getWorkModuleList(List<Integer> agendas, List<Integer> subjects, List<Integer> workTypes) {
        List<WorkModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + WORKS_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            FileInputStream fis = context.openFileInput(WORKS_FILENAME);
            modules = new WorkModuleXmlParser().parse(fis, -1, -1, -1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        
        int counter = 0;
        while (counter < modules.size()) {
            WorkModule m = modules.get(counter);
            
            if (!((agendas == null || agendas.contains(m.getAgendaId())) &&
                    (subjects == null || subjects.contains(m.getSubjectId())) &&
                    (workTypes == null || workTypes.contains(m.getWorkTypeId())))) {
                modules.remove(counter);
            } else {
                counter++;
            }
        }
        return modules;
    }
    
    /**
     * Deletes the OptionModule of the given id.
     *
     * @param id OptionModule id
     */
    public void deleteOptionModule(int id) {
        List<OptionModule> moduleList = getOptionModuleList(-1);
        for (int i = 0; i < moduleList.size(); i++) {
            if (moduleList.get(i).getId() == id) {
                moduleList.remove(i);
                break;
            }
        }
        writeOptionModuleToXml(moduleList);
    }
    
    /**
     * Deletes the WorkModule of the given id.
     *
     * @param id WorkModule id
     */
    public void deleteWorkModule(int id) {
        List<WorkModule> moduleList = getWorkModuleList(-1, -1, -1);
        for (int i = 0; i < moduleList.size(); i++) {
            if (moduleList.get(i).getId() == id) {
                moduleList.remove(i);
                break;
            }
        }
        writeWorkModuleToXml(moduleList);
    }
    
    /**
     * Saves the OptionModule of the given id.
     *
     * @param module OptionModule to save
     */
    public void saveModule(OptionModule module) {
        List<OptionModule> moduleList = getOptionModuleList(-1);
        if (module.getId() == -1) {
            module.setId(createUniqueModuleId(moduleList));
            moduleList.add(module);
        } else
            replaceExistingModule(moduleList, module);
        writeOptionModuleToXml(moduleList);
    }
    
    /**
     * Saves the WorkModule of the given id.
     *
     * @param module WorkModule to save
     */
    public void saveModule(WorkModule module) {
        List<WorkModule> moduleList = getWorkModuleList(-1, -1, -1);
        if (module.getId() == -1) {
            module.setId(createUniqueModuleId(moduleList));
            moduleList.add(module);
        } else
            replaceExistingModule(moduleList, module);
        if (moduleList.size() != 0)
            writeWorkModuleToXml(moduleList);
    }
    
    /**
     * Overwrites the given OptionModule in the given OptionModule list.
     *
     * @param list   OptionModule list to search in
     * @param module OptionModule to overwrite
     * @return OptionModule list with the overwritten object
     */
    private List<OptionModule> replaceExistingModule(List<OptionModule> list, OptionModule module) {
        for (int i = 0; i < list.size(); i++) {
            if (module.getId() == list.get(i).getId()) {
                list.remove(i);
                list.add(module);
                break;
            }
        }
        return list;
    }
    
    /**
     * Overwrites the given WorkModule in the given WorkModule list.
     *
     * @param list   WorkModule list to search in
     * @param module WorkModule to overwrite
     * @return WorkModule list with the overwritten object
     */
    private List<WorkModule> replaceExistingModule(List<WorkModule> list, WorkModule module) {
        for (int i = 0; i < list.size(); i++) {
            if (module.getId() == list.get(i).getId()) {
                list.remove(i);
                list.add(module);
                break;
            }
        }
        return list;
    }
    
    /**
     * Creates an unique id based on the given Module list.
     * The id is a number (starting at 0) which is not used by any other Module in the list.
     * The user will never see this id, it is only used to identify a module.
     *
     * @param modules Module list to search in
     * @return unique id generated. -1 if no id could be generated
     */
    private int createUniqueModuleId(List<?> modules) {
        for (int i = 0; i < modules.size() + 1; i++) {
            boolean valid = true;
            for (int k = 0; k < modules.size(); k++) {
                Module m = (Module) modules.get(k);
                if (m.getId() == i) {
                    valid = false;
                    break;
                }
            }
            if (valid)
                return i;
        }
        return -1;
    }
    
    /**
     * Writes the given OptionModule list to an xml file.
     *
     * @param list OptionModule list to save
     */
    private void writeOptionModuleToXml(List<OptionModule> list) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(MODULES_FILENAME, Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, OptionModuleXmlParser.OPTION_START_TAG);
            for (int i = 0; i < list.size(); i++) {
                serializer = writeModule(serializer, list.get(i));
            }
            serializer.endTag(null, OptionModuleXmlParser.OPTION_START_TAG);
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Writes the given WorkModule list to an xml file.
     *
     * @param list WorkModule list to save
     */
    private void writeWorkModuleToXml(List<WorkModule> list) {
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(WORKS_FILENAME, Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, WorkModuleXmlParser.WORK_START_TAG);
            for (int i = 0; i < list.size(); i++) {
                serializer = writeModule(serializer, list.get(i));
            }
            serializer.endTag(null, WorkModuleXmlParser.WORK_START_TAG);
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Serializes the given OptionModule to xml.
     *
     * @param serializer XmlSerializer to use
     * @param module     OptionModule to save
     */
    private XmlSerializer writeModule(XmlSerializer serializer, OptionModule module) {
        try {
            serializer.startTag(null, OptionModuleXmlParser.OPTION_MODULE_START_TAG);
            serializer.attribute(null, "id", Integer.toString(module.getId()));
            serializer.attribute(null, "type", Integer.toString(module.getType()));
            serializer.startTag(null, "text");
            serializer.text(module.getText());
            serializer.endTag(null, "text");
            serializer.startTag(null, "color");
            serializer.text(module.getColor());
            serializer.endTag(null, "color");
            serializer.startTag(null, "logo");
            serializer.text(module.getLogo());
            serializer.endTag(null, "logo");
            serializer.startTag(null, "notifications");
            serializer.text(Boolean.toString(module.isNotificationsEnabled()));
            serializer.endTag(null, "notifications");
            serializer.endTag(null, OptionModuleXmlParser.OPTION_MODULE_START_TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializer;
    }
    
    /**
     * Serializes the given WorkModule to xml.
     *
     * @param serializer XmlSerializer to use
     * @param module     WorkModule to save
     */
    private XmlSerializer writeModule(XmlSerializer serializer, WorkModule module) {
        try {
            serializer.startTag(null, WorkModuleXmlParser.WORK_MODULE_START_TAG);
            serializer.attribute(null, "id", Integer.toString(module.getId()));
            serializer.attribute(null, "agendaId", Integer.toString(module.getAgendaId()));
            serializer.attribute(null, "subjectId", Integer.toString(module.getSubjectId()));
            serializer.attribute(null, "workTypeId", Integer.toString(module.getWorkTypeId()));
            serializer.startTag(null, "text");
            serializer.text(module.getText());
            serializer.endTag(null, "text");
            serializer.startTag(null, "priority");
            serializer.text(Integer.toString(module.getPriority()));
            serializer.endTag(null, "priority");
            serializer.startTag(null, "date");
            serializer.text("");
            serializer.endTag(null, "date");
            serializer.startTag(null, "state");
            serializer.text(Boolean.toString(module.isState()));
            serializer.endTag(null, "state");
            serializer.startTag(null, "notifications");
            serializer.text(Boolean.toString(module.isNotificationsEnabled()));
            serializer.endTag(null, "notifications");
            serializer.endTag(null, WorkModuleXmlParser.WORK_MODULE_START_TAG);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serializer;
    }
    
    /**
     * Reads the given file and prints its content to the Log
     * Only for debug purposes
     *
     * @param context  Current context
     * @param fileName File to read
     */
    private void readFile(Context context, String fileName) {
        try {
            InputStream inputStream = context.openFileInput(fileName);
            
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;
                Log.w("test", "Document Start");
                while ((receiveString = bufferedReader.readLine()) != null) {
                    Log.w("test", receiveString);
                }
                inputStream.close();
                Log.w("test", "Document End");
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }
    }
}
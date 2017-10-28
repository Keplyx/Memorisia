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
    public static final int WORKTYPE = 1;
    public static final int AGENDA = 2;
    private static final String MODULES_FILENAME = "modules.xml";
    private static final String WORKS_FILENAME = "works.xml";
    private Context context;
    
    public SaveManager(Context context) {
        this.context = context;
    }
    
    
    public List<OptionModule> getOptionModuleList(int type) {
        List<OptionModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + MODULES_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            readFile(context, MODULES_FILENAME);
            FileInputStream fis = context.openFileInput(MODULES_FILENAME);
            modules = new OptionModuleXmlParser().parse(fis, type);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    public List<WorkModule> getWorkModuleList(int agenda, int subject, int workType) {
        List<WorkModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + WORKS_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            readFile(context, WORKS_FILENAME);
            FileInputStream fis = context.openFileInput(WORKS_FILENAME);
            modules = new WorkModuleXmlParser().parse(fis, agenda, subject, workType);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    public List<WorkModule> getWorkModuleList(List<Integer> agendas, List<Integer> subjects, List<Integer> workTypes) {
        List<WorkModule> modules = new ArrayList<>();
        try {
            File file = new File(context.getFilesDir().getPath() + "/" + WORKS_FILENAME);
            if (!file.exists())
                return new ArrayList<>();
            readFile(context, WORKS_FILENAME);
            FileInputStream fis = context.openFileInput(WORKS_FILENAME);
            modules = new WorkModuleXmlParser().parse(fis, -1, -1, -1);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        Log.w("test", "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        Log.w("test", "SELECTED :");
        Log.w("test", "SIZE " + modules.size());
        if (agendas != null) {
            for (int i = 0; i < agendas.size(); i++) {
                Log.w("test", "AGENDA_ID " + agendas.get(i));
            }
        }
        else
            Log.w("test", "AGENDA_ID ALL");
        if (subjects != null) {
            for (int i = 0; i < subjects.size(); i++) {
                Log.w("test", "SUBJECT_ID " + subjects.get(i));
            }
        }
        else
            Log.w("test", "SUBJECT_ID ALL");
        if (workTypes != null) {
            for (int i = 0; i < workTypes.size(); i++) {
                Log.w("test", "WORK_TYPE_ID " + workTypes.get(i));
            }
        }
        else
            Log.w("test", "WORK_TYPE_ID ALL");
    
        int counter = 0;
        while (counter < modules.size()) {
            WorkModule m = modules.get(counter);
            
            if (!((agendas == null || agendas.contains(m.getAgendaId())) &&
                    (subjects == null || subjects.contains(m.getSubjectId())) &&
                    (workTypes == null || workTypes.contains(m.getWorkTypeId())))) {
                modules.remove(counter);
                Log.w("test", "REMOVED :");
                Log.w("test", "AGENDA_ID " + m.getAgendaId());
                Log.w("test", "SUBJECT_ID " + m.getSubjectId());
                Log.w("test", "WORK_TYPE_ID " + m.getWorkTypeId());
                Log.w("test", "-----");
            }
            else {
                Log.w("test", "KEPT :");
                Log.w("test", "AGENDA_ID " + m.getAgendaId());
                Log.w("test", "SUBJECT_ID " + m.getSubjectId());
                Log.w("test", "WORK_TYPE_ID " + m.getWorkTypeId());
                Log.w("test", "********");
                counter++;
            }
        }
        Log.w("test", "^^^^^^^^^^^^^");
        Log.w("test", "SIZE " + modules.size());
        return modules;
    }
    
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
    
    public void saveModule(OptionModule module) {
        List<OptionModule> moduleList = getOptionModuleList(-1);
        if (module.getId() == -1) {
            module.setId(createUniqueModuleId(moduleList));
            moduleList.add(module);
        } else
            replaceExistingModule(moduleList, module);
        writeOptionModuleToXml(moduleList);
    }
    
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
    
    
    private void readFile(Context context, String fileName) {
//        try {
//            InputStream inputStream = context.openFileInput(fileName);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString;
//                Log.w("test", "Document Start");
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    Log.w("test", receiveString);
//                }
//                inputStream.close();
//                Log.w("test", "Document End");
//            }
//        } catch (FileNotFoundException e) {
//            Log.e("login activity", "File not found: " + e.toString());
//        } catch (IOException e) {
//            Log.e("login activity", "Can not read file: " + e.toString());
//        }
    }
}
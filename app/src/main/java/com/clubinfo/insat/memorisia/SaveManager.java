package com.clubinfo.insat.memorisia;


import android.content.Context;
import android.util.Xml;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {
    
    private static final String modulesFilename = "modules.xml";
    private Context context;
    
    public SaveManager(Context context) {
        this.context = context;
    }
    
    public List<OptionModule> getModuleList(int type){
        List<OptionModule> modules = new ArrayList<>();
        try {
            FileInputStream fis = context.openFileInput(modulesFilename);
            modules = new ModuleXmlParser().parse(fis, type);
//            for (OptionModule m:modules) {
//                Log.w("test", "--------------------------");
//                Log.w("test", "name: " + m.getName());
//                Log.w("test", "id: " + m.getId());
//                Log.w("test", "type: " + m.getType());
//                Log.w("test", "color: " + m.getColor());
//                Log.w("test", "logo: " + m.getLogo());
//                Log.w("test", "notifications: " + m.isNotificationsEnabled());
//            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return modules;
    }
    
    public void deleteOptionModule(int id){
        List<OptionModule> moduleList = getModuleList(-1);
        for (int i = 0; i < moduleList.size(); i++){
            if (moduleList.get(i).getId() == id){
                moduleList.remove(i);
                break;
            }
        }
        writeToXml(moduleList);
    }
    
    public void saveOptionModule(OptionModule module) {
        List<OptionModule> moduleList = getModuleList(-1);
        if (module.getId() == -1) {
            module.setId(createUniqueId(moduleList));
            moduleList.add(module);
        } else
            replaceExisting(moduleList, module);
        writeToXml(moduleList);
    }
    
    private List<OptionModule> replaceExisting(List<OptionModule> list, OptionModule module){
        for (int i = 0; i < list.size(); i++){
            if (module.getId() == list.get(i).getId()){
                list.remove(i);
                list.add(module);
                break;
            }
        }
        return list;
    }
    
    private int createUniqueId(List<OptionModule> modules){
        for (int i = 0; i < modules.size(); i++) {
            boolean valid = true;
            for (OptionModule m : modules) {
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
    
    private void writeToXml(List<OptionModule> list){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(modulesFilename, Context.MODE_PRIVATE);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fos, "UTF-8");
            serializer.startDocument(null, true);
            serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output", true);
            serializer.startTag(null, "modulesList");
            for (OptionModule m: list) {
                serializer = writeModule(serializer, m);
            }
            serializer.endDocument();
            serializer.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private XmlSerializer writeModule(XmlSerializer serializer, OptionModule module){
        try {
            serializer.startTag(null, "module");
            serializer.attribute(null, "id", Integer.toString(module.getId()));
            serializer.attribute(null, "type", Integer.toString(module.getType()));
            serializer.startTag(null, "name");
            serializer.text(module.getName());
            serializer.endTag(null, "name");
            serializer.startTag(null, "color");
            serializer.text(module.getColor());
            serializer.endTag(null, "color");
            serializer.startTag(null, "logo");
            serializer.text(module.getLogo());
            serializer.endTag(null, "logo");
            serializer.startTag(null, "notifications");
            serializer.text(Boolean.toString(module.isNotificationsEnabled()));
            serializer.endTag(null, "notifications");
            serializer.endTag(null, "module");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return serializer;
    }
    
}
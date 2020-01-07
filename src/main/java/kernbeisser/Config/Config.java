package kernbeisser.Config;

import jdk.nashorn.internal.ir.debug.JSONWriter;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;

public class Config {
    private static File file = new File("config.txt");
    private static HashMap<String,Config> configs;


    static {
        createFileIfNotExists();

    }

    private static String fileToString(File f, Charset charset) {
        try {
            StringBuilder sb = new StringBuilder();
            Files.readAllLines(f.toPath(), charset).forEach(sb.append("\n")::append);
            return sb.toString();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

    private static void createFileIfNotExists(){
        if(file.exists())return;
        JSONObject object = new JSONObject();
        JSONObject dbAccess = new JSONObject();
        dbAccess.put("Username","");
        dbAccess.put("Password","");
        object.put("DBAccess",dbAccess);
        object.put("Init",false);
        try {
            if(file.createNewFile()) {
                FileWriter fw = new FileWriter(file);
                fw.write(object.toString());
                fw.close();
            }
        }catch (IOException e){
            file.delete();
            e.printStackTrace();
        }

    }
}
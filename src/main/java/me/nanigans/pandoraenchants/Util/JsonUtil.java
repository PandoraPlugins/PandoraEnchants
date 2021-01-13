package me.nanigans.pandoraenchants.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import me.nanigans.pandoraenchants.PandoraEnchants;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    private static final PandoraEnchants plugin = PandoraEnchants.getPlugin(PandoraEnchants.class);
    private static GsonBuilder gsonBuilder = new GsonBuilder()
            .registerTypeAdapter(new TypeToken<Map<String, Object>>(){}.getType(),  new CustomizedObjectTypeAdapter());
    private static HashMap map = new HashMap<>();

    /**
     * Gets json data from the specified file and path in that file
     * @param file the path to the json file. Not the full path, just the name in the resource file +.json
     * @param path the path of the data to get within the file separated with a .
     * @return the data found at the path in the file
     */
    public static <T> T getData(String file, String path) {

        final File jsonPath = new File(plugin.getDataFolder(), file);
        if(!jsonPath.exists()){
            makeConfigFile(jsonPath);
        }

        try {
            JSONParser jsonParser = new JSONParser();
            Object parsed = jsonParser.parse(new FileReader(jsonPath));
            JSONObject jsonObject = (JSONObject) parsed;

            JSONObject currObject = (JSONObject) jsonObject.clone();
            if(path == null) return (T) currObject;
            String[] paths = path.split("\\.");

            for (String s : paths) {

                if (currObject.get(s) instanceof JSONObject)
                    currObject = (JSONObject) currObject.get(s);
                else return (T) currObject.get(s);

            }

            return (T) currObject;
        }catch(IOException | ParseException ignored){
            return null;
        }
    }


    public static void makeConfigFile(File file){

        plugin.saveResource(file.getName(), false);
        try{
            Gson gson = gsonBuilder.create();
            map = gson.fromJson(new FileReader(file), HashMap.class);
        }catch(IOException e){
            e.printStackTrace();
        }

    }


}

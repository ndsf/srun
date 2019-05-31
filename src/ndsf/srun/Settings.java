package ndsf.srun;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class Settings {
    private static boolean rememberPassword;
    private static boolean autoConnect;

    public static void save() {
        // write to file
        try (Writer writer = new FileWriter("Settings.json")) {
            //Gson gson = new GsonBuilder().create();
            //gson
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

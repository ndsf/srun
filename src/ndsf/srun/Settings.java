package ndsf.srun;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 设置类，单例模式
 */
public class Settings
{
    // singleton class

    private static Settings instance = new Settings();

    public static Settings getInstance()
    {
        return instance;
    }

    // filename of the settings file

    private static final String FILENAME = "Settings.json";

    // getter & setter of settings

    public boolean isRememberPassword()
    {
        return rememberPassword;
    }

    public void setRememberPassword(boolean rememberPassword)
    {
        this.rememberPassword = rememberPassword;

        // save the settings to file after changed

        save();
    }

    public boolean isAutoConnect()
    {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect)
    {
        this.autoConnect = autoConnect;

        // save the settings to file after changed

        save();
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;

        // save the settings to file after changed

        save();
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;

        // save the settings to file after changed

        save();
    }

    // variables of settings

    private String username = "";
    private String password = "";
    private boolean rememberPassword = false;
    private boolean autoConnect = false;

    /**
     * 初始化设置类
     * 设置用户名、密码、记住密码和自动登录等属性
     */
    private Settings()
    {
        // create file if not exists

        File file = new File(FILENAME);
        if (!file.exists())
        {
            save();
            return;
        }

        // load from file

        try (Reader reader = new FileReader(FILENAME))
        {
            // extract settings from Settings.json file

            Gson gson = new Gson();

            Map<String, Object> map = new HashMap<>();
            map = gson.fromJson(reader, map.getClass());

            //System.out.println(map);
            //System.out.println(map.get
            // ("rememberPassword").getClass()
            // .getName());

            // retrieve data from the map

            this.username = (String) map.get("username");
            this.password = (String) map.get("password");
            this.rememberPassword = (boolean) map.get(
                    "rememberPassword");
            this.autoConnect = (boolean) map.get(
                    "autoConnect");
        } catch (IOException ex)
        {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 保存设置
     */
    private void save()
    {
        // write to file

        try (Writer writer = new FileWriter(FILENAME))
        {
            // write settings to file through json

            Gson gson = new Gson();
            gson.toJson(this, writer);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

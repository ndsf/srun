package ndsf.srun;

import com.google.gson.Gson;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * 流量记录类，单例模式
 */
public class Statistics
{
    // filename of the time file

    private static final String TIMEFILENAME = "Times.json";

    // filename of the usage file

    private static final String DOUBLEFILENAME =
            "Statistics.json";

    // set the threshold of gigabytes between each double

    private static final double THRESHOLD = 1;

    // set the limit of records

    private static final int LIMIT = 30;

    /**
     * 当前本地时间
     *
     * @return 返回当前本地时间
     */
    public ArrayList<String> getLocalDateTimeArrayList()
    {
        return localDateTimeArrayList;
    }

    public void setLocalDateTimeArrayList(ArrayList<String> localDateTimeArrayList)
    {
        this.localDateTimeArrayList =
                localDateTimeArrayList;
    }

    /**
     * 当前剩余流量
     *
     * @return 返回当前剩余流量
     */
    public ArrayList<Double> getDoubleArrayList()
    {
        return doubleArrayList;
    }

    public void setDoubleArrayList(ArrayList<Double> doubleArrayList)
    {
        this.doubleArrayList = doubleArrayList;
    }

    // variables of array lists

    private ArrayList<String> localDateTimeArrayList =
            new ArrayList<>();
    private ArrayList<Double> doubleArrayList =
            new ArrayList<>();

    // singleton class

    private static Statistics instance = new Statistics();

    /**
     * 获取流量记录类实例
     *
     * @return Statistics的实例
     */
    public static Statistics getInstance()
    {
        return instance;
    }

    /**
     * 清空数据
     */
    public void clear()
    {
        // clear both lists

        localDateTimeArrayList.clear();
        doubleArrayList.clear();
    }

    public void add(Double number)
    {
        if (doubleArrayList.isEmpty() || Math.abs(doubleArrayList.get(doubleArrayList.size() - 1) - number) >= THRESHOLD)
        {
            localDateTimeArrayList.add(LocalDateTime.now().toString());
            doubleArrayList.add(number);
            save();
        }

        // clear the array lists if there are too many records

        if (doubleArrayList.size() > LIMIT)
        {
            clear();
        }
    }

    /**
     * 初始化流量记录类
     * 设定当前时间和剩余流量
     */
    private Statistics()
    {
        // create file if not exists through Java File

        File timeFile = new File(TIMEFILENAME);
        File doubleFile = new File(DOUBLEFILENAME);
        if (!timeFile.exists() || !doubleFile.exists())
        {
            save();
            return;
        }

        // load time statistics from file through a reader

        try (Reader reader = new FileReader(TIMEFILENAME))
        {
            Gson gson = new Gson();

            // extract the statistics from json

            localDateTimeArrayList = gson.fromJson(reader,
                    localDateTimeArrayList.getClass());
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        // load usage statistics from file through a reader

        try (Reader reader = new FileReader(DOUBLEFILENAME))
        {
            Gson gson = new Gson();

            // extract the statistics from json

            doubleArrayList = gson.fromJson(reader,
                    doubleArrayList.getClass());
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * 保存流量信息
     */
    private void save()
    {
        // write to file through a writer

        // write the times statistics to json

        try (Writer writer = new FileWriter(TIMEFILENAME))
        {
            Gson gson = new Gson();
            gson.toJson(this.localDateTimeArrayList,
                    writer);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

        // write the usage statistics to json

        try (Writer writer = new FileWriter(DOUBLEFILENAME))
        {
            Gson gson = new Gson();
            gson.toJson(this.doubleArrayList, writer);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
    }
}

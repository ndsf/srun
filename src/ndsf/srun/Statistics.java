package ndsf.srun;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import javafx.util.Pair;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Statistics
{

    private static final String TIMEFILENAME = "Times.json";
    private static final String DOUBLEFILENAME =
            "Statistics.json";
    private static final double THRESHOLD = 1;

    public ArrayList<String> getLocalDateTimeArrayList()
    {
        return localDateTimeArrayList;
    }

    public void setLocalDateTimeArrayList(ArrayList<String> localDateTimeArrayList)
    {
        this.localDateTimeArrayList =
                localDateTimeArrayList;
    }

    public ArrayList<Double> getDoubleArrayList()
    {
        return doubleArrayList;
    }

    public void setDoubleArrayList(ArrayList<Double> doubleArrayList)
    {
        this.doubleArrayList = doubleArrayList;
    }

    private ArrayList<String> localDateTimeArrayList =
            new ArrayList<>();
    private ArrayList<Double> doubleArrayList =
            new ArrayList<>();

    private static Statistics instance = new Statistics();

    public static Statistics getInstance()
    {
        return instance;
    }

    public void clear()
    {
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
    }

    private Statistics()
    {
        // create file if not exists

        File timeFile = new File(TIMEFILENAME);
        File doubleFile = new File(DOUBLEFILENAME);
        if (!timeFile.exists() || !doubleFile.exists())
        {
            save();
            return;
        }

        // load from file

        try (Reader reader = new FileReader(TIMEFILENAME))
        {
            Gson gson = new Gson();
            localDateTimeArrayList = gson.fromJson(reader,
                    localDateTimeArrayList.getClass());
        } catch (IOException ex)
        {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex)
        {
            ex.printStackTrace();
        }

        try (Reader reader = new FileReader(DOUBLEFILENAME))
        {
            Gson gson = new Gson();
            doubleArrayList = gson.fromJson(reader,
                    doubleArrayList.getClass());
        } catch (IOException ex)
        {
            ex.printStackTrace();
        } catch (JsonSyntaxException ex)
        {
            ex.printStackTrace();
        }
    }

    private void save()
    {
        // write to file
        try (Writer writer = new FileWriter(TIMEFILENAME))
        {
            Gson gson = new Gson();
            gson.toJson(this.localDateTimeArrayList,
                    writer);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }

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

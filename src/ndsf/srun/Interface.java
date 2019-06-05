package ndsf.srun;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 程序运行类
 */
public class Interface extends Application
{

    // interval between automatic connection

    private static final long PERIOD = 1000 * 60 * 30;

    public static void main(String[] args)
    {
        if (args.length == 0)
        {
            // start the Javafx GUI

            launch(args);
        } else if (args.length == 2 && args[1].equals("logout"))
        {
            // logout if the parameter is provided

            Scanner in = new Scanner(System.in);
            System.out.println("请输入账号：");
            String username = in.next();
            try
            {
                Srun.logout(username);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        } else if (args[0].equals("cli"))
        {
            // start the command line interface

            // input username & password

            Scanner in = new Scanner(System.in);
            System.out.println("请输入账号：");
            String username = in.next();
            System.out.println("请输入密码：");
            String password = in.next();

            // try to login

            try
            {

                // retrieve the map from json

                Map<String, String> map = Srun.login(username, password);

                // print retrieved informations

                System.out.println
                        ("套餐名称：" + map.get("billing_name"));
                System.out.println
                        ("已用流量：" + map.get("acount_used_bytes"));
                System.out.println
                        ("剩余流量：" + map.get("acount_remain_bytes"));
                System.out.println
                        ("账户流量：" + map.get("acount_all_bytes"));

                // start a timer task to connect automatically

                Timer timer = new Timer(true);
                TimerTask timerTask = new LoginTimerTask(username, password);
                timer.scheduleAtFixedRate(timerTask, PERIOD, PERIOD);
            } catch (IOException ex)
            {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // create a fxml loader

        // if Maven is imported, the name parameter should be changed

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource(
                        "srun.fxml"));

        // set up a stage

        Parent root = loader.load();
        primaryStage.setTitle("Srun");
        primaryStage.setScene(new Scene(root, 335, 600));
        primaryStage.getIcons().add(new Image(getClass().getResource("favicon" +
                ".png").toExternalForm()));

        // get the controller through loader

        Controller controller = loader.getController();

        // init the controller

        controller.init();

        // show the stage

        primaryStage.show();
    }
}

package ndsf.srun;

import java.io.IOException;
import java.util.TimerTask;

/**
 * 定时登录，防止掉线类
 */
public class LoginTimerTask extends TimerTask
{

    private Controller controller;

    public LoginTimerTask(Controller controller)
    {
        this.controller = controller;
    }

    @Override
    public void run()
    {
        //if (controller.getLoginButton().isDisable()) {
        String username =
                controller.getUsernameField().getText();
        String password =
                controller.getPasswordField().getText();
        try
        {
            Srun.login(username, password);
        } catch (IOException ex)
        {
            ex.printStackTrace();
        }
        //}
    }
}

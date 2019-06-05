package ndsf.srun;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.gson.Gson;

/**
 * 主程序类
 *
 * @author ndsf, limir
 * @since 2019.5.26
 */
public class Srun
{
    private static final String USER_AGENT = "Mozilla/5.0" + " (Macintosh; " +
            "Intel " + "Mac OS X 10_14_3) AppleWebKit/537.36 " + "(KHTML, " +
            "like Gecko) Chrome/73.0.3683.86 " + "Safari/537.36";

    public static void main(String[] args)
    {
    }

    /**
     * 登录，输入用户名和密码
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回用户名和密码的映射
     * @throws IOException IO异常
     */
    public static Map<String, String> login(String username, String password) throws IOException
    {
        // url of login page

        String url = "https://gw.buaa.edu.cn:802/srun_portal_phone" +
                ".php?ac_id=22";

        // patameters of post

        String urlParameters = "action=login&ac_id=22&user_ip=&nas_ip" +
                "=&user_mac=&username=" + username + "&password=" + password;

        // call the send post method to send a post

        sendPost(url, urlParameters);

        // get the user's UID via crawling the page
        // and get information through ajax

        return getAjax(getUid());
    }

    /**
     * 获取用户信息
     *
     * @param username 用户名
     * @param password 密码
     * @return 返回json解析出的map
     * @throws IOException IO异常
     */
    public static Map<String, String> getInformation(String username,
                                                     String password) throws IOException
    {
        // simply crawling the information, thus no need to login

        // get the user's UID via crawling the page
        // and get information through ajax

        return getAjax(getUid());
    }

    /**
     * 登出
     *
     * @param username 用户名
     * @throws IOException IO异常
     */
    public static void logout(String username) throws IOException
    {
        // url of logout page

        String url = "https://gw.buaa.edu.cn:802/include/auth_action.php";

        // patameters of post

        String urlParameters = "action=logout&username=" + username + "&ajax=1";

        // call the send post method to send a post

        sendPost(url, urlParameters);
    }

    /**
     * 发送一个post请求
     *
     * @param url           发送到的地址
     * @param urlParameters 发送的参数
     * @throws IOException IO异常
     */
    private static void sendPost(String url, String urlParameters) throws IOException
    {
        // send a post by means of Java Web

        HttpURLConnection con = null;
        // byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        try
        {
            // create a Java URL instance from the string

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");

            // set the user agent the same as my computer

            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type",
                    "application/x-www-form" + "-urlencoded");

            // write the parameters to the URL

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            // print the response code

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + urlParameters);
            System.out.println("Response Code : " + responseCode);

        } finally
        {
            if (con != null)
            {
                // disconnect the connection if it's not null

                con.disconnect();
            }
        }
    }

    /**
     * 从登录后的页面爬取UID
     *
     * @return 返回UID
     * @throws IOException IO异常
     */
    private static String getUid() throws IOException
    {
        // url of UID page

        String url = "https://gw.buaa.edu.cn:804/beihangview.php";

        // crawl the content from the page

        String content = getFromUrl(url);

        // System.out.println(content);

        // try to filter UID out of the content via Java regex

        try
        {
            // create the pattern of UID

            Pattern p = Pattern.compile("(?<=uid=).*?" + "(?=&|$)");

            // create a matcher from the content & the pattern

            Matcher m = p.matcher(content);

            // return the UID if it's find

            if (m.find())
            {
                System.out.println();
                System.out.println("UID = " + m.group());
                return m.group();
            }
        } catch (PatternSyntaxException ex)
        {
            ex.printStackTrace();
        }

        // return null if the UID id not found

        return null;
    }

    /**
     * 通过Ajax获取一个json
     *
     * @param uid 发送UID
     * @return 返回从一个json解析出的map，json内容如下：
     * log("UID = " + uid);
     * log("套餐名称：" + map.get("billing_name"));
     * log("已用流量：" + map.get("acount_used_bytes"));
     * log("剩余流量：" + map.get("acount_remain_bytes"));
     * log("账户流量：" + map.get("acount_all_bytes"));
     * json完整内容如下：
     * {
     * "product_all_bytes":"1073741824",
     * "product_remain_bytes":-10887150,
     * "product_used_bytes":"1084628974",
     * "package_free_all_bytes":107374182400,
     * "package_free_remain_bytes":7633214153,
     * "package_free_used_bytes":99740968247,
     * "package_all_bytes":0,
     * "acount_all_bytes":"101.00G",
     * "acount_used_bytes":"93.90G",
     * "acount_remain_bytes":"7.10G",
     * "package_remain_bytes":0,
     * "package_used_bytes":0,
     * "per_product":101,
     * "per":100,
     * "per_free":93,
     * "checkout_mode":[
     * "2019\/05\/1\/-2019\/6\/1",
     * "2019-05-1--2019-6-1"
     * ],
     * "billing_name":"\u8282\u5047\u65e5\u514d\u8d39-\u514d
     * \u8d39\u533a\u57df
     * -\u5b66\u751f\u514d\u8d391G-",
     * "package_all":[
     * {
     * "user_id":"77382",
     * "products_id":"1",
     * "package_id":"8",
     * "billing_id":"15",
     * "user_package_id":"411369",
     * "bytes":"107374182400",
     * "seconds":"0",
     * "times":"0",
     * "remain_bytes":"7633214153",
     * "remain_seconds":"0",
     * "remain_times":"0",
     * "sum_bytes":"0",
     * "sum_seconds":"0",
     * "sum_times":"0",
     * "auto_buy":"0",
     * "add_time":"1553686194",
     * "valid_day":"0",
     * "expire_time":"0",
     * "valid_cycle":"day",
     * "valid_mode":"0",
     * "package_name":"50\u5143100G",
     * "used_bytes":99740968247
     * }
     * ]
     * }
     * @throws IOException IO错误
     */
    private static Map<String, String> getAjax(String uid) throws IOException
    {
        if (uid == null)
        {
            return null;
        }

        // the url of ajax

        String url = "https://gw.buaa.edu.cn:804/beihang.php?route" +
                "=getPackage&uid=" + uid + "&pid=1";

        // crawl the content of ajax from UID

        String content = getFromUrl(url);

        // System.out.println(content);

        // extract the information from Json string

        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map = gson.fromJson(content, map.getClass());

        // System.out.println(map);

        // return the retrieved map

        return map;
    }

    /**
     * 爬取网页内容
     *
     * @param url 网址
     * @return 返回String
     * @throws IOException IO异常
     */
    private static String getFromUrl(String url) throws IOException
    {
        // create a connection of Java Web

        HttpURLConnection con = null;
        try
        {
            // create a Java URL instance from the URL string

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            // set the method to GET

            con.setRequestMethod("GET");

            // feed the content read into a string builder

            StringBuilder content;

            try (BufferedReader in =
                         new BufferedReader(new InputStreamReader(con.getInputStream())))
            {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null)
                {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            // return the string builder's content

            return content.toString();

        } finally
        {
            if (con != null)
            {
                // disconnect the connection if it's not null

                con.disconnect();
            }
        }
    }

    /**
     * 代替println的字符串输出方法
     *
     * @param s 字符串
     */
    public static void log(String s)
    {
        // Interface.addLogLine(s);

        // can further import log4j

        System.out.println(s);
    }
}

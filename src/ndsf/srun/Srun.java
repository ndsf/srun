package ndsf.srun;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.google.gson.Gson;

public class Srun {
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.86 Safari/537.36";

    public static void main(String[] args) {
    }

    public static Map<String, String> login(String username, String password) throws IOException {
        String url = "https://gw.buaa.edu.cn:802/srun_portal_phone.php?ac_id=22";
        String urlParameters = "action=login&ac_id=22&user_ip=&nas_ip=&user_mac=&username=" + username + "&password=" + password;
        sendPost(url, urlParameters);
        return getAjax(getUid());
    }

    public static void logout(String username) throws IOException {
        String url = "https://gw.buaa.edu.cn:802/include/auth_action.php";
        String urlParameters = "action=logout&username=" + username + "&ajax=1";
        sendPost(url, urlParameters);
    }

    private static void sendPost(String url, String urlParameters) throws IOException {
        HttpURLConnection con = null;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", USER_AGENT);
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    private static String getUid() throws IOException {
        String url = "https://gw.buaa.edu.cn:804/beihangview.php";

        String content = getFromUrl(url);
        try {
            Pattern p = Pattern.compile("(?<=uid=).*?(?=&|$)");
            Matcher m = p.matcher(content);
            if (m.find()) {
                return m.group();
            }
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private static Map<String, String> getAjax(String uid) throws IOException {
        String url = "https://gw.buaa.edu.cn:804/beihang.php?route=getPackage&uid=" + uid + "&pid=1";

        String content = getFromUrl(url);
        Gson gson = new Gson();
        Map<String, String> map = new HashMap<>();
        map = gson.fromJson(content, map.getClass());

        log("UID = " + uid);
        log("套餐名称：" + map.get("billing_name"));
        log("已用流量：" + map.get("acount_used_bytes"));
        log("剩余流量：" + map.get("acount_remain_bytes"));
        log("账户流量：" + map.get("acount_all_bytes"));

        return map;
    }

    private static String getFromUrl(String url) throws IOException {
        HttpURLConnection con = null;
        try {

            URL myurl = new URL(url);
            con = (HttpURLConnection) myurl.openConnection();

            con.setRequestMethod("GET");

            StringBuilder content;

            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {

                String line;
                content = new StringBuilder();

                while ((line = in.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }

            return content.toString();

        } finally {
            if (con != null)
                con.disconnect();
        }
    }

    public static void log(String s) {
        //Interface.addLogLine(s);
        System.out.println(s);
    }
}

/*
{
   "product_all_bytes":"1073741824",
   "product_remain_bytes":-10887150,
   "product_used_bytes":"1084628974",
   "package_free_all_bytes":107374182400,
   "package_free_remain_bytes":7633214153,
   "package_free_used_bytes":99740968247,
   "package_all_bytes":0,
   "acount_all_bytes":"101.00G",
   "acount_used_bytes":"93.90G",
   "acount_remain_bytes":"7.10G",
   "package_remain_bytes":0,
   "package_used_bytes":0,
   "per_product":101,
   "per":100,
   "per_free":93,
   "checkout_mode":[
      "2019\/05\/1\/-2019\/6\/1",
      "2019-05-1--2019-6-1"
   ],
   "billing_name":"\u8282\u5047\u65e5\u514d\u8d39-\u514d\u8d39\u533a\u57df-\u5b66\u751f\u514d\u8d391G-",
   "package_all":[
      {
         "user_id":"77382",
         "products_id":"1",
         "package_id":"8",
         "billing_id":"15",
         "user_package_id":"411369",
         "bytes":"107374182400",
         "seconds":"0",
         "times":"0",
         "remain_bytes":"7633214153",
         "remain_seconds":"0",
         "remain_times":"0",
         "sum_bytes":"0",
         "sum_seconds":"0",
         "sum_times":"0",
         "auto_buy":"0",
         "add_time":"1553686194",
         "valid_day":"0",
         "expire_time":"0",
         "valid_cycle":"day",
         "valid_mode":"0",
         "package_name":"50\u5143100G",
         "used_bytes":99740968247
      }
   ]
}
 */
package cn.codeprobe.utils;

import cn.hutool.core.text.CharSequenceUtil;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 验证是否是URL
 *
 * @author Lionido
 */
public class UrlUtil {

    /**
     * URL验证规则
     *
     * @param url 链接地址
     * @return true or false
     */
    public static boolean verifyUrl(String url) {
        String regEx = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\??(([A-Za-z0-9-~]+\\=?)([A-Za-z0-9-~]*)\\&?)*)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * URL验证规则（忽略大小写）
     *
     * @param url url
     * @return true / false
     */
    public static boolean verifyUrlCaseInsensitive(String url) {
        String regEx = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\??(([A-Za-z0-9-~]+\\=?)([A-Za-z0-9-~]*)\\&?)*)$";
        // 忽略大小写的写法
        Pattern pattern = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();
    }

    /**
     * 验证是否是URL
     *
     * @param urlStr 链接地址
     * @return true or false
     */
    public static boolean verifyUrlByUrl(String urlStr) {
        URL url = null;
        try {
            url = new URL(urlStr);
        } catch (MalformedURLException e) {
            return false;
        }

        String protocol = url.getProtocol();
        String authority = url.getAuthority();
        String path = url.getPath();
        String file = url.getFile();
        String host = url.getHost();
        if (CharSequenceUtil.isBlank(protocol)) {
            return false;
        }
        if (CharSequenceUtil.isBlank(authority)) {
            return false;
        }
        if (CharSequenceUtil.isBlank(file)) {
            return false;
        }
        if (CharSequenceUtil.isBlank(path)) {
            return false;
        }
        return !CharSequenceUtil.isBlank(host);
    }


    public static void main(String[] args) {
        boolean res = verifyUrl("https://admin.codeprobe.cn:8080/singularity-community/admin/friendLinks.html");
        System.out.println(res);
    }

}

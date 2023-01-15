package cn.codeprobe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlUtil {

    /**
     * 验证是否是URL
     * @param url 链接地址
     * @return true or false
     */
    public static boolean verifyUrl(String url){

        // URL验证规则
        // String regEx ="[A-Za-z]+://[A-Za-z0-9-_]+\\\\.[A-Za-z0-9-_%&\\?\\/.=]+";
        String regEx = "^([hH][tT]{2}[pP]:/*|[hH][tT]{2}[pP][sS]:/*|[fF][tT][pP]:/*)(([A-Za-z0-9-~]+).)+([A-Za-z0-9-~\\/])+(\\??(([A-Za-z0-9-~]+\\=?)([A-Za-z0-9-~]*)\\&?)*)$";
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        // 忽略大小写的写法
        // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        // 字符串是否与正则表达式相匹配
        return matcher.matches();

    }

    public static void main(String[] args) {
        boolean res =
                verifyUrl("http://admin.codeprobe.cn:8080/singularity-community/admin/friendLinks.html");
        System.out.println(res);
    }

}

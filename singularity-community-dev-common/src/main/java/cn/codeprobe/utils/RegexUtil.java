package cn.codeprobe.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Lionido
 */
public class RegexUtil {

    private static final int MOBILE_DIGITAL = 11;

    public static boolean isMobile(String mobile) {
        if (mobile.length() == MOBILE_DIGITAL) {
            // 移动号段正则表达式
            String pat1 = "^((13[4-9])|(147)|(15[0-2,7-9])|(178)|(18[2-4,7-8]))\\d{8}|(1705)\\d{7}$";
            // 联通号段正则表达式
            String pat2 = "^((13[0-2])|(145)|(15[5-6])|(176)|(18[5,6]))\\d{8}|(1709)\\d{7}$";
            // 电信号段正则表达式
            String pat3 = "^((133)|(153)|(177)|(18[0,1,9])|(149))\\d{8}$";
            // 虚拟运营商正则表达式
            String pat4 = "^((170))\\d{8}|(1718)|(1719)\\d{7}$";
            if (verify(mobile, pat1, pat2)) {
                return true;
            }
            return verify(mobile, pat3, pat4);
        }
        return false;
    }

    private static boolean verify(String mobile, String pat1, String pat2) {
        Pattern pattern1 = Pattern.compile(pat1);
        Matcher match1 = pattern1.matcher(mobile);
        boolean isMatch1 = match1.matches();
        if (isMatch1) {
            return true;
        }
        Pattern pattern2 = Pattern.compile(pat2);
        Matcher match2 = pattern2.matcher(mobile);
        return match2.matches();
    }
}

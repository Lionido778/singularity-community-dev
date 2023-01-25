package cn.codeprobe.api.config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

import cn.codeprobe.enums.ResponseStatusEnum;
import cn.codeprobe.exception.GlobalExceptionManage;

/**
 * 解决 请求路径url中的字符串类型的日期参数，进行时间日期类型的转换，（String字符串 -> Date日期）
 *
 * @author Lionido
 */
@Configuration
public class DateConverterConfig implements Converter<String, Date> {

    private static final List<String> FORMATTER_LIST = new ArrayList<>(4);

    static {
        FORMATTER_LIST.add("yyyy-MM");
        FORMATTER_LIST.add("yyyy-MM-dd");
        FORMATTER_LIST.add("yyyy-MM-dd hh:mm");
        FORMATTER_LIST.add("yyyy-MM-dd hh:mm:ss");
    }

    @Override
    public Date convert(String source) {
        String value = source.trim();
        if ("".equals(value)) {
            return null;
        }
        if (source.matches("^\\d{4}-\\d{1,2}$")) {
            return parseDate(source, FORMATTER_LIST.get(0));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2}$")) {
            return parseDate(source, FORMATTER_LIST.get(1));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, FORMATTER_LIST.get(2));
        } else if (source.matches("^\\d{4}-\\d{1,2}-\\d{1,2} \\d{1,2}:\\d{1,2}:\\d{1,2}$")) {
            return parseDate(source, FORMATTER_LIST.get(3));
        } else {
            GlobalExceptionManage.internal(ResponseStatusEnum.SYSTEM_DATE_PARSER_ERROR);
        }
        return null;
    }

    /**
     * 日期转换方法
     *
     * @param dateStr 日期字符串
     * @param formatter 格式
     * @return 格式化处理后的
     */
    public Date parseDate(String dateStr, String formatter) {
        Date date = null;
        try {
            DateFormat dateFormat = new SimpleDateFormat(formatter);
            date = dateFormat.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }
}

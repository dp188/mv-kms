package com.zhixin.core.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import com.zhixin.core.common.exceptions.ApiException;
import com.zhixin.core.enums.ErrorCodeEnum;

/**
 * 
 * @ClassName: DateUtil
 * @Description: 时间工具类
 * @author zhangtiebin@bwcmall.com
 * @date 2015年6月24日 上午10:29:27
 *
 */
public class DateUtil {
	 /**
     * Date format pattern used to parse HTTP date headers in RFC 1123 format.
     */
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in RFC 1036 format.
     */
    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";

    /**
     * Date format pattern used to parse HTTP date headers in ANSI C 
     * <code>asctime()</code> format.
     */
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    
    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1, 0, 0);
    }
    
    private static final TimeZone GMT = TimeZone.getTimeZone("GMT");
	/**
	 * 获取当前时间的秒数
	 * 
	 * @return
	 */
	public static Long getCurrentSecond() {

		return System.currentTimeMillis() / 1000;
	}
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static Date parse(String date,String format)throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			throw e;
		} 
	}
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String formatTimestamp(Timestamp time) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (time == null) {
			return null;
		}
		
		return sdf.format(time);
	}
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String formatSecond(Long second) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (second == null) {
			return null;
		}
		return sdf.format(new Date(second * 1000));
	}

	/**
     * 格式化
     * 
     * @param second
     * @return
     */
    public static String formatSecondOne(Long second) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (second == null) {
            return null;
        }
        return sdf.format(new Date(second * 1000));
    }
	
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String formatCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date());
	}
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static String format(String format,Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	/**
	 * String 转化成 Long
	 * 
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Long dateFormat(String format){
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    Date date;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCodeEnum.TimeFormatWrong);
        }
	    return date.getTime()/1000;
	}

	/**
	 * String 转化成 Long
	 * 
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Long timeFormat(String format){
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date;
        try {
            date = sdf.parse(format);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new ApiException(ErrorCodeEnum.TimeFormatWrong);
        }
	    return date.getTime()/1000;
	}
	/**
     * Formats the given date according to the RFC 1123 pattern.
     * 
     * @param date The date to format.
     * @return An RFC 1123 formatted date string.
     * 
     * @see #PATTERN_RFC1123
     */
    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_RFC1123);
    }
    
    /**
     * Formats the given date according to the specified pattern.  The pattern
     * must conform to that used by the {@link SimpleDateFormat simple date
     * format} class.
     * 
     * @param date The date to format.
     * @param pattern The pattern to use for formatting the date.  
     * @return A formatted date string.
     * 
     * @throws IllegalArgumentException If the given date pattern is invalid.
     * 
     * @see SimpleDateFormat
     */
    public static String formatDate(Date date, String pattern) {
        if (date == null) throw new IllegalArgumentException("date is null");
        if (pattern == null) throw new IllegalArgumentException("pattern is null");
        
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        formatter.setTimeZone(GMT);
        return formatter.format(date);
    }
	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static int getCurrentYear() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.YEAR);
	}

	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static int getCurrentMonth() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.MONTH);
	}

	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static int getCurrentDayOfMonth() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 格式化
	 * 
	 * @param second
	 * @return
	 */
	public static int getCurrentDayOfWeek() {
		Calendar ca = new GregorianCalendar();
		GregorianCalendar.getInstance().setTime(new Date());
		return ca.get(Calendar.DAY_OF_WEEK);
	}
}

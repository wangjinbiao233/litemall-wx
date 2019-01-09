package org.linlinjava.litemall.db.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    /**
     * 日期相加减 年
     * @param time
     *             时间字符串 yyyy-MM-dd HH:mm:ss
     * @param num
     *             加的数，-num就是减去
     * @return
     *             减去相应的数量的年的日期
     * @throws Date
     */
    public static Date yearAddNum(Date time, Integer num) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.YEAR, num);
        Date newTime = calendar.getTime();
        return newTime;
    }
    /**
     *日期相加减 月
     * @param time
     *           时间
     * @param num
     *           加的数，-num就是减去
     * @return
     *          减去相应的数量的月份的日期
     * @throws  Date
     */
    public static Date monthAddNum(Date time, Integer num){
        //SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = format.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MONTH, num);
        Date newTime = calendar.getTime();
        return newTime;
    }
    /**
     *日期相加减 日
     * @param time
     *           时间
     * @param num
     *           加的数，-num就是减去
     * @return
     *          减去相应的数量的天的日期
     * @throws Date
     */
    public static Date dayAddNum(Date time, Integer num){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        Date newTime = calendar.getTime();
        return newTime;
    }
}

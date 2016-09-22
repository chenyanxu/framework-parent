/**
 * @项目名称：FH601
 * @描述：
 * @包名：com.kalix.framework.common.util
 * @文件名称：DateUtil.java
 * @版本信息:
 * @日期:2013-12-25-下午1:01:59
 * @Copyright (c) : 长春丽明科技开发有限公司-版权所有
 */
package com.kalix.framework.core.util;

/**
 * @类描述：
 * @创建人：王凯冉
 * @创建时间：2013-12-25 下午1:01:59
 * @修改人：王凯冉
 * @修改时间：2013-12-25 下午1:01:59
 * @修改备注：
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static SimpleDateFormat bigLongSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSSS");
    public static SimpleDateFormat hourSdf = new SimpleDateFormat("HH");
    public static SimpleDateFormat minutesSdf = new SimpleDateFormat("mm");
    public static SimpleDateFormat shortSdf = new SimpleDateFormat("yyyy-MM-dd");
    public static SimpleDateFormat shortSdfNoDay = new SimpleDateFormat("yyyy-MM");
    public static SimpleDateFormat longHourSdf = new SimpleDateFormat("yyyy-MM-dd HH");
    public static SimpleDateFormat longSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    protected Calendar date = null;
    protected final static Integer MONTH_COUNT = 12;
    protected SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 获得本周的第一天，即周日
     *
     * @return
     */
    public static Date getCurrentWeekDayStartTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK) - 1;
            c.add(Calendar.DATE, -weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00"));
        } catch (Exception e) {
        }
        return c.getTime();
    }

    /**
     * 获得本周的最后一天，即本周六
     *
     * @return
     */
    public static Date getCurrentWeekDayEndTime() {
        Calendar c = Calendar.getInstance();
        try {
            int weekday = c.get(Calendar.DAY_OF_WEEK);
            c.add(Calendar.DATE, 7 - weekday);
            c.setTime(longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59"));
        } catch (Exception e) {
        }
        return c.getTime();
    }

    /**
     * 获得本天的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentDayStartTime() {
        Date now = new Date();
        try {
            now = shortSdf.parse(shortSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得本天的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentDayStartTime(Date date) {
        try {
            date = shortSdf.parse(shortSdf.format(date) + " 00:00:00");
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 获得本天的按6整除的某个整点时间，即2012-01-01 00:00:00;2012-01-01 06:00:00;2012-01-01 12:00:00;2012-01-01
     * 18:00:00
     */
    public static Date getCurrentDayTheHour(Date date, long divisor) {
        long time = 21600000 * divisor;
        long startTime = date.getTime();
        Date now = new Date(startTime + time);
        try {
            now = longSdf.parse(longSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得某个日期的后divisor的日期
     *
     * @param date    某一个日期
     * @param divisor 往前算几天
     */
    public static Date getCurrentDayTheDayStartTime(Date date, long divisor) {
        long time = 86400000 * divisor;
        long startTime = date.getTime();
        Date now = new Date(startTime + time);
        try {
            now = longSdf.parse(longSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得某个日期后的多少毫秒是的日期
     *
     * @param date    某一个日期
     * @param divisor 往前算几天
     */
    public static Date getCurrentDayTheDay(Date date, long divisor, long millisecond) {
        long time = millisecond * divisor;
        long startTime = date.getTime();
        Date now = new Date(startTime + time);
        try {
            now = longSdf.parse(longSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得某个日期的前divisor的日期
     *
     * @param date    某一个日期
     * @param divisor 往后算几天
     */
    public static Date getCurrentDayTheDayEndTime(Date date, long divisor) {
        long time = 86400000 * divisor;
        long startTime = date.getTime();
        Date now = new Date(startTime - time);
        try {
            now = longSdf.parse(longSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得本天的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentDayEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(shortSdf.format(now) + " 23:59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得时间参数的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */

    public static Date getCurrentDayEndTime(Date date) {
        try {
            date = longSdf.parse(shortSdf.format(date) + " 23:59:59");
        } catch (Exception e) {
        }
        return date;
    }

    /**
     * 获得本小时的开始时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentHourStartTime() {
        Date now = new Date();
        try {
            now = longHourSdf.parse(longHourSdf.format(now));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得本小时的结束时间，即2012-01-01 23:59:59
     *
     * @return
     */
    public static Date getCurrentHourEndTime() {
        Date now = new Date();
        try {
            now = longSdf.parse(longHourSdf.format(now) + ":59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 获得本月的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentMonthStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前月的结束时间，即2012-01-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentMonthEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.DATE, 1);
            c.add(Calendar.MONTH, 1);
            c.add(Calendar.DATE, -1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前年的开始时间，即2012-01-01 00:00:00
     *
     * @return
     */
    public static Date getCurrentYearStartTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DATE, 1);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前年的结束时间，即2012-12-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentYearEndTime() {
        Calendar c = Calendar.getInstance();
        Date now = null;
        try {
            c.set(Calendar.MONTH, 11);
            c.set(Calendar.DATE, 31);
            now = shortSdf.parse(shortSdf.format(c.getTime()));
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前季度的开始时间，即2012-01-1 00:00:00
     *
     * @return
     */
    public static Date getCurrentQuarterStartTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3)
                c.set(Calendar.MONTH, 0);
            else if (currentMonth >= 4 && currentMonth <= 6)
                c.set(Calendar.MONTH, 3);
            else if (currentMonth >= 7 && currentMonth <= 9)
                c.set(Calendar.MONTH, 4);
            else if (currentMonth >= 10 && currentMonth <= 12)
                c.set(Calendar.MONTH, 9);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前季度的三个月份，即2012-01-1 00:00:00;2012-02-1 00:00:00;2012-03-1 00:00:00
     *
     * @return
     */
    public static Date getCurrentQuarterTheMonthStartTime(int theMonth) {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 0);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 1);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 2);
                }
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 3);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 4);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 5);
                }
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 6);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 7);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 8);
                }
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 9);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 10);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 11);
                }
            }
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前季度的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterEndTime() {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                c.set(Calendar.MONTH, 2);
                c.set(Calendar.DATE, 31);
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                c.set(Calendar.MONTH, 5);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                c.set(Calendar.MONTH, 8);
                c.set(Calendar.DATE, 30);
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                c.set(Calendar.MONTH, 11);
                c.set(Calendar.DATE, 31);
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前季度的每个月的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentQuarterTheMonthEndTime(int theMonth) {
        Calendar c = Calendar.getInstance();
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentYear = c.get(Calendar.YEAR) + 1;
        Date now = null;
        try {
            if (currentMonth >= 1 && currentMonth <= 3) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 0);
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 1);
                    if (currentMonth == 2 &&
                            ((currentYear % 4 == 0 && currentYear % 100 != 0) || currentYear % 400 == 0)) {
                        c.set(Calendar.DATE, 29);
                    }
                    {
                        c.set(Calendar.DATE, 28);
                    }
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 2);
                    c.set(Calendar.DATE, 31);
                }
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 3);
                    c.set(Calendar.DATE, 30);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 4);
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 5);
                    c.set(Calendar.DATE, 30);
                }
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 6);
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 7);
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 8);
                    c.set(Calendar.DATE, 30);
                }
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                if (theMonth == 1) {
                    c.set(Calendar.MONTH, 9);
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    c.set(Calendar.MONTH, 10);
                    c.set(Calendar.DATE, 30);
                } else if (theMonth == 3) {
                    c.set(Calendar.MONTH, 11);
                    c.set(Calendar.DATE, 31);
                }
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前年度的某个月份的开始时间
     *
     * @return
     */
    public static Date getCurrentYearTheMonthStartTime(int theMonth) {
        Calendar c = Calendar.getInstance();
        int currentMonth = theMonth - 1;
        Date now = null;
        try {
            c.set(Calendar.MONTH, currentMonth);
            c.set(Calendar.DATE, 1);
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 00:00:00");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 当前年度的某个月的结束时间，即2012-03-31 23:59:59
     *
     * @return
     */
    public static Date getCurrentYearTheMonthEndTime(int theMonth) {
        Calendar c = Calendar.getInstance();
        int currentMonth = theMonth - 1;
        int currentYear = c.get(Calendar.YEAR) + 1;
        Date now = null;
        try {
            c.set(Calendar.MONTH, currentMonth);
            if (currentMonth >= 1 && currentMonth <= 3) {
                if (theMonth == 1) {
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    if (currentMonth == 2 &&
                            ((currentYear % 4 == 0 && currentYear % 100 != 0) || currentYear % 400 == 0)) {
                        c.set(Calendar.DATE, 29);
                    }
                    {
                        c.set(Calendar.DATE, 28);
                    }
                } else if (theMonth == 3) {
                    c.set(Calendar.DATE, 31);
                }
            } else if (currentMonth >= 4 && currentMonth <= 6) {
                if (theMonth == 1) {
                    c.set(Calendar.DATE, 30);
                } else if (theMonth == 2) {
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 3) {
                    c.set(Calendar.DATE, 30);
                }
            } else if (currentMonth >= 7 && currentMonth <= 9) {
                if (theMonth == 1) {
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 3) {
                    c.set(Calendar.DATE, 30);
                }
            } else if (currentMonth >= 10 && currentMonth <= 12) {
                if (theMonth == 1) {
                    c.set(Calendar.DATE, 31);
                } else if (theMonth == 2) {
                    c.set(Calendar.DATE, 30);
                } else if (theMonth == 3) {
                    c.set(Calendar.DATE, 31);
                }
            }
            now = longSdf.parse(shortSdf.format(c.getTime()) + " 23:59:59");
        } catch (Exception e) {
        }
        return now;
    }

    /**
     * 去掉最后一个字符
     *
     * @param str
     * @return String
     */
    public static String cutComma(String str) {
        if (str.length() > 0) {
            return str.substring(0, str.length() - 1);
        }
        return "";
    }

    /**
     * 转换date格式
     *
     * @param date
     * @return String
     */
    public static String convertDateToString(Date date, String formatter) {
        String dateString = "";
        if (date != null) {
            SimpleDateFormat sdf = new SimpleDateFormat(formatter);
            dateString = sdf.format(date);
        }
        return dateString;
    }

    /**
     * 转换date格式
     *
     * @param date
     * @return String
     */
    public static String convertDateToString(Date date, SimpleDateFormat sdf) {
        String dateString = "";
        try {
            if (sdf != null && date != null) {
                dateString = sdf.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;
    }


    /**
     *
     * @param dateString
     * @param sdf
     * @return
     */
    public static Date convertStringToDate(String dateString, SimpleDateFormat sdf) {
        Date date = null;
        try {
            if (dateString != null && !dateString.isEmpty()) {
                date = sdf.parse(dateString);
            }
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * String To Date
     *
     * @param dateString
     * @param formatter
     * @return Date
     */
    public static Date convertStringToDate(String dateString, String formatter) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatter);
            if (dateString != null && !dateString.isEmpty()) {
                date = sdf.parse(dateString);
            }
        } catch (ParseException e) {
        }
        return date;
    }

    /**
     * 取得2个时间间隔的天数
     *
     * @return long 间隔天数，负值说明date1的值比date2的值小。
     * @author cyc
     */
    public static long dateDiff(Date date1, Date date2) {
        long diff = date1.getTime() - date2.getTime();
        long days = diff / (1000 * 60 * 60 * 24);
        return days;
    }

    /**
     *
     * @mss 要转换的毫秒数
     * @return 该毫秒数转换为 * days * hours * minutes * seconds 后的格式
     * @author fy.zhang
     */
    public static String formatDuring(long mss) {
        String format_day="%d天:%d小时:%d分钟:%d秒";
        String format_hour="%d小时:%d分钟:%d秒";
        String format_minute="%d分钟:%d秒";
        String format_second="%d秒";

        long days = mss / (1000 * 60 * 60 * 24);
        long hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (mss % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (mss % (1000 * 60)) / 1000;

        if(days>0)
            return String.format(format_day,days,hours,minutes,seconds);
        else if(hours>0)
            return String.format(format_hour,hours,minutes,seconds);
        else if(minutes>0)
            return String.format(format_minute,minutes,seconds);
        else if(seconds>0)
            return String.format(format_second,seconds);
        return "";
    }
}

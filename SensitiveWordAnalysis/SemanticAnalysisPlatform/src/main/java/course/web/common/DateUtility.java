package course.web.common;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

public class DateUtility {

	public static final String dtSimple = "yyyy-MM-dd HH:mm:ss";

	public static final String dtSimpleCommon = "yyyy-MM-dd HH:mm";

	public static final String dSimple = "yyyy-MM-dd";

	public static final String tSimple = "HH:mm:ss";

	public static final String dtDense = "yyyyMMddHHmmss";

	public static final String dDense = "yyyyMMdd";

	public static final String tDense = "HHmmss";

	public static final String milliDense = "yyyyMMddHHmmssSSS";

	public static final String dtWeeHours = "yyyy-MM-dd 00:00:00";

	public static String getDTDenseDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dtDense));
	}

	public static String getDTSimpleDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dtSimple));
	}

	public static String getDDenseDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dDense));
	}

	public static String getDSimpleDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(dSimple));
	}

	public static String getTDenseDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(tDense));
	}

	public static String getTSimpleDate() {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(tSimple));
	}

	public static String getMilliDenseDate(Long timestamp) {
		return timestamp != null
				? LocalDateTime.ofInstant(new Date(timestamp).toInstant(), ZoneId.systemDefault())
						.format(DateTimeFormatter.ofPattern(milliDense))
				: LocalDateTime.now().format(DateTimeFormatter.ofPattern(milliDense));
	}

	public static Date parseDateTime(String dateStr, String format) throws DateTimeParseException {
		return parseDateTime(dateStr, format, Locale.getDefault(), TimeZone.getDefault());
	}

	public static Date parseDateTime(String dateStr, String format, Locale locale, TimeZone timeZone)
			throws DateTimeParseException {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		} else {
			return Date.from(LocalDateTime
					.parse(dateStr,
							DateTimeFormatter.ofPattern(format).withLocale(locale).withZone(timeZone.toZoneId()))
					.atZone(timeZone.toZoneId()).toInstant());
		}
	}

	public static Date parseDate(String dateStr, String format) throws DateTimeParseException {
		return parseDate(dateStr, format, Locale.getDefault(), TimeZone.getDefault());
	}

	public static Date parseDate(String dateStr, String format, Locale locale, TimeZone timeZone)
			throws DateTimeParseException {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		} else {
			return Date
					.from(LocalDate
							.parse(dateStr,
									DateTimeFormatter.ofPattern(format).withLocale(locale)
											.withZone(timeZone.toZoneId()))
							.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
	}

	public static Date getDTWeeHours() throws DateTimeParseException {
		return parseDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(dtWeeHours)), dtWeeHours);
	}

	public static String dateToString(Date date, String format) {
		if (date == null)
			return "";
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}

	public static String getAppointedDay(int n, String format) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return dateToString(calendar.getTime(), format);
	}

	public static Date getAppointedDay(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, n);
		return calendar.getTime();
	}

	public static int getOrderOfDay(String dateStr, String format, int limit) throws DateTimeParseException {
		Date date = parseDate(dateStr, format);
		return limit - ((int) ((new Date().getTime() - date.getTime()) / 24 / 60 / 60 / 1000));
	}

	public static String qingShuFormatTime(int time) {
		int hour = time / (60 * 60);
		int min = (time / 60) % 60;
		int second = time % 60;
		if (hour != 0 || min != 0 || second != 0) {
			return (hour > 0 ? hour + "小时" : "") + (min > 0 ? min + "分" : "") + (second > 0 ? second + "秒" : "");
		}
		return "0";
	}

	public static String praseSecToTime(int time) {
		String timeStr = "0";
		int hour = 0;
		int minute = 0;
		int second = 0;
		if (time <= 0) {
			return timeStr;
		} else {
			minute = time / 60;
			if (minute < 60) {
				if (Math.floor((time / 60)) > 0) {
					second = time % 60;
					timeStr = minute + "分" + second + "秒";
				} else {
					timeStr = time + "秒";
				}
			} else {
				hour = minute / 60;
				minute = minute % 60;
				second = time - hour * 3600 - minute * 60;
				timeStr = hour + "小时" + minute + "分" + second + "秒";
			}
		}
		return timeStr;
	}

	public static Date nowAddSeconds(int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, n);
		return calendar.getTime();
	}

	public static String paresLongToStringDate(long time) {
		if (time <= 0) {
			return "0";
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dtSimple);
		Date date = new Date(time);
		return sdf.format(date);
	}
}

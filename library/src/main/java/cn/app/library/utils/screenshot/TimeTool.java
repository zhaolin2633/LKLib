package cn.app.library.utils.screenshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeTool {

	public static final int MINUTE = 60;
	public static final int HOUR = 60 * 60;
	public static final int DAY = 24 * 60 * 60;

	public static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static SimpleDateFormat sdf_name = new SimpleDateFormat(
			"yyyyMMdd_HHmmss");

	public static SimpleDateFormat sdf_simple_name = new SimpleDateFormat(
			"yyyyMMdd");

	public static Calendar StringConvertCalendar(String time) {
		try {

			Date date = sdf.parse(time);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getTimeName(long time) {
		return sdf_name.format(new Date(time));
	}

	public static String getSimpleName() {
		return sdf_simple_name.format(new Date(System.currentTimeMillis()));
	}

}

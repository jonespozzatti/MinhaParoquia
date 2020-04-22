package org.paroquia.api.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String FORMAT_DIA_MES_ANO = "dd/MM/yyyy";
	public static String FORMAT_DIA_MES_ANO_HORA = "dd/MM/yyyy  HH:mm:ss";
	public static String FORMAT_HORA = "HH:mm:ss";

	public static SimpleDateFormat getSimpleDateFormat(String format) {
		if (format == null) {
			return null;
		}
		return new SimpleDateFormat(format);
	}

	public static String dateToString(Date date, String format) {
		if (date == null) {
			return null;
		}
		return getSimpleDateFormat(format).format(date);
	}

	public static String dateToString(Date date) {
		if (date == null) {
			return null;
		}
		return getSimpleDateFormat(FORMAT_DIA_MES_ANO).format(date);
	}

	public static String dateTimeToString(Date date) {
		if (date == null) {
			return null;
		}
		return getSimpleDateFormat(FORMAT_DIA_MES_ANO_HORA).format(date);
	}

}

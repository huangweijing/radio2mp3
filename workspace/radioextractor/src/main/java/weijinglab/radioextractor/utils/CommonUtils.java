package weijinglab.radioextractor.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 共通ツール
 * @author HuangWeijing
 * @version 20151221
 */
public class CommonUtils {
	
	private final static String FORMAT_YYYYMMDD_HHMMSS = "yyyyMMdd_HHmmss";

	/**
	 * 日付をyyyyMMdd_HHmmssに転換する
	 * @param date 転換日付
	 * @return 転換後のyyyyMMdd_HHmmss文字列
	 */
	public static String convertToYyyyMMdd_ssmm(Date date) {
		
		DateFormat sdf = new SimpleDateFormat(FORMAT_YYYYMMDD_HHMMSS);
		return sdf.format(date);
		
	}
	
}
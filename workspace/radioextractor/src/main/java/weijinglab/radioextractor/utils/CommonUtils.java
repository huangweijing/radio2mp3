package weijinglab.radioextractor.utils;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

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
	
	public static String changeEncoding(String src) {
		if(StringUtils.isEmpty(src)) {
			return StringUtils.EMPTY;
		}
		try {
			return new String(src.getBytes("GBK"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * ファイル保存できないファイル名の文字を消す
	 * 対象文字列は以下となる
	 * < (less than)
	 > (greater than)
	 : (colon)
	 " (double quote)
	 / (forward slash)
	 \ (backslash)
	 | (vertical bar or pipe)
	 ? (question mark)
	 * (asterisk)
	 * @param path
	 * @return 消したパス
	 */
	public static String deleteReservedPathChar(String path) {
		
		return path.replaceAll("<|>|:|\"|/|\\\\|\\|\\?|\\*", StringUtils.EMPTY);
	}
	
}

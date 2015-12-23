package weijinglab.radioextractor.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

	public static String convertToYyyyMMdd_ssmm(Date date) {
		
		DateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
		return sdf.format(date);
		
	}
	
}

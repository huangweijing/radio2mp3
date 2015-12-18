package weijinglab.radiotable.utils;

import java.util.Calendar;

/**
 * ラジオスケージュールを抽出する際利用するツールクラス
 * @author HuangWeijing
 * @version 20151218
 */
public class RadioTableUtil {

	/**
	 * Calendar.曜日からインデックス順に変える
	 * インデックス順とは月曜～日曜＝｛０～６｝
	 * @param weekDay
	 * @return
	 */
	public static Integer convertWeekdayToArrayIndex(Integer weekDay) {
		if(weekDay >= Calendar.MONDAY && weekDay <= Calendar.SATURDAY) {
			return weekDay - 2;
		} else {
			return 6;
		}
	}
	
	/**
	 * インデックス順からCalendar.曜日に変える
	 * インデックス順とは月曜～日曜＝｛０～６｝
	 * @param weekDay
	 * @return
	 */
	public static Integer convertArrayIndexToWeekday(Integer arrayIndex) {
		if(arrayIndex + 2 >= Calendar.MONDAY && arrayIndex + 2 <= Calendar.SATURDAY) {
			return arrayIndex + 2;
		} else {
			return Calendar.SUNDAY;
		}
	}
	
}

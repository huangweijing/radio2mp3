package weijinglab.radiotable.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import weijinglab.radiotable.utils.RadioTableUtil;

/**
 * 番組表データを維持するクラス
 * @author HuangWeijing
 * @version 20151217
 */
public class TimeTable {

	/** 番組表データ 曜日、その日の番組表 */
	private Map<Integer, List<ProgramEntry>> timeTableData;

	public Map<Integer, List<ProgramEntry>> getTimeTableData() {
		return timeTableData;
	}
	
	public List<ProgramEntry> getAllProgramList() {
		List<ProgramEntry> allProgramList = new ArrayList<ProgramEntry>();
		//日曜日から土曜日までのすべての番組表を初期化する。
		for (int dayOfWeek = Calendar.MONDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
			allProgramList.addAll(timeTableData.get(dayOfWeek));
		}
		allProgramList.addAll(timeTableData.get(Calendar.SUNDAY));
		return allProgramList;
	}
	
	
	/**
	 * 番組表の初期化処理
	 */
	public TimeTable() {
		timeTableData = new HashMap<Integer, List<ProgramEntry>>();
		//日曜日から土曜日までのすべての番組表を初期化する。
		for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
			timeTableData.put(dayOfWeek, new ArrayList<ProgramEntry>());
		}
	}

	/**
	 * 曜日を指定してその日の番組表を追加する。
	 * @param dayOfWeek 曜日
	 * @param programEntry 番組エントリー
	 */
	public void addProgramEntry(Integer dayOfWeek, ProgramEntry programEntry) {
		List<ProgramEntry> dayTimeTable = timeTableData.get(dayOfWeek);
		dayTimeTable.add(programEntry);
	}

	/**
	 * 時間を元に、現在進行中の番組を抽出する。
	 * @param currentTime 現在時刻
	 * @return 今の番組
	 */
	public ProgramEntry getCurrentProgram(Date currentTime) {
		for (int dayOfWeek = RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY); 
				dayOfWeek <= RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY);
				dayOfWeek++) {
			List<ProgramEntry> dayTimeTable = timeTableData.get(RadioTableUtil
					.convertArrayIndexToWeekday(dayOfWeek));
			for (ProgramEntry programEntry : dayTimeTable) {
				Date programEndTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				//現在時刻が番組開始時間から終了時間までの間であれば、今の番組とする。
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					return programEntry;
				}
			}
		}
		return null;
	}


	/**
	 * 時間を元に、次の番組を抽出する。
	 * @param currentTime 現在の時刻
	 * @return 次の番組
	 */
	public ProgramEntry getNextProgram(Date currentTime) {
		//次の番組対象有無フラグ
		boolean nextIsGood = false;
		for (int dayOfWeek = RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY); 
				dayOfWeek <= RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY);
				dayOfWeek++) {
			List<ProgramEntry> dayTimeTable = timeTableData.get(RadioTableUtil
					.convertArrayIndexToWeekday(dayOfWeek));
			for (ProgramEntry programEntry : dayTimeTable) {
				if (nextIsGood) {
					return programEntry;
				}
				Date programEndTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				//現在時刻が番組開始時間から終了時間までの間であれば、次の番組とする。
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					nextIsGood = true;
				}
			}
		}
		return null;
	}

	/**
	 * 時間を元に、前の番組を抽出する。
	 * @param currentTime 現在の時刻
	 * @return 前の番組
	 */
	public ProgramEntry getPreviousProgram(Date currentTime) {
		//常に前の番組を記録する
		ProgramEntry previousEntry = null;
		for (int dayOfWeek = RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY); 
				dayOfWeek <= RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY); 
				dayOfWeek++) {
			List<ProgramEntry> dayTimeTable = timeTableData.get(RadioTableUtil
					.convertArrayIndexToWeekday(dayOfWeek));
			for (ProgramEntry programEntry : dayTimeTable) {
				Date programEndTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				//現在時刻が番組開始時間から終了時間までの間であれば、その前の番組とする。
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					return previousEntry;
				}
				previousEntry = programEntry;
			}
		}
		return null;
	}

}

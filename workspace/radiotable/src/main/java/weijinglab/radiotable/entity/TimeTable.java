package weijinglab.radiotable.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.time.DateUtils;

import weijinglab.radiotable.utils.RadioTableUtil;

public class TimeTable {

	private Map<Integer, List<ProgramEntry>> timeTableData;

	public TimeTable() {
		timeTableData = new HashMap<Integer, List<ProgramEntry>>();

		for (int dayOfWeek = Calendar.SUNDAY; dayOfWeek <= Calendar.SATURDAY; dayOfWeek++) {
			timeTableData.put(dayOfWeek, new ArrayList<ProgramEntry>());
		}
	}

	public void addProgramEntry(Integer dayOfWeek, ProgramEntry programEntry) {
		List<ProgramEntry> dayTimeTable = timeTableData.get(dayOfWeek);
		dayTimeTable.add(programEntry);
	}

	public ProgramEntry getCurrentProgram(Date currentTime) {
		for (int dayOfWeek = RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY); 
				dayOfWeek <= RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY);
				dayOfWeek++) {
			List<ProgramEntry> dayTimeTable = timeTableData.get(RadioTableUtil
					.convertArrayIndexToWeekday(dayOfWeek));
			for (ProgramEntry programEntry : dayTimeTable) {
				Date programEndTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					return programEntry;
				}
			}
		}
		return null;
	}

	public ProgramEntry getNextProgram(Date currentTime) {
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
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					nextIsGood = true;
				}
			}
		}
		return null;
	}

	public ProgramEntry getPreviousProgram(Date currentTime) {
		ProgramEntry previousEntry = null;
		for (int dayOfWeek = RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY); 
				dayOfWeek <= RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY); 
				dayOfWeek++) {
			List<ProgramEntry> dayTimeTable = timeTableData.get(RadioTableUtil
					.convertArrayIndexToWeekday(dayOfWeek));
			for (ProgramEntry programEntry : dayTimeTable) {
				Date programEndTime = DateUtils.addMinutes(
						programEntry.getStartTime(), programEntry.getDuration());
				if (currentTime.compareTo(programEntry.getStartTime()) >= 0
						&& currentTime.compareTo(programEndTime) <= 0) {
					return previousEntry;
				}
				previousEntry = programEntry;
			}
		}
		return null;
	}
	// public Integer getEndTimeOfDay(Integer dayOfWeek) {
	// List<ProgramEntry> timeTableOfDay = timeTableData.get(dayOfWeek);
	// if(timeTableOfDay.size() == 0) {
	// return null;
	// } else {
	// ProgramEntry lastProgram = timeTableOfDay.get(timeTableOfDay.size() - 1);
	// return new Date(lastProgram.getStartTime().getTime() +
	// lastProgram.getDuration());
	// }
	//
	// }

}

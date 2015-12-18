package weijinglab.radiotable.entity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TimeTable {

	public Integer DAYCOUNT_OF_WEEK = 7;
	
	Map<Integer, List<ProgramEntry>> timeTableData;
	
	public TimeTable() {
		timeTableData = new HashMap<Integer, List<ProgramEntry>>();

		for(int dayOfWeek = Calendar.SUNDAY; dayOfWeek<= Calendar.SATURDAY; dayOfWeek++) {
			timeTableData.put(dayOfWeek, new ArrayList<ProgramEntry>());
		}
	}
	
	public void addProgramEntry(Integer dayOfWeek, ProgramEntry programEntry) {
		List<ProgramEntry> dayTimeTable = timeTableData.get(dayOfWeek);
		dayTimeTable.add(programEntry);
	}
	
//	public Integer getEndTimeOfDay(Integer dayOfWeek) {
//		List<ProgramEntry> timeTableOfDay = timeTableData.get(dayOfWeek);
//		if(timeTableOfDay.size() == 0) {
//			return null;
//		} else {
//			ProgramEntry lastProgram = timeTableOfDay.get(timeTableOfDay.size() - 1);
//			return new Date(lastProgram.getStartTime().getTime() + lastProgram.getDuration());
//		}
//			
//	}
	
}

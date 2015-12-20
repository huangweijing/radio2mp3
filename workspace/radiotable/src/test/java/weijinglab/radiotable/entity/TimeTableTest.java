package weijinglab.radiotable.entity;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

import junit.framework.TestCase;

public class TimeTableTest extends TestCase {

	public void testTimeTable() {
		TimeTable timeTable = new TimeTable();
		assertNull(timeTable.getCurrentProgram(new Date()));
	}

	public void testAddProgramEntry() {
		TimeTable timeTable = new TimeTable();
		Date currentDate = new Date();
		Calendar currentCalendar = Calendar.getInstance();
		ProgramEntry programEntry = new ProgramEntry();
		programEntry.setCaster("huangweijing");
		programEntry.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), -10));
		programEntry.setDuration(60);
		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry);
		
		assertEquals(timeTable.getCurrentProgram(currentDate), programEntry);
	}

//	public void testGetCurrentProgram() {
//		TimeTable timeTable = new TimeTable();
//		Date currentDate = new Date();
//		Calendar currentCalendar = Calendar.getInstance();
//		ProgramEntry programEntry = new ProgramEntry();
//		programEntry.setCaster("huangweijing");
//		programEntry.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), -10));
//		programEntry.setDuration(60);
//		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry);
//		
//		assertEquals(timeTable.getCurrentProgram(currentDate), programEntry);
//	}

	public void testGetNextProgram() {
		TimeTable timeTable = new TimeTable();
		Date currentDate = new Date();
		Calendar currentCalendar = Calendar.getInstance();
		ProgramEntry programEntry = new ProgramEntry();
		programEntry.setCaster("huangweijing");
		programEntry.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), -10));
		programEntry.setDuration(60);
		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry);
		
		ProgramEntry programEntry2 = new ProgramEntry();
		programEntry2.setCaster("huangweijing2");
		programEntry2.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), 50));
		programEntry2.setDuration(60);
		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry2);
		
		assertEquals(timeTable.getNextProgram(currentDate), programEntry2);
	}

	public void testGetPreviousProgram() {
		TimeTable timeTable = new TimeTable();
		Calendar currentCalendar = Calendar.getInstance();
		ProgramEntry programEntry = new ProgramEntry();
		programEntry.setCaster("huangweijing");
		programEntry.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), -10));
		programEntry.setDuration(60);
		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry);
		
		ProgramEntry programEntry2 = new ProgramEntry();
		programEntry2.setCaster("huangweijing2");
		programEntry2.setStartTime(DateUtils.addMinutes(currentCalendar.getTime(), 50));
		programEntry2.setDuration(60);
		timeTable.addProgramEntry(currentCalendar.get(Calendar.DAY_OF_WEEK), programEntry2);
		
		assertEquals(timeTable.getPreviousProgram(DateUtils.addMinutes(currentCalendar.getTime(), 70)), programEntry);
	}

}

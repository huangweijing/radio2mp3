package weijinglab.radiotable.net.webpage;

import java.io.IOException;
import java.util.Date;

import junit.framework.Assert;
import junit.framework.TestCase;
import weijinglab.radiotable.entity.ProgramEntry;
import weijinglab.radiotable.entity.TimeTable;

public class HtmlExtractorTest extends TestCase {

	public void testFillTimeTable() throws IOException {
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		TimeTable timeTable = new TimeTable();
		htmlExtractor.setTimetableUrl("http://www.agqr.jp/timetable/streaming.html");
		htmlExtractor.fillTimeTable(timeTable);
		ProgramEntry currentProgram = timeTable.getCurrentProgram(new Date());
		System.out.println(currentProgram.getProgramName());
		System.out.println(currentProgram.getCaster());
		System.out.println(currentProgram.getStartTime());
//		System.out.println("=============================");
//		timeTable.printOutAllProgram();
	}

	public void testGetTimetableUrl() {
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		htmlExtractor.setTimetableUrl("http://www.agqr.jp/timetable/streaming.php");
		Assert.assertEquals("http://www.agqr.jp/timetable/streaming.php"
				, htmlExtractor.getTimetableUrl());
	}

	public void testGetMaxSizeOfTimeTable() {
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		htmlExtractor.setMaxSizeOfTimeTable(100);
		Assert.assertEquals(Integer.valueOf(100), htmlExtractor.getMaxSizeOfTimeTable());
	}

}

package weijinglab.radiotable.net.webpage;

import java.io.IOException;

import weijinglab.radiotable.entity.TimeTable;
import junit.framework.Assert;
import junit.framework.TestCase;

public class HtmlExtractorTest extends TestCase {

	public void testFillTimeTable() throws IOException {
		HtmlExtractor htmlExtractor = new HtmlExtractor();
		TimeTable timeTable = new TimeTable();
		htmlExtractor.setTimetableUrl("http://www.agqr.jp/timetable/streaming.php");
		htmlExtractor.fillTimeTable(timeTable);		
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

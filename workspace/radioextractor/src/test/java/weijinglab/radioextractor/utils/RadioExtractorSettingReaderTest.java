package weijinglab.radioextractor.utils;

import junit.framework.TestCase;

public class RadioExtractorSettingReaderTest extends TestCase {

	public void testGetInstance() {
		RadioExtractorSettingReader settings = RadioExtractorSettingReader.getInstance();
		assertEquals("http://www.agqr.jp/timetable/streaming.php", settings.getSetting("net.timetable"));
	}

}

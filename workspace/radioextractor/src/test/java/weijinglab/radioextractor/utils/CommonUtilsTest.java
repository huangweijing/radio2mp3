package weijinglab.radioextractor.utils;

import junit.framework.TestCase;

public class CommonUtilsTest extends TestCase {

	public void testDeleteReservedPathChar() {
		assertEquals("abc" , CommonUtils.deleteReservedPathChar("a><:\"/b\\|?*c"));
	}

}

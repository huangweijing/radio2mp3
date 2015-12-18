package weijinglab.radiotable.utils;

import java.util.Calendar;

import junit.framework.Assert;
import junit.framework.TestCase;

public class RadioTableUtilTest extends TestCase {

	public void testConvertWeekdayToArrayIndex() {
		Assert.assertEquals(Calendar.MONDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(0));
		Assert.assertEquals(Calendar.TUESDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(1));
		Assert.assertEquals(Calendar.WEDNESDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(2));
		Assert.assertEquals(Calendar.THURSDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(3));
		Assert.assertEquals(Calendar.FRIDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(4));
		Assert.assertEquals(Calendar.SATURDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(5));
		Assert.assertEquals(Calendar.SUNDAY, (int)RadioTableUtil.convertArrayIndexToWeekday(6));
	}

	public void testConvertArrayIndexToWeekday() {
		Assert.assertEquals(0, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.MONDAY));
		Assert.assertEquals(1, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.TUESDAY));
		Assert.assertEquals(2, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.WEDNESDAY));
		Assert.assertEquals(3, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.THURSDAY));
		Assert.assertEquals(4, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.FRIDAY));
		Assert.assertEquals(5, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SATURDAY));
		Assert.assertEquals(6, (int)RadioTableUtil.convertWeekdayToArrayIndex(Calendar.SUNDAY));
	}

}

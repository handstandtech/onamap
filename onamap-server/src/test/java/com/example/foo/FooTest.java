package com.example.foo;

import static org.junit.Assert.assertEquals;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Foo}.
 * 
 * @author user@example.com (John Doe)
 */
@RunWith(JUnit4.class)
public class FooTest {

	public static enum DateType {
		YEAR;
	}

	SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");

	@Test
	public void twoYearsFromDate() throws Exception {
		Date expDate = ExpirationDateCalculator.calculate(sdf.parse("01/01/2012"), DateType.YEAR, 2);
		assertEquals("01/01/2014", sdf.format(expDate));
	}

	@Test
	public void december31stOfEvenNumberedYear() throws Exception {

	}

	@Test
	@Ignore
	public void thisIsIgnored() {
	}
}
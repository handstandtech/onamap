package com.example.foo;

import java.util.Calendar;
import java.util.Date;

import com.example.foo.FooTest.DateType;

public class ExpirationDateCalculator {

	public static Date calculate(Date date, DateType dateType, int amount) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		switch(dateType){
		case YEAR:
		{
			calendar.add(Calendar.YEAR, amount);
			break;
		}
		}
		return calendar.getTime();
		
	}

}

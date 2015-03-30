package com.facebook.device.yearclass.sample;

import com.facebook.device.yearclass.YearClass;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {
	@Test
	public void SampleTest() {
        MainActivity activity = Robolectric.setupActivity(MainActivity.class);
        assertEquals(YearClass.get(activity), 1);
	}

}

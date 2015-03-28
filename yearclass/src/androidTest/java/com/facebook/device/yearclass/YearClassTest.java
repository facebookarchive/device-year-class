/* Copyright (c) 2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.facebook.device.yearclass;

import android.content.Context;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
public class YearClassTest {

  @PrepareForTest(DeviceInfo.class)
  @Test
  public void testGetYearCategory() {
    // CPU, frequency, RAM, and YearClass values from Samsung Galaxy S5.
    YearClass yearClass = getYearClass(4, 2457600, 1946939392L);
    assertEquals(YearClass.CLASS_2013, yearClass);
  }

  @PrepareForTest(DeviceInfo.class)
  @Test
  public void testEmptyCase() {
    YearClass yearClass = getYearClass(DeviceInfo.DEVICEINFO_UNKNOWN,
        DeviceInfo.DEVICEINFO_UNKNOWN, DeviceInfo.DEVICEINFO_UNKNOWN);
    assertEquals(YearClass.CLASS_UNKNOWN, yearClass);
  }

  @PrepareForTest(DeviceInfo.class)
  @Test
  public void testCoreNums() {
    //Test with only number of cores information available.
    YearClass yearClass = getYearClass(4,
        DeviceInfo.DEVICEINFO_UNKNOWN, DeviceInfo.DEVICEINFO_UNKNOWN);
    assertEquals(YearClass.CLASS_2012, yearClass);
  }

  @PrepareForTest(DeviceInfo.class)
  @Test
  public void testClockSpeed() {
    //Test with only clock speed information available.
    YearClass yearClass = getYearClass(DeviceInfo.DEVICEINFO_UNKNOWN,
        2457600, DeviceInfo.DEVICEINFO_UNKNOWN);
    assertEquals(YearClass.CLASS_2014, yearClass);
  }

  @PrepareForTest(DeviceInfo.class)
  @Test
  public void testTotalRAM() {
    //Test with only total RAM information available.
    YearClass yearClass = getYearClass(DeviceInfo.DEVICEINFO_UNKNOWN,
        DeviceInfo.DEVICEINFO_UNKNOWN, 1946939392L);
    assertEquals(YearClass.CLASS_2013, yearClass);
  }

  private YearClass getYearClass(int numCores, int maxFreqKHz, long memoryBytes) {
    mockStatic(DeviceInfo.class);
    when(DeviceInfo.getNumberOfCPUCores()).thenReturn(numCores);
    when(DeviceInfo.getCPUMaxFreqKHz()).thenReturn(maxFreqKHz);
    when(DeviceInfo.getTotalMemory((Context) anyObject())).thenReturn(memoryBytes);
    YearClass yearClass = YearClass.get(null);
    PowerMockito.verifyStatic();

    return yearClass;
  }
}
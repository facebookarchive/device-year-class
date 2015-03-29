/* Copyright (c) 2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.facebook.device.yearclass;

import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;

public enum YearClass {
  CLASS_UNKNOWN(-1), CLASS_2008(2008), CLASS_2009(2009), CLASS_2010(2010), CLASS_2011(2011),
  CLASS_2012(2012), CLASS_2013(2013), CLASS_2014(2014);

  private static final long MB = 1024 * 1024;
  private static final int MHZ_IN_KHZ = 1000;

  private static YearClass mYearCategory;
  private int mIntValue;

  YearClass(int intValue) {
    this.mIntValue = intValue;
  }

  /**
   * @return a YearClass constructed from the integer value, or CLASS_UNKNOWN if not found.
   */
  public static YearClass fromIntValue(int intValue) {
    for (YearClass yearClass : YearClass.values()) {
      if (intValue == yearClass.mIntValue) {
        return yearClass;
      }
    }
    return CLASS_UNKNOWN;
  }

  /**
   * @return a integer value associated with the YearClass.  For example, CLASS_2008 will return 2008.
   */
  public int getIntValue() {
    return mIntValue;
  }

  /**
   * Calculates the YearClass class by the number of processor cores the phone has.
   * Evaluations are based off the table below:
   * <table border="1">
   * <thead>
   * <tr><th width="50%">Amount</th><th>Year</th></tr>
   * <thead>
   * <tbody>
   * <tr><td>>4 or More</td><td>2012</td></tr>
   * <tr><td>2 or 3</td><td>2011</td></tr>
   * <tr><td>1</td><td>2008</td></tr>
   * </tbody>
   * </table>
   *
   * @return the YearClass in which top-of-the-line phones had the same number of processors as this phone.
   */
  private static YearClass fromNumberOfCores() {
    int cores = DeviceInfo.getNumberOfCPUCores();
    if (cores < 1) return CLASS_UNKNOWN;
    if (cores == 1) return CLASS_2008;
    if (cores <= 3) return CLASS_2011;
    return CLASS_2012;
  }

  /**
   * Calculates the YearClass class by the clock speed of the cores in the phone.
   * Evaluations are based off the table below:
   * <table border="1">
   * <thead>
   * <tr><th width="50%">Amount</th><th>Year</th></tr>
   * <thead>
   * <tbody>
   * <tr><td>>2GHz</td><td>2014</td></tr>
   * <tr><td><=2GHz</td><td>2013</td></tr>
   * <tr><td><=1.5GHz</td><td>2012</td></tr>
   * <tr><td><=1.2GHz</td><td>2011</td></tr>
   * <tr><td><=1GHz</td><td>2010</td></tr>
   * <tr><td><=600MHz</td><td>2009</td></tr>
   * <tr><td><=528MHz</td><td>2008</td></tr>
   * </tbody>
   * </table>
   *
   * @return the YearClass in which top-of-the-line phones had the same clock speed.
   */
  private static YearClass fromClockSpeed() {
    long clockSpeedKHz = DeviceInfo.getCPUMaxFreqKHz();
    if (clockSpeedKHz == DeviceInfo.DEVICEINFO_UNKNOWN) return CLASS_UNKNOWN;
    // These cut-offs include 20MHz of "slop" because my "1.5GHz" Galaxy S3 reports
    // its clock speed as 1512000. So we add a little slop to keep things nominally correct.
    if (clockSpeedKHz <= 528 * MHZ_IN_KHZ) return CLASS_2008;
    if (clockSpeedKHz <= 620 * MHZ_IN_KHZ) return CLASS_2009;
    if (clockSpeedKHz <= 1020 * MHZ_IN_KHZ) return CLASS_2010;
    if (clockSpeedKHz <= 1220 * MHZ_IN_KHZ) return CLASS_2011;
    if (clockSpeedKHz <= 1520 * MHZ_IN_KHZ) return CLASS_2012;
    if (clockSpeedKHz <= 2020 * MHZ_IN_KHZ) return CLASS_2013;
    return CLASS_2014;
  }

  /**
   * Calculates the YearClass class by the amount of RAM the phone has.
   * Evaluations are based off the table below:
   * <table border="1">
   * <thead>
   * <tr><th width="50%">Amount</th><th>Year</th></tr>
   * <thead>
   * <tbody>
   * <tr><td>>2GB</td><td>2014</td></tr>
   * <tr><td><=2GB</td><td>2013</td></tr>
   * <tr><td><=1.5GB</td><td>2012</td></tr>
   * <tr><td><=1GB</td><td>2011</td></tr>
   * <tr><td><=512MB</td><td>2010</td></tr>
   * <tr><td><=256MB</td><td>2009</td></tr>
   * <tr><td><=128MB</td><td>2008</td></tr>
   * </tbody>
   * </table>
   *
   * @return the YearClass in which top-of-the-line phones had the same amount of RAM as this phone.
   */
  private static YearClass fromRamTotal(Context c) {
    long totalRam = DeviceInfo.getTotalMemory(c);
    if (totalRam == DeviceInfo.DEVICEINFO_UNKNOWN) return CLASS_UNKNOWN;
    if (totalRam <= 192 * MB) return CLASS_2008;
    if (totalRam <= 290 * MB) return CLASS_2009;
    if (totalRam <= 512 * MB) return CLASS_2010;
    if (totalRam <= 1024 * MB) return CLASS_2011;
    if (totalRam <= 1536 * MB) return CLASS_2012;
    if (totalRam <= 2048 * MB) return CLASS_2013;
    return CLASS_2014;
  }

  /**
   * Entry Point of YearClass. Extracts YearClass variable with memoizing.
   * Example usage:
   * <p>
   * <pre>
   *   int yearClass = YearClass.get(context);
   * </pre>
   */
  public static YearClass get(Context c) {
    if (mYearCategory == null) {
      synchronized(YearClass.class) {
        if (mYearCategory == null) {
          mYearCategory = categorizeByYear(c);
        }
      }
    }
    return mYearCategory;
  }

  private static void conditionallyAdd(ArrayList<YearClass> list, YearClass value) {
    if (value != YearClass.CLASS_UNKNOWN) {
      list.add(value);
    }
  }

  /**
   * Calculates the "best-in-class year" of the device. This represents the top-end or flagship
   * devices of that year, not the actual release YearClass of the phone. For example, the Galaxy Duos
   * S was released in 2012, but its specs are very similar to the Galaxy S that was released in
   * 2010 as a then top-of-the-line phone, so it is a 2010 device.
   *
   * @return The YearClass when this device would have been considered top-of-the-line.
   */
  private static YearClass categorizeByYear(Context c) {
    ArrayList<YearClass> componentYears = new ArrayList<YearClass>();
    conditionallyAdd(componentYears, YearClass.fromNumberOfCores());
    conditionallyAdd(componentYears, YearClass.fromClockSpeed());
    conditionallyAdd(componentYears, YearClass.fromRamTotal(c));
    if (componentYears.isEmpty())
      return YearClass.CLASS_UNKNOWN;
    Collections.sort(componentYears);
    if ((componentYears.size() & 0x01) == 1) {  // Odd number; pluck the median.
      return componentYears.get(componentYears.size() / 2);
    } else { // Even number. Average the two "center" values.
      int baseIndex = componentYears.size() / 2 - 1;
      // There's an implicit rounding down in here; 2011.5 becomes 2011.
      int averageRoundedDown = componentYears.get(baseIndex).mIntValue +
              (componentYears.get(baseIndex + 1).mIntValue - componentYears.get(baseIndex).mIntValue) / 2;
      return YearClass.fromIntValue(averageRoundedDown);
    }
  }
}
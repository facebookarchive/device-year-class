/* Copyright (c) 2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.facebook.device.yearclass;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

public class YearClass {
  // Year definitions
  public static final int CLASS_UNKNOWN = -1;
  public static final int CLASS_2008 = 2008;
  public static final int CLASS_2009 = 2009;
  public static final int CLASS_2010 = 2010;
  public static final int CLASS_2011 = 2011;
  public static final int CLASS_2012 = 2012;
  public static final int CLASS_2013 = 2013;
  public static final int CLASS_2014 = 2014;
  public static final int CLASS_2015 = 2015;
  public static final int CLASS_2016 = 2016;

  private static final long MB = 1024 * 1024;
  private static final int MHZ_IN_KHZ = 1000;

  private volatile static Integer mYearCategory;

  /**
   * Entry Point of YearClass. Extracts YearClass variable with memoizing.
   * Example usage:
   * <p>
   * <pre>
   *   int yearClass = YearClass.get(context);
   * </pre>
   */
  public static int get(Context c) {
    if (mYearCategory == null) {
      synchronized(YearClass.class) {
        if (mYearCategory == null) {
          mYearCategory = categorizeByYear2016Method(c);
        }
      }
    }
    return mYearCategory;
  }

  private static void conditionallyAdd(ArrayList<Integer> list, int value) {
    if (value != CLASS_UNKNOWN) {
      list.add(value);
    }
  }

  /**
   * This formulation of year class smooths out the distribution of devices in the field
   * in early 2016 so that the buckets are a bit more even in size and performance metrics
   * (specifically app startup time, scrolling perf, animations) are more uniform within
   * the buckets than with the 2014 calculations.
   */
  private static int categorizeByYear2016Method(Context c) {
    long totalRam = DeviceInfo.getTotalMemory(c);
    if (totalRam == DeviceInfo.DEVICEINFO_UNKNOWN) {
      return categorizeByYear2014Method(c);
    }

    if (totalRam <= 768 * MB) {
      return DeviceInfo.getNumberOfCPUCores() <= 1 ? CLASS_2009 : CLASS_2010;
    }
    if (totalRam <= 1024 * MB) {
      return DeviceInfo.getCPUMaxFreqKHz() < 1300 * MHZ_IN_KHZ ? CLASS_2011 : CLASS_2012;
    }
    if (totalRam <= 1536 * MB) {
      return DeviceInfo.getCPUMaxFreqKHz() < 1800 * MHZ_IN_KHZ ? CLASS_2012 : CLASS_2013;
    }
    if (totalRam <= 2048 * MB) {
      return CLASS_2013;
    }
    if (totalRam <= 3 * 1024 * MB) {
      return CLASS_2014;
    }
    return totalRam <= 5 * 1024 * MB ? CLASS_2015 : CLASS_2016;
  }

  /**
   * Calculates the "best-in-class year" of the device. This represents the top-end or flagship
   * devices of that year, not the actual release year of the phone. For example, the Galaxy Duos
   * S was released in 2012, but its specs are very similar to the Galaxy S that was released in
   * 2010 as a then top-of-the-line phone, so it is a 2010 device.
   *
   * @return The year when this device would have been considered top-of-the-line.
   */
  private static int categorizeByYear2014Method(Context c) {
    ArrayList<Integer> componentYears = new ArrayList<Integer>();
    conditionallyAdd(componentYears, getNumCoresYear());
    conditionallyAdd(componentYears, getClockSpeedYear());
    conditionallyAdd(componentYears, getRamYear(c));
    if (componentYears.isEmpty())
      return CLASS_UNKNOWN;
    Collections.sort(componentYears);
    if ((componentYears.size() & 0x01) == 1) {  // Odd number; pluck the median.
      return componentYears.get(componentYears.size() / 2);
    } else { // Even number. Average the two "center" values.
      int baseIndex = componentYears.size() / 2 - 1;
      // There's an implicit rounding down in here; 2011.5 becomes 2011.
      return componentYears.get(baseIndex) +
          (componentYears.get(baseIndex + 1) - componentYears.get(baseIndex)) / 2;
    }
  }

  /**
   * Calculates the year class by the number of processor cores the phone has.
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
   * @return the year in which top-of-the-line phones had the same number of processors as this phone.
   */
  private static int getNumCoresYear() {
    int cores = DeviceInfo.getNumberOfCPUCores();
    if (cores < 1) return CLASS_UNKNOWN;
    if (cores == 1) return CLASS_2008;
    if (cores <= 3) return CLASS_2011;
    return CLASS_2012;
  }

  /**
   * Calculates the year class by the clock speed of the cores in the phone.
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
   * @return the year in which top-of-the-line phones had the same clock speed.
   */
  private static int getClockSpeedYear() {
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
   * Calculates the year class by the amount of RAM the phone has.
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
   * @return the year in which top-of-the-line phones had the same amount of RAM as this phone.
   */
  private static int getRamYear(Context c) {
    long totalRam = DeviceInfo.getTotalMemory(c);
    if (totalRam <= 0) return CLASS_UNKNOWN;
    if (totalRam <= 192 * MB) return CLASS_2008;
    if (totalRam <= 290 * MB) return CLASS_2009;
    if (totalRam <= 512 * MB) return CLASS_2010;
    if (totalRam <= 1024 * MB) return CLASS_2011;
    if (totalRam <= 1536 * MB) return CLASS_2012;
    if (totalRam <= 2048 * MB) return CLASS_2013;
    return CLASS_2014;
  }
}

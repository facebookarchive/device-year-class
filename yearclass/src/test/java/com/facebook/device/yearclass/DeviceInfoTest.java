/* Copyright (c) 2015, Facebook, Inc.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.facebook.device.yearclass;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
public class DeviceInfoTest {

    @Test
    public void testFileStringValid() {
        assertEquals(DeviceInfo.getCoresFromFileString("0-3"), 4);
        assertEquals(DeviceInfo.getCoresFromFileString("0-11"), 12);
    }

    @Test
    public void testFileStringInvalid() {
        assertEquals(DeviceInfo.getCoresFromFileString("INVALIDSTRING"), -1);
        assertEquals(DeviceInfo.getCoresFromFileString("0-2a"), -1);
        assertEquals(DeviceInfo.getCoresFromFileString("035"), -1);
    }
}

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.fredy.filerenamer.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * @author fredy
 */
public class FileNameUtilsTest {

    @Test
    public void testGetBaseNameWithExtension() {
        assertEquals("test.123", FileNameUtils.getBaseName(
                FileNameUtils.normalize("/home/test.123.txt")));
    }

    @Test
    public void testGetBaseNameWithoutExtension() {
        assertEquals("test", FileNameUtils.getBaseName(
                FileNameUtils.normalize("/home/test")));
    }

    @Test
    public void testGetExtensionWithExtension() {
        assertEquals(".txt", FileNameUtils.getExtension(
                FileNameUtils.normalize("/home/test.123.txt")));
    }

    @Test
    public void testGetExtensionWithoutExtension() {
        assertEquals("", FileNameUtils.getExtension(
                FileNameUtils.normalize("/home/test")));
    }

    @Test
    public void testGetFullPathWithExtension() {
        assertEquals(FileNameUtils.normalize("/home/fredy/"),
                FileNameUtils.getFullPath(
                        FileNameUtils.normalize("/home/fredy/test.txt")));
    }

    @Test
    public void testNormalize() {
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            assertEquals("\\home\\fredy\\test", FileNameUtils.normalize("/home/fredy/test"));
        } else if (System.getProperty("os.name").toLowerCase().contains("linux")) {
            assertEquals("/home/fredy/test", FileNameUtils.normalize("\\home\\fredy\\test"));
        }
    }

    @Test
    public void testTrimPath() {
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/abc/test.txt"));
        fileNames.add(FileNameUtils.normalize("/home/def/test.txt"));

        List<String> trimmedFileNames = FileNameUtils.trimPath(fileNames);
        assertEquals("test.txt", trimmedFileNames.get(0));
        assertEquals("test.txt", trimmedFileNames.get(1));
    }

    @Test
    public void testIsValidFileName() {
        assertTrue(FileNameUtils.isValidFileName("test123'+abc.txt"));
        assertFalse(FileNameUtils.isValidFileName("test123'+|abc.txt"));
    }
}

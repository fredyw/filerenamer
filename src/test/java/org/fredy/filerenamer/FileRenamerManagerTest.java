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

package org.fredy.filerenamer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.replacer.ReplacerFactory;
import org.fredy.filerenamer.replacer.ReplacerType;
import org.fredy.filerenamer.util.FileUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/*
 * @author fredy
 */
public class FileRenamerManagerTest {

    @BeforeClass
    public static void setUp() throws Exception {
        new File("./testdata/dir1").mkdirs();
        new File("./testdata/dir2").mkdirs();
        new File("./testdata/1 Hello World.txt").createNewFile();
        new File("./testdata/2 Hello World.txt").createNewFile();
        new File("./testdata/dir1/3 Hello World.txt").createNewFile();
        new File("./testdata/dir1/4 Hello World.txt").createNewFile();
        new File("./testdata/dir2/5 Hello World.txt").createNewFile();
        new File("./testdata/dir2/6 Hello World.txt").createNewFile();
    }

    @AfterClass
    public static void cleanUp() {
        FileUtils.deleteDirectory(new File("./testdata"));
    }

    @Test
    public void testPreviewForDir() {
        FileRenamerManager mgr = new FileRenamerManager();
        Map<AttributeKey, Object> attrs = new HashMap<AttributeKey, Object>();
        attrs.put(AttributeKey.FROM_PATTERN, "\\s+");
        attrs.put(AttributeKey.TO_PATTERN, "_");
        attrs.put(AttributeKey.TURN_ON_REGEX, Boolean.TRUE);
        Replacer replacer = ReplacerFactory.getInstance(ReplacerType.PATTERN, attrs);
        Map<File, File> files = mgr.preview(replacer, new File("./testdata/dir1"));
        assertEquals("3_Hello_World.txt", files.get(
                new File("./testdata/dir1/3 Hello World.txt")).getName());
        assertEquals("4_Hello_World.txt", files.get(
                new File("./testdata/dir1/4 Hello World.txt")).getName());
    }

    @Test
    public void testPreviewForListOfFiles() {
        FileRenamerManager mgr = new FileRenamerManager();
        Map<AttributeKey, Object> attrs = new HashMap<AttributeKey, Object>();
        attrs.put(AttributeKey.FROM_PATTERN, "\\s+");
        attrs.put(AttributeKey.TO_PATTERN, "_");
        attrs.put(AttributeKey.TURN_ON_REGEX, Boolean.TRUE);
        Replacer replacer = ReplacerFactory.getInstance(ReplacerType.PATTERN, attrs);
        Map<File, File> files = mgr.preview(replacer, new File("./testdata/dir2"));
        assertEquals("5_Hello_World.txt", files.get(
                new File("./testdata/dir2/5 Hello World.txt")).getName());
        assertEquals("6_Hello_World.txt", files.get(
                new File("./testdata/dir2/6 Hello World.txt")).getName());
    }

    @Test
    public void testRename() {
        FileRenamerManager mgr = new FileRenamerManager();
        Map<AttributeKey, Object> attrs = new HashMap<AttributeKey, Object>();
        attrs.put(AttributeKey.FROM_PATTERN, "\\s+");
        attrs.put(AttributeKey.TO_PATTERN, "_");
        attrs.put(AttributeKey.TURN_ON_REGEX, Boolean.TRUE);
        Replacer replacer = ReplacerFactory.getInstance(ReplacerType.PATTERN, attrs);
        Map<File, File> files = mgr.preview(replacer, new File("./testdata/dir1"));
        mgr.rename(files);
        assertTrue(files.get(new File("./testdata/dir1/3 Hello World.txt")).exists());
        assertTrue(files.get(new File("./testdata/dir1/4 Hello World.txt")).exists());
    }
}

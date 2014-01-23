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

import java.io.File;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author fredy
 */
public class FileUtilsTest {

    @BeforeClass
    public static void setUp() throws Exception {
        new File("./testdata/dir1/dir2").mkdirs();
        new File("./testdata/abc.txt").createNewFile();
        new File("./testdata/def.txt").createNewFile();
        new File("./testdata/dir1/ghi.txt").createNewFile();
        new File("./testdata/dir1/dir2/jkl.txt").createNewFile();
    }

    @AfterClass
    public static void cleanUp() {
        FileUtils.deleteDirectory(new File("./testdata"));
    }

    @Test
    public void testGetFileListing() throws Exception {
        List<File> files = FileUtils.getFileListing(new File("./testdata"));
        for (File file : files) {
            System.out.println(file);
        }
        assertEquals(4, files.size());
    }
}

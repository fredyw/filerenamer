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

package org.fredy.filerenamer.replacer.impl;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.replacer.ReplacerFactory;
import org.fredy.filerenamer.replacer.ReplacerType;
import org.fredy.filerenamer.util.FileNameUtils;
import org.junit.Test;

/**
 * @author fredy
 */
public class AllUpperCaseReplacerImplTest {

    @Test
    public void testReplaceWithExtension() {
        Replacer replacer = ReplacerFactory.getInstance(ReplacerType.ALL_UPPER_CASE, null);
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/abc.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/123abc.txt"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/ABC.txt"), newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/123ABC.txt"), newFileNames.get(1));
    }

    @Test
    public void testReplaceWithoutExtension() {
        Replacer replacer = ReplacerFactory.getInstance(ReplacerType.ALL_UPPER_CASE, null);
        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/abc"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/123abc"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/ABC"), newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/123ABC"), newFileNames.get(1));
    }
}
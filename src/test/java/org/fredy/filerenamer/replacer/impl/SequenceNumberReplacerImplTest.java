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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.replacer.ReplacerFactory;
import org.fredy.filerenamer.replacer.ReplacerType;
import org.fredy.filerenamer.util.FileNameUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author fredy
 */
public class SequenceNumberReplacerImplTest {

    @Test
    public void testReplaceWithExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.INITIAL_NO_FOR_START, Integer.valueOf(1));
        attributes.put(AttributeKey.INITIAL_NO_FOR_END, Integer.valueOf(3));
        attributes.put(AttributeKey.INITIAL_NO_FOR_INDEX, Integer.valueOf(6));
        attributes.put(AttributeKey.INDEX_NO, Integer.valueOf(2));

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.SEQUENCE_NUMBER, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/axz.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/bxz.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/cxz.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/dxz.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/exz.txt"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/1ax6z3.txt"), newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/2bx7z4.txt"), newFileNames.get(1));
        assertEquals(FileNameUtils.normalize("/home/fredy/3cx8z5.txt"), newFileNames.get(2));
        assertEquals(FileNameUtils.normalize("/home/fredy/4dx9z6.txt"), newFileNames.get(3));
        assertEquals(FileNameUtils.normalize("/home/fredy/5ex10z7.txt"), newFileNames.get(4));
    }

    @Test
    public void testReplaceWithoutExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.INITIAL_NO_FOR_START, Integer.valueOf(1));
        attributes.put(AttributeKey.INITIAL_NO_FOR_END, Integer.valueOf(3));
        attributes.put(AttributeKey.INITIAL_NO_FOR_INDEX, Integer.valueOf(6));
        attributes.put(AttributeKey.INDEX_NO, Integer.valueOf(2));

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.SEQUENCE_NUMBER, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/axz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/bxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/cxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/dxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/exz"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/1ax6z3"), newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/2bx7z4"), newFileNames.get(1));
        assertEquals(FileNameUtils.normalize("/home/fredy/3cx8z5"), newFileNames.get(2));
        assertEquals(FileNameUtils.normalize("/home/fredy/4dx9z6"), newFileNames.get(3));
        assertEquals(FileNameUtils.normalize("/home/fredy/5ex10z7"), newFileNames.get(4));
    }

    @Test
    public void testReplaceWithEmptyAttributes() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.SEQUENCE_NUMBER, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/axz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/bxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/cxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/dxz"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/exz"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/axz"), newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/bxz"), newFileNames.get(1));
        assertEquals(FileNameUtils.normalize("/home/fredy/cxz"), newFileNames.get(2));
        assertEquals(FileNameUtils.normalize("/home/fredy/dxz"), newFileNames.get(3));
        assertEquals(FileNameUtils.normalize("/home/fredy/exz"), newFileNames.get(4));
    }
}

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
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fredy.filerenamer.InvalidFileNameException;
import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.replacer.ReplacerFactory;
import org.fredy.filerenamer.replacer.ReplacerType;
import org.fredy.filerenamer.util.FileNameUtils;
import org.junit.Test;

/**
 * @author fredy
 */
public class PatternReplacerImplTest {

    @Test
    public void testReplaceRegexWithExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.FROM_PATTERN, "\\s+");
        attributes.put(AttributeKey.TO_PATTERN, "_");
        attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.TRUE);

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.PATTERN, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/123 Hello World.txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/test.txt"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/123_Hello_World.txt"),
                newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/test.txt"),
                newFileNames.get(1));
    }

    @Test
    public void testReplaceRegexWithoutExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.FROM_PATTERN, "\\s+");
        attributes.put(AttributeKey.TO_PATTERN, "_");
        attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.TRUE);

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.PATTERN, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/123 Hello World"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/123_Hello_World"),
                newFileNames.get(0));
    }

    @Test
    public void testReplaceNoRegexWithExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.FROM_PATTERN, "{1-,2.3}");
        attributes.put(AttributeKey.TO_PATTERN, "{123}");
        attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.FALSE);

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.PATTERN, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/Hi {1-,2.3}, Hello+World [123].txt"));
        fileNames.add(FileNameUtils.normalize("/home/fredy/Hello {1-,2.3}, Hello+World [123].txt"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/Hi {123}, Hello+World [123].txt"),
                newFileNames.get(0));
        assertEquals(FileNameUtils.normalize("/home/fredy/Hello {123}, Hello+World [123].txt"),
                newFileNames.get(1));
    }

    @Test
    public void testReplaceNoRegexWithoutExtension() {
        Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
        attributes.put(AttributeKey.FROM_PATTERN, "{1-,2-3}");
        attributes.put(AttributeKey.TO_PATTERN, "{123}");
        attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.FALSE);

        Replacer replacer = ReplacerFactory.getInstance(
                ReplacerType.PATTERN, attributes);

        List<String> fileNames = new ArrayList<String>();
        fileNames.add(FileNameUtils.normalize("/home/fredy/Hi {1-,2-3}, Hello+World [123]"));

        List<String> newFileNames = replacer.replace(fileNames);
        assertEquals(FileNameUtils.normalize("/home/fredy/Hi {123}, Hello+World [123]"),
                newFileNames.get(0));
    }

    @Test
    public void testReplaceWithInvalidFromPattern() {
        try {
            Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
            attributes.put(AttributeKey.FROM_PATTERN, "{1-,2-3}?");
            attributes.put(AttributeKey.TO_PATTERN, "{123}");
            attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.FALSE);

            ReplacerFactory.getInstance(ReplacerType.PATTERN, attributes);
            fail("InvalidFileNameException should be thrown");
        } catch (InvalidFileNameException e) {
        }
    }

    @Test
    public void testReplaceWithInvalidToPattern() {
        try {
            Map<AttributeKey, Object> attributes = new HashMap<AttributeKey, Object>();
            attributes.put(AttributeKey.FROM_PATTERN, "{1-,2-3}");
            attributes.put(AttributeKey.TO_PATTERN, "{123}?");
            attributes.put(AttributeKey.TURN_ON_REGEX, Boolean.FALSE);

            ReplacerFactory.getInstance(ReplacerType.PATTERN, attributes);
            fail("InvalidFileNameException should be thrown");
        } catch (InvalidFileNameException e) {
        }
    }
}

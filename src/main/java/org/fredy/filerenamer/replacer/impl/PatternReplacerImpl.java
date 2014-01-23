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
import java.util.List;
import java.util.Map;

import org.fredy.filerenamer.FileRenamerException;
import org.fredy.filerenamer.InvalidFileNameException;
import org.fredy.filerenamer.replacer.AbstractReplacer;
import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.util.FileNameUtils;
import org.fredy.filerenamer.util.RegexEscapeUtils;

/**
 * Replace the file name based on the pattern or regex.
 * @author fredy
 */
public class PatternReplacerImpl extends AbstractReplacer {

    private String fromPattern;
    private String toPattern;
    private boolean turnOnRegex;
    private boolean extensionIgnored = true;

    public PatternReplacerImpl(Map<AttributeKey, Object> attributes) {
        super(attributes);
        if (attributes.get(AttributeKey.FROM_PATTERN) == null) {
            throw new FileRenamerException("The " + AttributeKey.FROM_PATTERN + " can't be found");
        }
        if (attributes.get(AttributeKey.TO_PATTERN) == null) {
            throw new FileRenamerException("The " + AttributeKey.TO_PATTERN + " can't be found");
        }
        if (attributes.get(AttributeKey.IGNORE_EXTENSION) != null) {
            extensionIgnored = (Boolean) attributes.get(AttributeKey.IGNORE_EXTENSION);
        }
        if (attributes.get(AttributeKey.TURN_ON_REGEX) != null) {
            turnOnRegex = (Boolean) attributes.get(AttributeKey.TURN_ON_REGEX);
        }
        fromPattern = (String) attributes.get(AttributeKey.FROM_PATTERN);
        if (!turnOnRegex) {
            if (!FileNameUtils.isValidFileName(fromPattern)) {
                throw new InvalidFileNameException(
                        "A file name can't contain any of these \\/:*?\"<>| characters");
            }
        }
        toPattern = (String) attributes.get(AttributeKey.TO_PATTERN);
        if (!FileNameUtils.isValidFileName(toPattern)) {
            throw new InvalidFileNameException(
                    "A file name can't contain any of these \\/:*?\"<>| characters");
        }
        if (!turnOnRegex) {
            fromPattern = RegexEscapeUtils.escapeRegex(fromPattern);
        }
    }

    @Override
    public List<String> replace(List<String> fileNames) {
        List<String> newFileNames = new ArrayList<String>();
        for (String fileName : fileNames) {
            newFileNames.add(replaceWithPattern(fileName));
        }
        return newFileNames;
    }

    private String replaceWithPattern(String fileName) {
        String fullPath = FileNameUtils.getFullPath(fileName);
        String srcFileName = FileNameUtils.getBaseName(fileName);
        String extension = "";
        if (extensionIgnored) {
            extension = FileNameUtils.getExtension(fileName);
        }
        String newFileName = srcFileName.replaceAll(fromPattern, toPattern);
        String destFileName = fullPath + newFileName + extension;

        return destFileName;
    }
}

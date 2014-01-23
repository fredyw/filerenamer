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

/**
 * Insert characters into the start or end file name or by specifying the index no.
 * @author fredy
 */
public class CharactersReplacerImpl extends AbstractReplacer {

    private String charsForStart;
    private String charsForEnd;
    private String charsForIndex;
    private int indexNo;
    private boolean startFlag;
    private boolean endFlag;
    private boolean indexFlag;

    public CharactersReplacerImpl(Map<AttributeKey, Object> attributes) {
        super(attributes);
        if (attributes.get(AttributeKey.CHARS_FOR_START) != null) {
            charsForStart = (String) attributes.get(AttributeKey.CHARS_FOR_START);
            if (!FileNameUtils.isValidFileName(charsForStart)) {
                throw new InvalidFileNameException(
                        "A file name can't contain any of these \\/:*?\"<>| characters");
            }
            startFlag = true;
        }
        if (attributes.get(AttributeKey.CHARS_FOR_END) != null) {
            charsForEnd = (String) attributes.get(AttributeKey.CHARS_FOR_END);
            if (!FileNameUtils.isValidFileName(charsForEnd)) {
                throw new InvalidFileNameException(
                        "A file name can't contain any of these \\/:*?\"<>| characters");
            }
            endFlag = true;
        }
        if (attributes.get(AttributeKey.CHARS_FOR_INDEX) != null) {
            charsForIndex = (String) attributes.get(AttributeKey.CHARS_FOR_INDEX);
            if (!FileNameUtils.isValidFileName(charsForIndex)) {
                throw new InvalidFileNameException(
                        "A file name can't contain any of these \\/:*?\"<>| characters");
            }
            if (attributes.get(AttributeKey.INDEX_NO) == null) {
                throw new FileRenamerException("The " + AttributeKey.INDEX_NO
                        + " key can't be found");
            }
            indexNo = (Integer) attributes.get(AttributeKey.INDEX_NO);
            indexFlag = true;
        }
    }

    @Override
    public List<String> replace(List<String> fileNames) {
        List<String> newFileNames = new ArrayList<String>();
        for (String fileName : fileNames) {
            newFileNames.add(addChars(fileName));
        }
        return newFileNames;
    }

    public String addChars(String fileName) {
        String fullPath = FileNameUtils.getFullPath(fileName);
        String srcFileName = FileNameUtils.getBaseName(fileName);
        String extension = FileNameUtils.getExtension(fileName);
        StringBuilder newFileName = new StringBuilder(srcFileName);
        if (indexFlag) {
            if (indexNo < newFileName.length()) {
                newFileName.insert(indexNo, charsForIndex);
            }
        }
        if (startFlag) {
            newFileName.insert(0, charsForStart);
        }
        if (endFlag) {
            newFileName.insert(newFileName.length(), charsForEnd);
        }
        String destFileName = fullPath + newFileName.toString() + extension;

        return destFileName;
    }
}

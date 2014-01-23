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
import org.fredy.filerenamer.replacer.AbstractReplacer;
import org.fredy.filerenamer.replacer.AttributeKey;
import org.fredy.filerenamer.util.FileNameUtils;

/**
 * Insert sequence number into the start or end file name or by specifying the
 * index no.
 * @author fredy
 */
public class SequenceNumberReplacerImpl extends AbstractReplacer {

    private int initialNoForStart;
    private int initialNoForEnd;
    private int initialNoForIndex;
    private int indexNo;
    private boolean startFlag;
    private boolean endFlag;
    private boolean indexFlag;

    public SequenceNumberReplacerImpl(Map<AttributeKey, Object> attributes) {
        super(attributes);
        if (attributes.get(AttributeKey.INITIAL_NO_FOR_START) != null) {
            initialNoForStart = (Integer) attributes.get(AttributeKey.INITIAL_NO_FOR_START);
            startFlag = true;
        }
        if (attributes.get(AttributeKey.INITIAL_NO_FOR_END) != null) {
            initialNoForEnd = (Integer) attributes.get(AttributeKey.INITIAL_NO_FOR_END);
            endFlag = true;
        }
        if (attributes.get(AttributeKey.INITIAL_NO_FOR_INDEX) != null) {
            initialNoForIndex = (Integer) attributes.get(AttributeKey.INITIAL_NO_FOR_INDEX);
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
            newFileNames.add(addSequenceNumber(fileName, initialNoForStart++,
                    initialNoForIndex++, initialNoForEnd++));
        }
        return newFileNames;
    }

    public String addSequenceNumber(String fileName, int startSeqNo,
            int indexSeqNo, int endSeqNo) {
        String fullPath = FileNameUtils.getFullPath(fileName);
        String srcFileName = FileNameUtils.getBaseName(fileName);
        String extension = FileNameUtils.getExtension(fileName);
        StringBuilder newFileName = new StringBuilder(srcFileName);
        if (indexFlag) {
            if (indexNo < newFileName.length()) {
                newFileName.insert(indexNo, indexSeqNo);
            }
        }
        if (startFlag) {
            newFileName.insert(0, startSeqNo);
        }
        if (endFlag) {
            newFileName.insert(newFileName.length(), endSeqNo);
        }
        String destFileName = fullPath + newFileName.toString() + extension;

        return destFileName;
    }
}

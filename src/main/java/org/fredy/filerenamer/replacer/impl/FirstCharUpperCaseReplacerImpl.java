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
import java.util.regex.Pattern;
import org.fredy.filerenamer.replacer.AbstractReplacer;
import org.fredy.filerenamer.util.FileNameUtils;

/**
 * Replace only the first character of each word into upper case.
 * @author fredy
 */
public class FirstCharUpperCaseReplacerImpl extends AbstractReplacer {

    @Override
    public List<String> replace(List<String> fileNames) {
        List<String> newFileNames = new ArrayList<String>();
        for (String fileName : fileNames) {
            newFileNames.add(toFirstCharUpperCase(fileName));
        }
        return newFileNames;
    }

    private String toFirstCharUpperCase(String fileName) {
        String fullPath = FileNameUtils.getFullPath(fileName);
        String srcFileName = FileNameUtils.getBaseName(fileName);
        String extension = FileNameUtils.getExtension(fileName);
        StringBuilder newFileName = new StringBuilder();
        StringBuilder newWord = new StringBuilder();
        boolean found = false;
        for (int i = 0; i < srcFileName.length(); i++) {
            char character = srcFileName.charAt(i);
            char newChar = character;
            if (Pattern.matches("\\s", Character.toString(character))) {
                found = false;
            } else if (Pattern.matches("[a-zA-Z]", Character.toString(character))) {
                if (!found) {
                    newChar = Character.toUpperCase(character);
                    found = true;
                }
            }
            newWord.append(newChar);
        }
        newFileName.append(newWord.toString());
        String destFileName = fullPath + newFileName.toString() + extension;

        return destFileName;
    }
}

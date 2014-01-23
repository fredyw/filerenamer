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

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Filename utility class.
 * @author fredy
 */
public class FileNameUtils {

    private static final List<String> invalidFileNameChars =
        Arrays.asList(new String[] { "\\", "/", ":", "*", "?", "\"", "<", ">", "|" });

    private FileNameUtils() {
    }

    public static String getBaseName(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf(File.separator) + 1,
                fileName.lastIndexOf("."));
        } else {
            return fileName.substring(fileName.lastIndexOf(File.separator) + 1);
        }
    }

    public static String getExtension(String fileName) {
        if (fileName.contains(".")) {
            return fileName.substring(fileName.lastIndexOf("."));
        }
        return "";
    }

    public static String getFullPath(String fileName) {
        return fileName.substring(0, fileName.lastIndexOf(File.separator) + 1);
    }

    public static String normalize(String fileName) {
        String fileSeparator = File.separator;
        if (System.getProperty("os.name").toLowerCase().contains("windows")) {
            fileSeparator += File.separator;
        }
        return fileName.replaceAll("[/\\\\]+", fileSeparator);
    }

    public static List<String> toFileNames(List<File> files) {
        List<String> fileNames = new ArrayList<String>();
        for (File file : files) {
            fileNames.add(file.getPath());
        }
        return fileNames;
    }

    public static List<String> trimPath(List<String> fileNames) {
        List<String> trimmedFileNames = new ArrayList<String>();
        for (String fileName : fileNames) {
            trimmedFileNames.add(fileName.substring(fileName.lastIndexOf(File.separatorChar)+1));
        }
        return trimmedFileNames;
    }

    public static boolean isValidFileName(String fileName) {
        for (String c : invalidFileNameChars) {
            if (fileName.contains(c)) {
                return false;
            }
        }
        return true;
    }
}

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

import org.fredy.filerenamer.FileRenamerException;

/**
 * File utility class.
 * @author fredy
 */
public class FileUtils {

    private FileUtils() {
    }

    public static void rename(List<File> fromFiles, List<File> toFiles) {
        if (fromFiles.size() != toFiles.size()) {
            throw new FileRenamerException("fromFile size and toFile size aren't equal");
        }
        for (int i = 0; i < fromFiles.size(); i++) {
            fromFiles.get(i).renameTo(toFiles.get(i));
        }
    }

    public static List<File> getFileListing(File file) {
        List<File> list = new ArrayList<File>();
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                list.addAll(getFileListing(f));
            }
        } else {
            return Arrays.asList(new File[] { file });
        }
        return list;
    }

    public static List<File> toFiles(List<String> fileNames) {
        List<File> files = new ArrayList<File>();
        for (String fileName : fileNames) {
            files.add(new File(fileName));
        }
        return files;
    }

    public static boolean deleteDirectory(File file) {
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return file.delete();
    }
}

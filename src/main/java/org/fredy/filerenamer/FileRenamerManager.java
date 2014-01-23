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

package org.fredy.filerenamer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.fredy.filerenamer.replacer.Replacer;
import org.fredy.filerenamer.util.FileNameUtils;
import org.fredy.filerenamer.util.FileUtils;

/**
 * This class is used for the actual renaming.
 * @author fredy
 */
public class FileRenamerManager {

    /**
     * Previews the files that are going to be renamed.
     * @param replacer the replacer implementation
     * @param dir the directory
     * @return the map contains key=fromFile and value=toFile
     */
    public Map<File, File> preview(Replacer replacer, File dir) {
        List<File> fromFiles = FileUtils.getFileListing(dir);
        List<File> toFiles = FileUtils.toFiles(replacer.replace(
                FileNameUtils.toFileNames(fromFiles)));
        Map<File, File> map = new LinkedHashMap<File, File>();
        for (int i = 0; i < fromFiles.size(); i++) {
            map.put(fromFiles.get(i), toFiles.get(i));
        }
        return map;
    }

    /**
     * Previews the files that are going to be renamed.
     * @param eplacer the replacer implementation
     * @param files the list of files
     * @return the map contains key=fromFile and value=toFile
     */
    public Map<File, File> preview(Replacer replacer, List<File> files) {
        List<File> fromFiles = new ArrayList<File>();
        for (File file : files) {
            fromFiles.addAll(FileUtils.getFileListing(file));
        }
        List<File> toFiles = FileUtils.toFiles(replacer.replace(
                FileNameUtils.toFileNames(fromFiles)));
        Map<File, File> map = new LinkedHashMap<File, File>();
        for (int i = 0; i < fromFiles.size(); i++) {
            map.put(fromFiles.get(i), toFiles.get(i));
        }
        return map;
    }

    /**
     * Renames the files.
     * @param map the map contains key-fromFile and value=toFile
     */
    public void rename(Map<File, File> map) {
        for (Map.Entry<File, File> entry : map.entrySet()) {
            entry.getKey().renameTo(entry.getValue());
        }
    }
}

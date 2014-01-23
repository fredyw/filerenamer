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

package org.fredy.filerenamer.ui;

public enum ResourceBundleKey {

    LABEL_APPLICATION("label.application"),
    LABEL_PATTERN("label.pattern"),
    LABEL_ALLUPPERCASE("label.alluppercase"),
    LABEL_ALLLOWERCASE("label.alllowercase"),
    LABEL_FIRSTUPPERCASE("label.firstuppercase"),
    LABEL_SEQUENCE_NUMBER("label.sequencenumber"),
    LABEL_CHARACTERS("label.characters"),
    LABEL_UP("label.up"),
    LABEL_DOWN("label.down"),
    LABEL_DELETE("label.delete"),
    LABEL_OK("label.ok"),
    LABEL_CANCEL("label.cancel"),
    LABEL_OPEN("label.open"),
    LABEL_RENAME("label.rename"),
    LABEL_CLEAR("label.clear"),
    LABEL_YEAR("label.year"),
    LABEL_FROM("label.from"),
    LABEL_TO("label.to"),
    LABEL_START("label.start"),
    LABEL_END("label.end"),
    LABEL_INDEX("label.index"),
    LABEL_ZERO_INDEX("label.zeroindex"),
    LABEL_PREVIEW("label.preview"),
    LABEL_CREATED_BY("label.createdby"),
    ERROR_TITLE("error.title"),
    ERROR_MANDATORY_FROM("error.mandatory.from"),
    ERROR_MANDATORY_SELECTION("error.mandatory.selection"),
    ERROR_INVALID_FILENAME("error.invalid.filename");

    private String key;

    private ResourceBundleKey(String key) {
        this.key = key;
    }

    @Override
    public String toString() {
        return key;
    }
}

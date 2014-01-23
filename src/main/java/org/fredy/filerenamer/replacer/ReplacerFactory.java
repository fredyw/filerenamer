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

package org.fredy.filerenamer.replacer;

import java.util.Map;
import org.fredy.filerenamer.FileRenamerException;
import org.fredy.filerenamer.replacer.impl.AllLowerCaseReplacerImpl;
import org.fredy.filerenamer.replacer.impl.PatternReplacerImpl;
import org.fredy.filerenamer.replacer.impl.AllUpperCaseReplacerImpl;
import org.fredy.filerenamer.replacer.impl.CharactersReplacerImpl;
import org.fredy.filerenamer.replacer.impl.FirstCharUpperCaseReplacerImpl;
import org.fredy.filerenamer.replacer.impl.SequenceNumberReplacerImpl;

/**
 * A factory class for creating Replacer instance.
 * @author fredy
 */
public class ReplacerFactory {

    private ReplacerFactory() {
    }

    public static Replacer getInstance(ReplacerType type,
            Map<AttributeKey, Object> attributes) {
        if (type == ReplacerType.PATTERN) {
            return new PatternReplacerImpl(attributes);
        } else if (type == ReplacerType.ALL_LOWER_CASE) {
            return new AllLowerCaseReplacerImpl();
        } else if (type == ReplacerType.ALL_UPPER_CASE) {
            return new AllUpperCaseReplacerImpl();
        } else if (type == ReplacerType.FIRST_CHAR_UPPER_CASE) {
            return new FirstCharUpperCaseReplacerImpl();
        } else if (type == ReplacerType.SEQUENCE_NUMBER) {
            return new SequenceNumberReplacerImpl(attributes);
        } else if (type == ReplacerType.CHARACTERS) {
            return new CharactersReplacerImpl(attributes);
        } else {
            throw new FileRenamerException("Invalid ReplacerType");
        }
    }
}

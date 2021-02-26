package org.apache.maven.building;

/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Describes a problem that was encountered during settings building. A problem
 * can either be an exception that was thrown or a simple string message. In
 * addition, a problem carries a hint about its source, e.g. the settings file
 * that exhibits the problem.
 *
 * @author Benjamin Bentmann
 * @author Robert Scholte
 */
@Getter
@RequiredArgsConstructor
public class Problem {

    /**
     * The different severity levels for a problem, in decreasing order.
     */
    public enum Severity {
        FATAL,
        ERROR,
        WARNING
    }

    @NonNull
    final String source;

    final int lineNumber;
    final int columnNumber;

    final String message;

    final Exception exception;

    final Severity severity;

    public String getLocation() {
        return String.format("%s:[line %d, col %d]", source, lineNumber, columnNumber);
    }

    @Override
    public String toString() {
        return String.format("%s: %s @ %s", severity, message, getLocation());
    }



}

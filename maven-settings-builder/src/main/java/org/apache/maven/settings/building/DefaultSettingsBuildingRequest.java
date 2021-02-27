package org.apache.maven.settings.building;

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
import java.io.File;
import java.util.Properties;

/**
 * Collects settings that control building of effective settings.
 *
 * @author Benjamin Bentmann
 */
public class DefaultSettingsBuildingRequest
        implements SettingsBuildingRequest {

    private File globalSettingsFile;

    private File userSettingsFile;

    private SettingsSource globalSettingsSource;

    private SettingsSource userSettingsSource;

    private Properties systemProperties;

    private Properties userProperties;

    @Override
    public File getGlobalSettingsFile() {
        return globalSettingsFile;
    }

    @Override
    public void setGlobalSettingsFile(File globalSettingsFile) {
        this.globalSettingsFile = globalSettingsFile;

    }

    @Override
    public SettingsSource getGlobalSettingsSource() {
        return globalSettingsSource;
    }

    @Override
    public DefaultSettingsBuildingRequest setGlobalSettingsSource(SettingsSource globalSettingsSource) {
        this.globalSettingsSource = globalSettingsSource;

        return this;
    }

    @Override
    public File getUserSettingsFile() {
        return userSettingsFile;
    }

    @Override
    public void setUserSettingsFile(File userSettingsFile) {
        this.userSettingsFile = userSettingsFile;

    }

    @Override
    public SettingsSource getUserSettingsSource() {
        return userSettingsSource;
    }

    @Override
    public DefaultSettingsBuildingRequest setUserSettingsSource(SettingsSource userSettingsSource) {
        this.userSettingsSource = userSettingsSource;

        return this;
    }

    @Override
    public Properties getSystemProperties() {
        if (systemProperties == null) {
            systemProperties = new Properties();
        }

        return systemProperties;
    }

    @Override
    public void setSystemProperties(Properties systemProperties) {
        if (systemProperties != null) {
            this.systemProperties = new Properties();
            synchronized (systemProperties) { // avoid concurrentmodification if someone else sets/removes an unrelated
                                              // system property
                this.systemProperties.putAll(systemProperties);
            }
        } else {
            this.systemProperties = null;
        }

    }

    @Override
    public Properties getUserProperties() {
        if (userProperties == null) {
            userProperties = new Properties();
        }

        return userProperties;
    }

    @Override
    public void setUserProperties(Properties userProperties) {
        if (userProperties != null) {
            this.userProperties = new Properties();
            this.userProperties.putAll(userProperties);
        } else {
            this.userProperties = null;
        }

    }

}

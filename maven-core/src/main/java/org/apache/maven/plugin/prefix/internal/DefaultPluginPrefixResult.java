package org.apache.maven.plugin.prefix.internal;

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
import org.apache.maven.model.Plugin;
import org.apache.maven.plugin.prefix.PluginPrefixResult;
import org.eclipse.aether.repository.ArtifactRepository;

/**
 * Describes the result of a plugin prefix resolution request.
 *
 * @since 3.0
 * @author Benjamin Bentmann
 */
class DefaultPluginPrefixResult
        implements PluginPrefixResult {

    private String groupId;

    private String artifactId;

    private ArtifactRepository repository;

    DefaultPluginPrefixResult() {
        // does nothing
    }

    DefaultPluginPrefixResult(Plugin plugin) {
        groupId = plugin.getGroupId();
        artifactId = plugin.getArtifactId();
    }

    DefaultPluginPrefixResult(String groupId, String artifactId, ArtifactRepository repository) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.repository = repository;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public ArtifactRepository getRepository() {
        return repository;
    }

    public void setRepository(ArtifactRepository repository) {
        this.repository = repository;
    }

}

package org.apache.maven.project.artifact;

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
import java.util.Collections;
import java.util.List;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.DefaultArtifact;
import org.apache.maven.artifact.handler.ArtifactHandler;
import org.apache.maven.model.Dependency;
import org.apache.maven.model.Plugin;

/**
 * PluginArtifact
 */
public class PluginArtifact
        extends DefaultArtifact
        implements ArtifactWithDependencies {
    private final Plugin plugin;

    public PluginArtifact(Plugin plugin, Artifact pluginArtifact) {
        super(plugin.getGroupId(), plugin.getArtifactId(), plugin.getVersion(), null, "maven-plugin", null,
                new PluginArtifactHandler());
        this.plugin = plugin;
        setFile(pluginArtifact.getFile());
        setResolved(true);
    }

    public List<Dependency> getDependencies() {
        return plugin.getDependencies();
    }

    public List<Dependency> getManagedDependencies() {
        return Collections.emptyList();
    }

    static class PluginArtifactHandler
            implements ArtifactHandler {
        public String getClassifier() {
            return null;
        }

        public String getDirectory() {
            return null;
        }

        public String getExtension() {
            return "jar";
        }

        public String getLanguage() {
            return "none";
        }

        public String getPackaging() {
            return "maven-plugin";
        }

        public boolean isAddedToClasspath() {
            return true;
        }

        public boolean isIncludesDependencies() {
            return false;
        }
    }
}

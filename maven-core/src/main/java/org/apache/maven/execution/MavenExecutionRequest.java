package org.apache.maven.execution;

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
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.eventspy.internal.EventSpyDispatcher;
import org.apache.maven.model.Profile;
import org.apache.maven.project.ProjectBuildingRequest;
// 
// These settings values need to be removed and pushed down into a provider of configuration information
//
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
//
import org.apache.maven.toolchain.model.ToolchainModel;
import org.codehaus.plexus.logging.Logger;
import org.eclipse.aether.RepositoryCache;
import org.eclipse.aether.repository.WorkspaceReader;
import org.eclipse.aether.transfer.TransferListener;

/**
 * @author Jason van Zyl
 */
public interface MavenExecutionRequest {
    // ----------------------------------------------------------------------
    // Logging
    // ----------------------------------------------------------------------

    int LOGGING_LEVEL_DEBUG = Logger.LEVEL_DEBUG;

    int LOGGING_LEVEL_INFO = Logger.LEVEL_INFO;

    int LOGGING_LEVEL_WARN = Logger.LEVEL_WARN;

    int LOGGING_LEVEL_ERROR = Logger.LEVEL_ERROR;

    int LOGGING_LEVEL_FATAL = Logger.LEVEL_FATAL;

    int LOGGING_LEVEL_DISABLED = Logger.LEVEL_DISABLED;

    // ----------------------------------------------------------------------
    // Reactor Failure Mode
    // ----------------------------------------------------------------------

    String REACTOR_FAIL_FAST = "FAIL_FAST";

    String REACTOR_FAIL_AT_END = "FAIL_AT_END";

    String REACTOR_FAIL_NEVER = "FAIL_NEVER";

    // ----------------------------------------------------------------------
    // Reactor Make Mode
    // ----------------------------------------------------------------------

    String REACTOR_MAKE_UPSTREAM = "make-upstream";

    String REACTOR_MAKE_DOWNSTREAM = "make-downstream";

    String REACTOR_MAKE_BOTH = "make-both";

    // ----------------------------------------------------------------------
    // Artifact repository policies
    // ----------------------------------------------------------------------

    String CHECKSUM_POLICY_FAIL = ArtifactRepositoryPolicy.CHECKSUM_POLICY_FAIL;

    String CHECKSUM_POLICY_WARN = ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN;

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    // Base directory
    MavenExecutionRequest setBaseDirectory(File basedir);

    String getBaseDirectory();

    // Timing (remove this)
    void setStartTime(Date start);

    Date getStartTime();

    // Goals
    MavenExecutionRequest setGoals(List<String> goals);

    List<String> getGoals();

    // Properties

    /**
     * Sets the system properties to use for interpolation and profile activation.
     * The system properties are collected from the runtime environment like
     * {@link System#getProperties()} and environment variables.
     *
     * @param systemProperties The system properties, may be {@code null}.
     * @return This request, never {@code null}.
     */
    MavenExecutionRequest setSystemProperties(Properties systemProperties);

    /**
     * Gets the system properties to use for interpolation and profile activation.
     * The system properties are collected from the runtime environment like
     * {@link System#getProperties()} and environment variables.
     *
     * @return The system properties, never {@code null}.
     */
    Properties getSystemProperties();

    /**
     * Sets the user properties to use for interpolation and profile activation. The
     * user properties have been configured directly by the user on his discretion,
     * e.g. via the {@code -Dkey=value} parameter on the command line.
     *
     * @param userProperties The user properties, may be {@code null}.
     */
    void setUserProperties(Properties userProperties);

    /**
     * Gets the user properties to use for interpolation and profile activation. The
     * user properties have been configured directly by the user on his discretion,
     * e.g. via the {@code -Dkey=value} parameter on the command line.
     *
     * @return The user properties, never {@code null}.
     */
    Properties getUserProperties();

    // Reactor
    void setReactorFailureBehavior(String failureBehavior);

    String getReactorFailureBehavior();

    void setSelectedProjects(List<String> projects);

    List<String> getSelectedProjects();

    /**
     * @param projects the projects to exclude
     * @since 3.2
     */
    void setExcludedProjects(List<String> projects);

    /**
     * @return the excluded projects, never {@code null}
     * @since 3.2
     */
    List<String> getExcludedProjects();

    /**
     * Sets whether the build should be resumed from the data in the
     * resume.properties file.
     * 
     * @param resume Whether or not to resume a previous build.
     */
    void setResume(boolean resume);

    /**
     * @return Whether the build should be resumed from the data in the
     *         resume.properties file.
     */
    boolean isResume();

    void setResumeFrom(String project);

    String getResumeFrom();

    void setMakeBehavior(String makeBehavior);

    String getMakeBehavior();

    /**
     * Set's the parallel degree of concurrency used by the build.
     *
     * @param degreeOfConcurrency
     */
    void setDegreeOfConcurrency(int degreeOfConcurrency);

    /**
     * @return the degree of concurrency for the build.
     */
    int getDegreeOfConcurrency();

    // Recursive (really to just process the top-level POM)
    void setRecursive(boolean recursive);

    boolean isRecursive();

    MavenExecutionRequest setPom(File pom);

    File getPom();

    // Errors
    MavenExecutionRequest setShowErrors(boolean showErrors);

    boolean isShowErrors();

    // Transfer listeners
    void setTransferListener(TransferListener transferListener);

    TransferListener getTransferListener();

    // Logging
    void setLoggingLevel(int loggingLevel);

    int getLoggingLevel();

    // Update snapshots
    void setUpdateSnapshots(boolean updateSnapshots);

    boolean isUpdateSnapshots();

    void setNoSnapshotUpdates(boolean noSnapshotUpdates);

    boolean isNoSnapshotUpdates();

    // Checksum policy
    void setGlobalChecksumPolicy(String globalChecksumPolicy);

    String getGlobalChecksumPolicy();

    // Local repository
    void setLocalRepositoryPath(String localRepository);

    void setLocalRepositoryPath(File localRepository);

    File getLocalRepositoryPath();

    MavenExecutionRequest setLocalRepository(ArtifactRepository repository);

    ArtifactRepository getLocalRepository();

    // Interactive
    void setInteractiveMode(boolean interactive);

    boolean isInteractiveMode();

    // Offline
    void setOffline(boolean offline);

    boolean isOffline();

    boolean isCacheTransferError();

    void setCacheTransferError(boolean cacheTransferError);

    boolean isCacheNotFound();

    void setCacheNotFound(boolean cacheNotFound);

    // Profiles
    List<Profile> getProfiles();

    void addProfile(Profile profile);

    MavenExecutionRequest setProfiles(List<Profile> profiles);

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest addActiveProfile(String profile);

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest addActiveProfiles(List<String> profiles);

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest setActiveProfiles(List<String> profiles);

    /**
     * @return The list of profiles that the user wants to activate.
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    List<String> getActiveProfiles();

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest addInactiveProfile(String profile);

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest addInactiveProfiles(List<String> profiles);

    /**
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    MavenExecutionRequest setInactiveProfiles(List<String> profiles);

    /**
     * @return The list of profiles that the user wants to de-activate.
     * @deprecated Use {@link #getProfileActivation()}.
     */
    @Deprecated
    List<String> getInactiveProfiles();

    /**
     * Return the requested activation(s) of profile(s) in this execution.
     * 
     * @return requested (de-)activation(s) of profile(s) in this execution. Never
     *         {@code null}.
     */
    ProfileActivation getProfileActivation();

    // Proxies
    List<Proxy> getProxies();

    MavenExecutionRequest setProxies(List<Proxy> proxies);

    void addProxy(Proxy proxy);

    // Servers
    List<Server> getServers();

    MavenExecutionRequest setServers(List<Server> servers);

    void addServer(Server server);

    // Mirrors
    List<Mirror> getMirrors();

    MavenExecutionRequest setMirrors(List<Mirror> mirrors);

    void addMirror(Mirror mirror);

    // Plugin groups
    List<String> getPluginGroups();

    MavenExecutionRequest setPluginGroups(List<String> pluginGroups);

    void addPluginGroup(String pluginGroup);

    MavenExecutionRequest addPluginGroups(List<String> pluginGroups);

    boolean isProjectPresent();

    MavenExecutionRequest setProjectPresent(boolean isProjectPresent);

    File getUserSettingsFile();

    void setUserSettingsFile(File userSettingsFile);

    File getGlobalSettingsFile();

    void setGlobalSettingsFile(File globalSettingsFile);

    void addRemoteRepository(ArtifactRepository repository);

    void addPluginArtifactRepository(ArtifactRepository repository);

    /**
     * Set a new list of remote repositories to use the execution request. This is
     * necessary if you perform transformations on the remote repositories being
     * used. For example if you replace existing repositories with mirrors then it's
     * easier to just replace the whole list with a new list of transformed
     * repositories.
     *
     * @param repositories
     * @return This request, never {@code null}.
     */
    MavenExecutionRequest setRemoteRepositories(List<ArtifactRepository> repositories);

    List<ArtifactRepository> getRemoteRepositories();

    MavenExecutionRequest setPluginArtifactRepositories(List<ArtifactRepository> repositories);

    List<ArtifactRepository> getPluginArtifactRepositories();

    void setRepositoryCache(RepositoryCache repositoryCache);

    RepositoryCache getRepositoryCache();

    WorkspaceReader getWorkspaceReader();

    MavenExecutionRequest setWorkspaceReader(WorkspaceReader workspaceReader);

    File getUserToolchainsFile();

    void setUserToolchainsFile(File userToolchainsFile);

    /**
     *
     *
     * @return the global toolchains file
     * @since 3.3.0
     */
    File getGlobalToolchainsFile();

    /**
     *
     * @param globalToolchainsFile the global toolchains file
     * @since 3.3.0
     */
    void setGlobalToolchainsFile(File globalToolchainsFile);

    ExecutionListener getExecutionListener();

    void setExecutionListener(ExecutionListener executionListener);

    ProjectBuildingRequest getProjectBuildingRequest();

    /**
     * @since 3.1
     */
    boolean isUseLegacyLocalRepository();

    /**
     * @since 3.1
     */
    void setUseLegacyLocalRepository(boolean useLegacyLocalRepository);

    /**
     * Controls the {@link org.apache.maven.lifecycle.internal.builder.Builder} used
     * by Maven by specification of the builder's id.
     *
     * @since 3.2.0
     */
    void setBuilderId(String builderId);

    /**
     * Controls the {@link org.apache.maven.lifecycle.internal.builder.Builder} used
     * by Maven by specification of the builders id.
     *
     * @since 3.2.0
     */
    String getBuilderId();

    /**
     *
     * @param toolchains all toolchains grouped by type
     * @since 3.3.0
     */
    void setToolchains(Map<String, List<ToolchainModel>> toolchains);

    /**
     *
     * @return all toolchains grouped by type, never {@code null}
     * @since 3.3.0
     */
    Map<String, List<ToolchainModel>> getToolchains();

    /**
     * @since 3.3.0
     */
    void setMultiModuleProjectDirectory(File file);

    /**
     * @since 3.3.0
     */
    File getMultiModuleProjectDirectory();

    /**
     * @since 3.3.0
     */
    void setEventSpyDispatcher(EventSpyDispatcher eventSpyDispatcher);

    /**
     * @since 3.3.0
     */
    EventSpyDispatcher getEventSpyDispatcher();

    /**
     * @since 3.3.0
     */
    Map<String, Object> getData();
}

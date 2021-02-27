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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.eventspy.internal.EventSpyDispatcher;
import org.apache.maven.model.Profile;
import org.apache.maven.project.DefaultProjectBuildingRequest;
import org.apache.maven.project.ProjectBuildingRequest;
import org.apache.maven.properties.internal.SystemProperties;
import org.apache.maven.settings.Mirror;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.apache.maven.toolchain.model.ToolchainModel;
import org.eclipse.aether.DefaultRepositoryCache;
import org.eclipse.aether.RepositoryCache;
import org.eclipse.aether.repository.WorkspaceReader;
import org.eclipse.aether.transfer.TransferListener;

/**
 * @author Jason van Zyl
 */
public class DefaultMavenExecutionRequest
        implements MavenExecutionRequest {

    private RepositoryCache repositoryCache = new DefaultRepositoryCache();

    private WorkspaceReader workspaceReader;

    private ArtifactRepository localRepository;

    private EventSpyDispatcher eventSpyDispatcher;

    private File localRepositoryPath;

    private boolean offline = false;

    private boolean interactiveMode = true;

    private boolean cacheTransferError = false;

    private boolean cacheNotFound = false;

    private List<Proxy> proxies;

    private List<Server> servers;

    private List<Mirror> mirrors;

    private List<Profile> profiles;

    private final ProfileActivation profileActivation = new ProfileActivation();

    private List<String> pluginGroups;

    private boolean isProjectPresent = true;

    // ----------------------------------------------------------------------------
    // We need to allow per execution user and global settings as the embedder
    // might be running in a mode where its executing many threads with totally
    // different settings.
    // ----------------------------------------------------------------------------

    private File userSettingsFile;

    private File globalSettingsFile;

    private File userToolchainsFile;

    private File globalToolchainsFile;

    // ----------------------------------------------------------------------------
    // Request
    // ----------------------------------------------------------------------------

    private File multiModuleProjectDirectory;

    private File basedir;

    private List<String> goals;

    private boolean useReactor = false;

    private boolean recursive = true;

    private File pom;

    private String reactorFailureBehavior = REACTOR_FAIL_FAST;

    private List<String> selectedProjects;

    private List<String> excludedProjects;

    private boolean resume = false;

    private String resumeFrom;

    private String makeBehavior;

    private Properties systemProperties;

    private Properties userProperties;

    private Date startTime;

    private boolean showErrors = false;

    private TransferListener transferListener;

    private int loggingLevel = LOGGING_LEVEL_INFO;

    private String globalChecksumPolicy;

    private boolean updateSnapshots = false;

    private List<ArtifactRepository> remoteRepositories;

    private List<ArtifactRepository> pluginArtifactRepositories;

    private ExecutionListener executionListener;

    private int degreeOfConcurrency = 1;

    private String builderId = "singlethreaded";

    private Map<String, List<ToolchainModel>> toolchains;

    /**
     * Suppress SNAPSHOT updates.
     *
     * @issue MNG-2681
     */
    private boolean noSnapshotUpdates = false;

    private boolean useLegacyLocalRepositoryManager = false;

    private Map<String, Object> data;

    public DefaultMavenExecutionRequest() {
    }

    public static MavenExecutionRequest copy(MavenExecutionRequest original) {
        DefaultMavenExecutionRequest copy = new DefaultMavenExecutionRequest();
        copy.setLocalRepository(original.getLocalRepository());
        copy.setLocalRepositoryPath(original.getLocalRepositoryPath());
        copy.setOffline(original.isOffline());
        copy.setInteractiveMode(original.isInteractiveMode());
        copy.setCacheNotFound(original.isCacheNotFound());
        copy.setCacheTransferError(original.isCacheTransferError());
        copy.setProxies(original.getProxies());
        copy.setServers(original.getServers());
        copy.setMirrors(original.getMirrors());
        copy.setProfiles(original.getProfiles());
        copy.setPluginGroups(original.getPluginGroups());
        copy.setProjectPresent(original.isProjectPresent());
        copy.setUserSettingsFile(original.getUserSettingsFile());
        copy.setGlobalSettingsFile(original.getGlobalSettingsFile());
        copy.setUserToolchainsFile(original.getUserToolchainsFile());
        copy.setGlobalToolchainsFile(original.getGlobalToolchainsFile());
        copy.setBaseDirectory((original.getBaseDirectory() != null) ? new File(original.getBaseDirectory())
                : null);
        copy.setGoals(original.getGoals());
        copy.setRecursive(original.isRecursive());
        copy.setPom(original.getPom());
        copy.setSystemProperties(original.getSystemProperties());
        copy.setUserProperties(original.getUserProperties());
        copy.setShowErrors(original.isShowErrors());
        copy.setActiveProfiles(original.getActiveProfiles());
        copy.setInactiveProfiles(original.getInactiveProfiles());
        copy.setTransferListener(original.getTransferListener());
        copy.setLoggingLevel(original.getLoggingLevel());
        copy.setGlobalChecksumPolicy(original.getGlobalChecksumPolicy());
        copy.setUpdateSnapshots(original.isUpdateSnapshots());
        copy.setRemoteRepositories(original.getRemoteRepositories());
        copy.setPluginArtifactRepositories(original.getPluginArtifactRepositories());
        copy.setRepositoryCache(original.getRepositoryCache());
        copy.setWorkspaceReader(original.getWorkspaceReader());
        copy.setNoSnapshotUpdates(original.isNoSnapshotUpdates());
        copy.setExecutionListener(original.getExecutionListener());
        copy.setUseLegacyLocalRepository(original.isUseLegacyLocalRepository());
        copy.setBuilderId(original.getBuilderId());
        return copy;
    }

    @Override
    public String getBaseDirectory() {
        if (basedir == null) {
            return null;
        }

        return basedir.getAbsolutePath();
    }

    @Override
    public ArtifactRepository getLocalRepository() {
        return localRepository;
    }

    @Override
    public File getLocalRepositoryPath() {
        return localRepositoryPath;
    }

    @Override
    public List<String> getGoals() {
        if (goals == null) {
            goals = new ArrayList<>();
        }
        return goals;
    }

    @Override
    public Properties getSystemProperties() {
        if (systemProperties == null) {
            systemProperties = new Properties();
        }

        return systemProperties;
    }

    @Override
    public Properties getUserProperties() {
        if (userProperties == null) {
            userProperties = new Properties();
        }

        return userProperties;
    }

    @Override
    public File getPom() {
        return pom;
    }

    @Override
    public String getReactorFailureBehavior() {
        return reactorFailureBehavior;
    }

    @Override
    public List<String> getSelectedProjects() {
        if (selectedProjects == null) {
            selectedProjects = new ArrayList<>();
        }

        return selectedProjects;
    }

    @Override
    public List<String> getExcludedProjects() {
        if (excludedProjects == null) {
            excludedProjects = new ArrayList<>();
        }

        return excludedProjects;
    }

    @Override
    public boolean isResume() {
        return resume;
    }

    @Override
    public String getResumeFrom() {
        return resumeFrom;
    }

    @Override
    public String getMakeBehavior() {
        return makeBehavior;
    }

    @Override
    public Date getStartTime() {
        return startTime;
    }

    @Override
    public boolean isShowErrors() {
        return showErrors;
    }

    @Override
    public boolean isInteractiveMode() {
        return interactiveMode;
    }

    @Override
    public MavenExecutionRequest setActiveProfiles(List<String> activeProfiles) {
        if (activeProfiles != null) {
            this.profileActivation.overwriteActiveProfiles(activeProfiles);
        }

        return this;
    }

    @Override
    public MavenExecutionRequest setInactiveProfiles(List<String> inactiveProfiles) {
        if (inactiveProfiles != null) {
            this.profileActivation.overwriteInactiveProfiles(inactiveProfiles);
        }

        return this;
    }

    @Override
    public ProfileActivation getProfileActivation() {
        return this.profileActivation;
    }

    @Override
    public MavenExecutionRequest setRemoteRepositories(List<ArtifactRepository> remoteRepositories) {
        if (remoteRepositories != null) {
            this.remoteRepositories = new ArrayList<>(remoteRepositories);
        } else {
            this.remoteRepositories = null;
        }

        return this;
    }

    @Override
    public MavenExecutionRequest setPluginArtifactRepositories(List<ArtifactRepository> pluginArtifactRepositories) {
        if (pluginArtifactRepositories != null) {
            this.pluginArtifactRepositories = new ArrayList<>(pluginArtifactRepositories);
        } else {
            this.pluginArtifactRepositories = null;
        }

        return this;
    }

    public void setProjectBuildingConfiguration(ProjectBuildingRequest projectBuildingConfiguration) {
        this.projectBuildingRequest = projectBuildingConfiguration;
    }

    @Override
    public List<String> getActiveProfiles() {
        return this.profileActivation.getActiveProfiles();
    }

    @Override
    public List<String> getInactiveProfiles() {
        return this.profileActivation.getInactiveProfiles();
    }

    @Override
    public TransferListener getTransferListener() {
        return transferListener;
    }

    @Override
    public int getLoggingLevel() {
        return loggingLevel;
    }

    @Override
    public boolean isOffline() {
        return offline;
    }

    @Override
    public boolean isUpdateSnapshots() {
        return updateSnapshots;
    }

    @Override
    public boolean isNoSnapshotUpdates() {
        return noSnapshotUpdates;
    }

    @Override
    public String getGlobalChecksumPolicy() {
        return globalChecksumPolicy;
    }

    @Override
    public boolean isRecursive() {
        return recursive;
    }

    // ----------------------------------------------------------------------
    //
    // ----------------------------------------------------------------------

    @Override
    public MavenExecutionRequest setBaseDirectory(File basedir) {
        this.basedir = basedir;

        return this;
    }

    @Override
    public void setStartTime(Date startTime) {
        this.startTime = startTime;

    }

    @Override
    public MavenExecutionRequest setShowErrors(boolean showErrors) {
        this.showErrors = showErrors;

        return this;
    }

    @Override
    public MavenExecutionRequest setGoals(List<String> goals) {
        if (goals != null) {
            this.goals = new ArrayList<>(goals);
        } else {
            this.goals = null;
        }

        return this;
    }

    @Override
    public MavenExecutionRequest setLocalRepository(ArtifactRepository localRepository) {
        this.localRepository = localRepository;

        if (localRepository != null) {
            setLocalRepositoryPath(new File(localRepository.getBasedir()).getAbsoluteFile());
        }

        return this;
    }

    @Override
    public void setLocalRepositoryPath(File localRepository) {
        localRepositoryPath = localRepository;

    }

    @Override
    public void setLocalRepositoryPath(String localRepository) {
        localRepositoryPath = (localRepository != null) ? new File(localRepository) : null;

    }

    @Override
    public MavenExecutionRequest setSystemProperties(Properties properties) {
        if (properties != null) {
            this.systemProperties = SystemProperties.copyProperties(properties);
        } else {
            this.systemProperties = null;
        }

        return this;
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

    @Override
    public void setReactorFailureBehavior(String failureBehavior) {
        reactorFailureBehavior = failureBehavior;

    }

    @Override
    public void setSelectedProjects(List<String> selectedProjects) {
        if (selectedProjects != null) {
            this.selectedProjects = new ArrayList<>(selectedProjects);
        } else {
            this.selectedProjects = null;
        }

    }

    @Override
    public void setExcludedProjects(List<String> excludedProjects) {
        if (excludedProjects != null) {
            this.excludedProjects = new ArrayList<>(excludedProjects);
        } else {
            this.excludedProjects = null;
        }

    }

    @Override
    public void setResume(boolean resume) {
        this.resume = resume;

    }

    @Override
    public void setResumeFrom(String project) {
        this.resumeFrom = project;

    }

    @Override
    public void setMakeBehavior(String makeBehavior) {
        this.makeBehavior = makeBehavior;

    }

    @Override
    public MavenExecutionRequest addActiveProfile(String profile) {
        if (!getActiveProfiles().contains(profile)) {
            getActiveProfiles().add(profile);
        }

        return this;
    }

    @Override
    public MavenExecutionRequest addInactiveProfile(String profile) {
        if (!getInactiveProfiles().contains(profile)) {
            getInactiveProfiles().add(profile);
        }

        return this;
    }

    @Override
    public MavenExecutionRequest addActiveProfiles(List<String> profiles) {
        for (String profile : profiles) {
            addActiveProfile(profile);
        }

        return this;
    }

    @Override
    public MavenExecutionRequest addInactiveProfiles(List<String> profiles) {
        for (String profile : profiles) {
            addInactiveProfile(profile);
        }

        return this;
    }

    public MavenExecutionRequest setUseReactor(boolean reactorActive) {
        useReactor = reactorActive;

        return this;
    }

    public boolean useReactor() {
        return useReactor;
    }

    /** @deprecated use {@link #setPom(File)} */
    @Deprecated
    public MavenExecutionRequest setPomFile(String pomFilename) {
        if (pomFilename != null) {
            pom = new File(pomFilename);
        }

        return this;
    }

    @Override
    public MavenExecutionRequest setPom(File pom) {
        this.pom = pom;

        return this;
    }

    @Override
    public void setInteractiveMode(boolean interactive) {
        interactiveMode = interactive;

    }

    @Override
    public void setTransferListener(TransferListener transferListener) {
        this.transferListener = transferListener;

    }

    @Override
    public void setLoggingLevel(int loggingLevel) {
        this.loggingLevel = loggingLevel;

    }

    @Override
    public void setOffline(boolean offline) {
        this.offline = offline;

    }

    @Override
    public void setUpdateSnapshots(boolean updateSnapshots) {
        this.updateSnapshots = updateSnapshots;

    }

    @Override
    public void setNoSnapshotUpdates(boolean noSnapshotUpdates) {
        this.noSnapshotUpdates = noSnapshotUpdates;

    }

    @Override
    public void setGlobalChecksumPolicy(String globalChecksumPolicy) {
        this.globalChecksumPolicy = globalChecksumPolicy;

    }

    // ----------------------------------------------------------------------------
    // Settings equivalents
    // ----------------------------------------------------------------------------

    @Override
    public List<Proxy> getProxies() {
        if (proxies == null) {
            proxies = new ArrayList<>();
        }
        return proxies;
    }

    @Override
    public MavenExecutionRequest setProxies(List<Proxy> proxies) {
        if (proxies != null) {
            this.proxies = new ArrayList<>(proxies);
        } else {
            this.proxies = null;
        }

        return this;
    }

    @Override
    public void addProxy(Proxy proxy) {
        Objects.requireNonNull(proxy, "proxy cannot be null");

        for (Proxy p : getProxies()) {
            if (p.getId() != null && p.getId().equals(proxy.getId())) {
                return;
            }
        }

        getProxies().add(proxy);

    }

    @Override
    public List<Server> getServers() {
        if (servers == null) {
            servers = new ArrayList<>();
        }
        return servers;
    }

    @Override
    public MavenExecutionRequest setServers(List<Server> servers) {
        if (servers != null) {
            this.servers = new ArrayList<>(servers);
        } else {
            this.servers = null;
        }

        return this;
    }

    @Override
    public void addServer(Server server) {
        Objects.requireNonNull(server, "server cannot be null");

        for (Server p : getServers()) {
            if (p.getId() != null && p.getId().equals(server.getId())) {
                return;
            }
        }

        getServers().add(server);

    }

    @Override
    public List<Mirror> getMirrors() {
        if (mirrors == null) {
            mirrors = new ArrayList<>();
        }
        return mirrors;
    }

    @Override
    public MavenExecutionRequest setMirrors(List<Mirror> mirrors) {
        if (mirrors != null) {
            this.mirrors = new ArrayList<>(mirrors);
        } else {
            this.mirrors = null;
        }

        return this;
    }

    @Override
    public void addMirror(Mirror mirror) {
        Objects.requireNonNull(mirror, "mirror cannot be null");

        for (Mirror p : getMirrors()) {
            if (p.getId() != null && p.getId().equals(mirror.getId())) {
                return;
            }
        }

        getMirrors().add(mirror);

    }

    @Override
    public List<Profile> getProfiles() {
        if (profiles == null) {
            profiles = new ArrayList<>();
        }
        return profiles;
    }

    @Override
    public MavenExecutionRequest setProfiles(List<Profile> profiles) {
        if (profiles != null) {
            this.profiles = new ArrayList<>(profiles);
        } else {
            this.profiles = null;
        }

        return this;
    }

    @Override
    public List<String> getPluginGroups() {
        if (pluginGroups == null) {
            pluginGroups = new ArrayList<>();
        }

        return pluginGroups;
    }

    @Override
    public MavenExecutionRequest setPluginGroups(List<String> pluginGroups) {
        if (pluginGroups != null) {
            this.pluginGroups = new ArrayList<>(pluginGroups);
        } else {
            this.pluginGroups = null;
        }

        return this;
    }

    @Override
    public void addPluginGroup(String pluginGroup) {
        if (!getPluginGroups().contains(pluginGroup)) {
            getPluginGroups().add(pluginGroup);
        }

    }

    @Override
    public MavenExecutionRequest addPluginGroups(List<String> pluginGroups) {
        for (String pluginGroup : pluginGroups) {
            addPluginGroup(pluginGroup);
        }

        return this;
    }

    @Override
    public void setRecursive(boolean recursive) {
        this.recursive = recursive;

    }

    // calculated from request attributes.
    private ProjectBuildingRequest projectBuildingRequest;

    @Override
    public boolean isProjectPresent() {
        return isProjectPresent;
    }

    @Override
    public MavenExecutionRequest setProjectPresent(boolean projectPresent) {
        isProjectPresent = projectPresent;

        return this;
    }

    // Settings files

    @Override
    public File getUserSettingsFile() {
        return userSettingsFile;
    }

    @Override
    public void setUserSettingsFile(File userSettingsFile) {
        this.userSettingsFile = userSettingsFile;

    }

    @Override
    public File getGlobalSettingsFile() {
        return globalSettingsFile;
    }

    @Override
    public void setGlobalSettingsFile(File globalSettingsFile) {
        this.globalSettingsFile = globalSettingsFile;

    }

    @Override
    public File getUserToolchainsFile() {
        return userToolchainsFile;
    }

    @Override
    public void setUserToolchainsFile(File userToolchainsFile) {
        this.userToolchainsFile = userToolchainsFile;

    }

    @Override
    public File getGlobalToolchainsFile() {
        return globalToolchainsFile;
    }

    @Override
    public void setGlobalToolchainsFile(File globalToolchainsFile) {
        this.globalToolchainsFile = globalToolchainsFile;
    }

    @Override
    public void addRemoteRepository(ArtifactRepository repository) {
        for (ArtifactRepository repo : getRemoteRepositories()) {
            if (repo.getId() != null && repo.getId().equals(repository.getId())) {
                return;
            }
        }

        getRemoteRepositories().add(repository);

    }

    @Override
    public List<ArtifactRepository> getRemoteRepositories() {
        if (remoteRepositories == null) {
            remoteRepositories = new ArrayList<>();
        }
        return remoteRepositories;
    }

    @Override
    public void addPluginArtifactRepository(ArtifactRepository repository) {
        for (ArtifactRepository repo : getPluginArtifactRepositories()) {
            if (repo.getId() != null && repo.getId().equals(repository.getId())) {
                return;
            }
        }

        getPluginArtifactRepositories().add(repository);

    }

    @Override
    public List<ArtifactRepository> getPluginArtifactRepositories() {
        if (pluginArtifactRepositories == null) {
            pluginArtifactRepositories = new ArrayList<>();
        }
        return pluginArtifactRepositories;
    }

    // TODO this does not belong here.
    @Override
    public ProjectBuildingRequest getProjectBuildingRequest() {
        if (projectBuildingRequest == null) {
            projectBuildingRequest = new DefaultProjectBuildingRequest();
            projectBuildingRequest.setLocalRepository(getLocalRepository());
            projectBuildingRequest.setSystemProperties(getSystemProperties());
            projectBuildingRequest.setUserProperties(getUserProperties());
            projectBuildingRequest.setRemoteRepositories(getRemoteRepositories());
            projectBuildingRequest.setPluginArtifactRepositories(getPluginArtifactRepositories());
            projectBuildingRequest.setActiveProfileIds(getActiveProfiles());
            projectBuildingRequest.setInactiveProfileIds(getInactiveProfiles());
            projectBuildingRequest.setProfiles(getProfiles());
            projectBuildingRequest.setProcessPlugins(true);
            projectBuildingRequest.setBuildStartTime(getStartTime());
        }

        return projectBuildingRequest;
    }

    @Override
    public void addProfile(Profile profile) {
        Objects.requireNonNull(profile, "profile cannot be null");

        for (Profile p : getProfiles()) {
            if (p.getId() != null && p.getId().equals(profile.getId())) {
                return;
            }
        }

        getProfiles().add(profile);

    }

    @Override
    public RepositoryCache getRepositoryCache() {
        return repositoryCache;
    }

    @Override
    public void setRepositoryCache(RepositoryCache repositoryCache) {
        this.repositoryCache = repositoryCache;

    }

    @Override
    public ExecutionListener getExecutionListener() {
        return executionListener;
    }

    @Override
    public void setExecutionListener(ExecutionListener executionListener) {
        this.executionListener = executionListener;

    }

    @Override
    public void setDegreeOfConcurrency(final int degreeOfConcurrency) {
        this.degreeOfConcurrency = degreeOfConcurrency;
    }

    @Override
    public int getDegreeOfConcurrency() {
        return degreeOfConcurrency;
    }

    @Override
    public WorkspaceReader getWorkspaceReader() {
        return workspaceReader;
    }

    @Override
    public MavenExecutionRequest setWorkspaceReader(WorkspaceReader workspaceReader) {
        this.workspaceReader = workspaceReader;
        return this;
    }

    @Override
    public boolean isCacheTransferError() {
        return cacheTransferError;
    }

    @Override
    public void setCacheTransferError(boolean cacheTransferError) {
        this.cacheTransferError = cacheTransferError;
    }

    @Override
    public boolean isCacheNotFound() {
        return cacheNotFound;
    }

    @Override
    public void setCacheNotFound(boolean cacheNotFound) {
        this.cacheNotFound = cacheNotFound;
    }

    @Override
    public boolean isUseLegacyLocalRepository() {
        return this.useLegacyLocalRepositoryManager;
    }

    @Override
    public void setUseLegacyLocalRepository(boolean useLegacyLocalRepositoryManager) {
        this.useLegacyLocalRepositoryManager = useLegacyLocalRepositoryManager;
    }

    @Override
    public void setBuilderId(String builderId) {
        this.builderId = builderId;
    }

    @Override
    public String getBuilderId() {
        return builderId;
    }

    @Override
    public Map<String, List<ToolchainModel>> getToolchains() {
        if (toolchains == null) {
            toolchains = new HashMap<>();
        }
        return toolchains;
    }

    @Override
    public void setToolchains(Map<String, List<ToolchainModel>> toolchains) {
        this.toolchains = toolchains;
    }

    @Override
    public void setMultiModuleProjectDirectory(File directory) {
        this.multiModuleProjectDirectory = directory;
    }

    @Override
    public File getMultiModuleProjectDirectory() {
        return multiModuleProjectDirectory;
    }

    @Override
    public void setEventSpyDispatcher(EventSpyDispatcher eventSpyDispatcher) {
        this.eventSpyDispatcher = eventSpyDispatcher;
    }

    @Override
    public EventSpyDispatcher getEventSpyDispatcher() {
        return eventSpyDispatcher;
    }

    @Override
    public Map<String, Object> getData() {
        if (data == null) {
            data = new HashMap<>();
        }

        return data;
    }
}

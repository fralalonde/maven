package org.apache.maven.settings.crypto;

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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.building.Problem;
import org.apache.maven.building.Problem.Severity;
import org.apache.maven.settings.Proxy;
import org.apache.maven.settings.Server;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcher;
import org.sonatype.plexus.components.sec.dispatcher.SecDispatcherException;

/**
 * Decrypts passwords in the settings.
 *
 * @author Benjamin Bentmann
 */
@Named
@Singleton
public class DefaultSettingsDecrypter        implements SettingsDecrypter {
    private final SecDispatcher securityDispatcher;

    @Inject
    public DefaultSettingsDecrypter(@Named("maven") SecDispatcher securityDispatcher) {
        this.securityDispatcher = securityDispatcher;
    }

    @Override
    public SettingsDecryptionResult decrypt(SettingsDecryptionRequest request) {
        List<Problem> problems = new ArrayList<>();

        List<Server> servers = new ArrayList<>();

        for (Server server : request.getServers()) {
            server = server.clone();

            servers.add(server);

            try {
                server.setPassword(decrypt(server.getPassword()));
            } catch (SecDispatcherException e) {
                problems.add(new Problem("server: " + server.getId(),  -1, -1, "Failed to decrypt password for server " + server.getId()
                        + ": " + e.getMessage(), e,  Severity.ERROR));
            }

            try {
                server.setPassphrase(decrypt(server.getPassphrase()));
            } catch (SecDispatcherException e) {
                problems.add(new Problem("server: " + server.getId(), -1, -1, "Failed to decrypt passphrase for server " + server.getId()
                        + ": " + e.getMessage(), e, Severity.ERROR));
            }
        }

        List<Proxy> proxies = new ArrayList<>();

        for (Proxy proxy : request.getProxies()) {
            proxy = proxy.clone();

            proxies.add(proxy);

            try {
                proxy.setPassword(decrypt(proxy.getPassword()));
            } catch (SecDispatcherException e) {
                problems.add(new Problem("proxy: " + proxy.getId(), -1, -1, "Failed to decrypt password for proxy " + proxy.getId()
                        + ": " + e.getMessage(), e, Severity.ERROR));
            }
        }

        return new DefaultSettingsDecryptionResult(servers, proxies, problems);
    }

    private String decrypt(String str)
            throws SecDispatcherException {
        return (str == null) ? null : securityDispatcher.decrypt(str);
    }

}

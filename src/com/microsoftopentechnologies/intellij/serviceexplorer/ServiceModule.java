/**
 * Copyright 2014 Microsoft Open Technologies Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.microsoftopentechnologies.intellij.serviceexplorer;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;

public abstract class ServiceModule extends Node {
    public ServiceModule(String id, String name) {
        super(id, name);
    }

    public ServiceModule(
            String id,
            String name,
            Node parent,
            String iconPath,
            boolean hasRefresh,
            Object data) {
        super(id, name, parent, iconPath, data);

        // add the refresh node action
        if(hasRefresh) {
            NodeAction refresh = new NodeAction(this, "Refresh");
            refresh.addListener(new NodeActionListener() {
                @Override
                public void actionPerformed(NodeActionEvent e) {
                    refreshItems();
                }
            });
            addAction(refresh);
        }
    }

    public abstract ListenableFuture<List<Node>> load();

    protected void refreshItems() {
        // we expect this method to be overriden by sub-classes
        throw new UnsupportedOperationException("ServiceModule.refreshItems() must be overridden by subclasses.");
    }
}

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

import java.util.ArrayList;
import java.util.List;

public class NodeAction {
    private String name;
    private List<NodeActionListener> listeners = new ArrayList<NodeActionListener>();
    private Node node; // the node with which this action is associated

    public NodeAction(Node node, String name) {
        this.node = node;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addListener(NodeActionListener listener) {
        listeners.add(listener);
    }

    public List<NodeActionListener> getListeners() {
        return listeners;
    }

    public void fireNodeActionEvent() {
        if(!listeners.isEmpty()) {
            NodeActionEvent event = new NodeActionEvent(this);
            for(NodeActionListener listener : listeners) {
                listener.actionPerformed(event);
            }
        }
    }

    public Node getNode() {
        return node;
    }
}

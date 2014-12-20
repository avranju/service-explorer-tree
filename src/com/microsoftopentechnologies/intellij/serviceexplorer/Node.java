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

import com.microsoftopentechnologies.intellij.helper.collections.ObservableList;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

public class Node {
    private static final String CLICK_ACTION = "click";
    private String id;
    private String name;
    private Node parent;
    private ObservableList<Node> childNodes = new ObservableList<Node>();
    private String iconPath;
    private Object userData;
    private Object viewData;
    private NodeAction clickAction = new NodeAction(this, CLICK_ACTION);
    private List<NodeAction> nodeActions = new ArrayList<NodeAction>();

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public Node(String id, String name) {
        this(id, name, null, null, null);
    }

    public Node(String id, String name, Node parent, String iconPath, Object userData) {
        this.id = id;
        this.name = name;
        this.parent = parent;
        this.iconPath = iconPath;
        this.userData = userData;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String oldValue = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange("name", oldValue, name);
    }

    public Node getParent() {
        return parent;
    }

    public ObservableList<Node> getChildNodes() {
        return childNodes;
    }

    public boolean hasChildNodes() {
        return !childNodes.isEmpty();
    }

    public void removeAllChildNodes() {
        while(!childNodes.isEmpty()) {
            // this remove call should cause the NodeListChangeListener object
            // registered on it's child nodes to fire
            childNodes.remove(0);
        }
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        String oldValue = this.iconPath;
        this.iconPath = iconPath;
        propertyChangeSupport.firePropertyChange("iconPath", oldValue, iconPath);
    }

    public Object getUserData() {
        return userData;
    }

    public void setUserData(Object userData) {
        this.userData = userData;
    }

    public void addChildNode(Node child) {
        childNodes.add(child);
    }

    public void addAction(NodeAction action) {
        nodeActions.add(action);
    }

    public List<NodeAction> getNodeActions() {
        return nodeActions;
    }

    public boolean hasNodeActions() {
        return !nodeActions.isEmpty();
    }

    public void addActions(Iterable<NodeAction> actions) {
        for(NodeAction action : actions) {
            addAction(action);
        }
    }

    public NodeAction getClickAction() {
        return clickAction;
    }

    public void addClickActionListener(NodeActionListener actionListener) {
        clickAction.addListener(actionListener);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    public Object getViewData() {
        return viewData;
    }

    public void setViewData(Object viewData) {
        this.viewData = viewData;
    }
}

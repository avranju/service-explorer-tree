package com.microsoftopentechnologies.intellij.components;

import com.microsoftopentechnologies.intellij.helpers.collections.ListChangeListener;
import com.microsoftopentechnologies.intellij.helpers.collections.ListChangedEvent;
import com.microsoftopentechnologies.intellij.helpers.collections.ObservableList;
import com.microsoftopentechnologies.intellij.serviceexplorer.AzureServiceModule;
import com.microsoftopentechnologies.intellij.serviceexplorer.Node;
import com.microsoftopentechnologies.intellij.serviceexplorer.NodeAction;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.Collection;

public class ServerExplorerToolWindowFactory implements PropertyChangeListener {
    private JTree tree;
    private AzureServiceModule azureServiceModule = new AzureServiceModule();
    private DefaultTreeModel treeModel;

    public void createToolWindowContent(JFrame frame) {
        // initialize with all the service modules
        treeModel = new DefaultTreeModel(initRoot());

        // initialize tree
        tree = new JTree(treeModel);
        tree.setRootVisible(false);
        tree.setCellRenderer(new NodeTreeCellRenderer());
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        // add a click handler for the tree
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                treeMousePressed(e);
            }
        });

        frame.add(new JScrollPane(tree));
    }

    private DefaultMutableTreeNode initRoot() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode();

        // add the azure service root service module
        root.add(createTreeNode(azureServiceModule, null));

        // kick-off asynchronous load of child nodes on all the modules
        azureServiceModule.load();

        return root;
    }

    private void treeMousePressed(MouseEvent e) {
        // get the tree node associated with this mouse click
        TreePath treePath = tree.getPathForLocation(e.getX(), e.getY());
        if(treePath == null)
            return;

        DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)treePath.getLastPathComponent();
        Node node = (Node)treeNode.getUserObject();

        // delegate click to the node's click action if this is a left button click
        if(SwingUtilities.isLeftMouseButton(e)) {
            node.getClickAction().fireNodeActionEvent();
        }
        // for right click show the context menu populated with all the
        // actions from the node
        else if(SwingUtilities.isRightMouseButton(e) || e.isPopupTrigger()) {
            if(node.hasNodeActions()) {
                // select the node which was right-clicked
                tree.getSelectionModel().setSelectionPath(treePath);

                JPopupMenu menu = createPopupMenuForNode(node);
                menu.show(e.getComponent(), e.getX(), e.getY());
            }
        }
    }

    private JPopupMenu createPopupMenuForNode(Node node) {
        JPopupMenu menu = new JPopupMenu();

        for(final NodeAction nodeAction : node.getNodeActions()) {
            JMenuItem menuItem = new JMenuItem(nodeAction.getName());
            menuItem.setIconTextGap(16);

            // delegate the menu item click to the node action's listeners
            menuItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nodeAction.fireNodeActionEvent();
                }
            });

            menu.add(menuItem);
        }

        return menu;
    }

    private DefaultMutableTreeNode createTreeNode(Node node, MutableTreeNode parent) {
        DefaultMutableTreeNode treeNode = new DefaultMutableTreeNode(node, true);

        // associate the DefaultMutableTreeNode with the Node via it's "viewData"
        // property; this allows us to quickly retrieve the DefaultMutableTreeNode
        // object associated with a Node
        node.setViewData(treeNode);

        // listen for property change events on the node
        node.addPropertyChangeListener(this);

        // listen for structure changes on the node, i.e. when child nodes are
        // added or removed
        node.getChildNodes().addChangeListener(new NodeListChangeListener(treeNode));

        // create child tree nodes for each child node
        if(node.hasChildNodes()) {
            for (Node childNode : node.getChildNodes()) {
                treeNode.add(createTreeNode(childNode, treeNode));
            }
        }

        return treeNode;
    }

    private void removeEventHandlers(Node node) {
        node.removePropertyChangeListener(this);

        ObservableList<Node> childNodes = node.getChildNodes();
        childNodes.removeAllChangeListeners();

        if(node.hasChildNodes()) {
            // this remove call should cause the NodeListChangeListener object
            // registered on it's child nodes to fire which should recursively
            // clean up event handlers on it's children
            node.removeAllChildNodes();
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // this event is fired whenever a property on a node in the
        // model changes; we respond by triggering a node change
        // event in the tree's model
        Node node = (Node)evt.getSource();

        // the treeModel object can be null before it is initialized
        // from createToolWindowContent; we ignore property change
        // notifications till we have a valid model object
        if(treeModel != null) {
            treeModel.nodeChanged((TreeNode) node.getViewData());
        }
    }

    private class NodeListChangeListener implements ListChangeListener {
        private DefaultMutableTreeNode treeNode;

        public NodeListChangeListener(DefaultMutableTreeNode treeNode) {
            this.treeNode = treeNode;
        }

        @Override
        public void listChanged(ListChangedEvent e) {
            switch (e.getAction()) {
                case add:
                    // create child tree nodes for the new nodes
                    for(Node childNode : (Collection<Node>)e.getNewItems()) {
                        treeNode.add(createTreeNode(childNode, treeNode));
                    }
                    break;
                case remove:
                    // unregister all event handlers recursively and remove
                    // child nodes from the tree
                    for(Node childNode : (Collection<Node>)e.getOldItems()) {
                        removeEventHandlers(childNode);
                    }
                    break;
            }
        }
    }

    // TODO: Change this to a NodeRenderer when moving to the IntelliJ plugin
    private class NodeTreeCellRenderer extends DefaultTreeCellRenderer {
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            // if the node has an icon set then we use that
            DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode)value;
            Node node = (Node)treeNode.getUserObject();

            // "node" can be null if it's the root node which we keep hidden to simulate
            // a multi-root tree control
            if(node == null) {
                return this;
            }

            String iconPath = node.getIconPath();
            if(iconPath != null && !iconPath.isEmpty()) {
                setIcon(loadIcon(iconPath));
            }

            // setup a tooltip
            setToolTipText(node.getName());

            // setup label text (DefaultTreeCellRenderer inherits from JLabel)
            setText(node.getName());

            return this;
        }

        private ImageIcon loadIcon(String iconPath) {
            URL url = NodeTreeCellRenderer.class.getResource("/com/microsoftopentechnologies/intellij/icons/" + iconPath);
            return new ImageIcon(url);
        }
    }
}

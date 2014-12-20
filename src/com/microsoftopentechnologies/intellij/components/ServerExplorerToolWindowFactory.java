package com.microsoftopentechnologies.intellij.components;

import javax.swing.*;

public class ServerExplorerToolWindowFactory {
    private JTree tree;

    public void createToolWindowContent(JFrame frame) {
        tree = new JTree();
        tree.setRootVisible(false);

        frame.add(new JScrollPane(tree));
    }
}

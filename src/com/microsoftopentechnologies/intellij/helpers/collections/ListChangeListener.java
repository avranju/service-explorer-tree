package com.microsoftopentechnologies.intellij.helpers.collections;

import java.util.EventListener;

public interface ListChangeListener extends EventListener {
    void listChanged(ListChangedEvent e);
}

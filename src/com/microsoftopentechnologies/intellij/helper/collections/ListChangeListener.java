package com.microsoftopentechnologies.intellij.helper.collections;

import java.util.EventListener;

public interface ListChangeListener extends EventListener {
    void listChanged(ListChangedEvent e);
}

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

import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoftopentechnologies.intellij.serviceexplorer.mobileservice.MobileServiceModule;
import com.microsoftopentechnologies.intellij.serviceexplorer.vm.VMServiceModule;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AzureServiceModule extends ServiceModule {
    private static final String AZURE_SERVICE_MODULE_ID = AzureServiceModule.class.getName();
    private static final String ICON_PATH = "azure.png";
    private static final String BASE_MODULE_NAME = "Azure";

    public AzureServiceModule() {
        this(null, ICON_PATH, null);
    }

    public AzureServiceModule(Node parent, String iconPath, Object data) {
        super(AZURE_SERVICE_MODULE_ID, BASE_MODULE_NAME, parent, iconPath, true, data);
    }

    @Override
    protected void refreshItems() {
        // remove all child nodes
        removeAllChildNodes();

        // add the mobile service module
        MobileServiceModule mobileServiceModule = new MobileServiceModule(this);
        addChildNode(mobileServiceModule);
        mobileServiceModule.load();

        // add the VM service module
        VMServiceModule vmServiceModule = new VMServiceModule(this);
        addChildNode(vmServiceModule);
        vmServiceModule.load();
    }

    @Override
    public ListenableFuture<List<Node>> load() {
        final SettableFuture<List<Node>> future = SettableFuture.create();
        setName(BASE_MODULE_NAME + " (loading)...");
        new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshItems();
                future.set(getChildNodes());
                Timer timer = (Timer)e.getSource();
                timer.stop();
                setName(BASE_MODULE_NAME);
            }
        }).start();

        return future;
    }
}

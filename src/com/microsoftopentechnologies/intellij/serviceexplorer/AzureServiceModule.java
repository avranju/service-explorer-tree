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

import java.util.ArrayList;
import java.util.List;

public class AzureServiceModule extends ServiceModule {
    private static final String AZURE_SERVICE_MODULE_ID = AzureServiceModule.class.getName();
    private static final String ICON_PATH = "azure.png";

    public AzureServiceModule() {
        this(null, ICON_PATH, null);
    }

    public AzureServiceModule(Node parent, String iconPath, Object data) {
        super(AZURE_SERVICE_MODULE_ID, "Azure", parent, iconPath, data);
    }

    @Override
    public ListenableFuture<List<Node>> load() {
        return null;
    }
}
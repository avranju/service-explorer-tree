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

package com.microsoftopentechnologies.intellij.serviceexplorer.mobileservice;

import com.google.common.util.concurrent.ListenableFuture;
import com.microsoftopentechnologies.intellij.serviceexplorer.Node;
import com.microsoftopentechnologies.intellij.serviceexplorer.ServiceModule;

import java.util.List;

public class MobileServiceModule extends ServiceModule {
    private static final String MOBILE_SERVICE_MODULE_ID = MobileServiceModule.class.getName();
    private static final String ICON_PATH = "service.png";
    private static final String BASE_MODULE_NAME = "Mobile Services";

    public MobileServiceModule(Node parent) {
        super(MOBILE_SERVICE_MODULE_ID, BASE_MODULE_NAME, parent, ICON_PATH, true, null);
    }

    @Override
    protected void refreshItems() {
        // TODO:
    }

    @Override
    public ListenableFuture<List<Node>> load() {
        return null;
    }
}

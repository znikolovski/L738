/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2019 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package com.adobe.aem.guides.wkndevents.ssr.impl;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface RemoteContentRendererConfigurationFactoryConsumer {

    @Nullable
    public RemoteContentRendererConfigurationFactory getConfiguration(@Nonnull String contentPath);
}

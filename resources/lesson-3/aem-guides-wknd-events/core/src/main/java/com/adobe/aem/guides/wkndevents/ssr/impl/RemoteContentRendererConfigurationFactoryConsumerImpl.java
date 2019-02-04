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

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(immediate = true, service = RemoteContentRendererConfigurationFactoryConsumer.class)
public class RemoteContentRendererConfigurationFactoryConsumerImpl implements RemoteContentRendererConfigurationFactoryConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContentRendererConfigurationFactoryConsumerImpl.class);

    private transient List<RemoteContentRendererConfigurationFactory> configurationList;

    /**
     * Executed on Configuration Add event
     * @param config New configuration for factory
     */
    @Reference(name = "configurationFactory", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected synchronized void bindConfigurationFactory(final RemoteContentRendererConfigurationFactory config) {
        LOGGER.info("bindConfigurationFactory: {0}", config.getContentPathPattern());

        if (configurationList == null) {
            configurationList = new ArrayList<>();
        }

        configurationList.add(config);
    }

    /**
     * Executed on Configuration Remove event
     * @param config New configuration for factory
     */
    protected synchronized void unbindConfigurationFactory(final RemoteContentRendererConfigurationFactory config) {
        LOGGER.info("unbindConfigurationFactory: {0}", config.getContentPathPattern());
        configurationList.remove(config);
    }

    @Nullable
    @Override
    public RemoteContentRendererConfigurationFactory getConfiguration(@Nonnull String contentPath) {
        if (StringUtils.isBlank(contentPath) || configurationList == null) {
            return null;
        }

        for (RemoteContentRendererConfigurationFactory configuration : configurationList) {
            if (contentPath.matches(configuration.getContentPathPattern())) {
                return configuration;
            }
        }

        return null;
    }
}

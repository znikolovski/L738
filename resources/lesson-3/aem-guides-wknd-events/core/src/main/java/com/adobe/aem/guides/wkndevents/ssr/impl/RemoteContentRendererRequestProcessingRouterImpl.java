/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 ~ Copyright 2018 Adobe Systems Incorporated
 ~
 ~ Licensed under the Apache License, Version 2.0 (the "License");
 ~ you may not use this file except in compliance with the License.
 ~ You may obtain a copy of the License at
 ~
 ~     http://www.apache.org/licenses/LICENSE-2.0
 ~
 ~ Unless required by applicable law or agreed to in writing, software
 ~ distributed under the License is distributed on an "AS IS" BASIS,
 ~ WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 ~ See the License for the specific language governing permissions and
 ~ limitations under the License.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
package com.adobe.aem.guides.wkndevents.ssr.impl;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.sling.api.SlingHttpServletRequest;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.ssr.RemoteContentRendererRequestProcessor;

@Component(immediate = true, service = RemoteContentRendererRequestProcessingRouter.class)
public class RemoteContentRendererRequestProcessingRouterImpl implements RemoteContentRendererRequestProcessingRouter {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContentRendererRequestProcessingRouterImpl.class);

    private transient List<RemoteContentRendererRequestProcessor> requestProcessorList;
    
    /**
     * Executed on Configuration Add event
     * @param processor the request processor to bind
     */
    @Reference(name = "configurationFactory", cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
    protected synchronized void bindRemoteContentRendererRequestProcessor(final RemoteContentRendererRequestProcessor processor) {
        LOGGER.info("bindRemoteContentRendererRequestProcessor: {0}", processor);

        if (requestProcessorList == null) {
            requestProcessorList = new ArrayList<>();
        }

        requestProcessorList.add(processor);
    }

    /**
     * Executed on Configuration Remove event
     * @param processor the request processor to unbind
     */
    protected synchronized void unbindRemoteContentRendererRequestProcessor(final RemoteContentRendererRequestProcessor processor) {
        LOGGER.info("unbindRemoteContentRendererRequestProcessor: {0}", processor);
        requestProcessorList.remove(processor);
    }

    @Nullable
    @Override
    public RemoteContentRendererRequestProcessor getRequestProcessor(@Nonnull SlingHttpServletRequest request) {
        if (requestProcessorList == null) {
            return null;
        }

        for (RemoteContentRendererRequestProcessor processor : requestProcessorList) {
            if (processor.use(request)) {
                return processor;
            }
        }

        return null;
    }
}

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


import java.io.IOException;

import javax.annotation.Nonnull;
import javax.servlet.Servlet;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.core.models.HierarchyPage;
import com.adobe.aem.guides.wkndevents.ssr.RemoteContentRendererRequestProcessor;

@Component(
        service = {Servlet.class},
        property = {
                "sling.servlet.resourceTypes=cq/processor/remote/content/renderer",
                "sling.servlet.methods=GET"
        }
)
/**
 * Service responsible for fetching Server-Side pre-rendered content
 */
public class ProcessorRemoteContentRendererServlet extends RemoteContentRendererServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessorRemoteContentRendererServlet.class);

    @Reference
    private transient HttpClientBuilderFactory clientBuilderFactory;

    @Reference
    private transient RemoteContentRendererRequestProcessingRouter requestProcessingRouter;

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws IOException {
        try {
            RemoteContentRendererRequestProcessor processor = requestProcessingRouter.getRequestProcessor(request);

            if (processor == null) {
                LOGGER.warn("No RemoteContentRendererRequestProcessor implementation has been found");
                // obfuscated message
                ServletResponseUtils.writeError(response, "<!-- No request processor has been found, please refer to the logs -->");
                return;
            }

            HierarchyPage rootPage = getRootModel(request);

            if (rootPage == null) {
                return;
            }

            CloseableHttpClient client = clientBuilderFactory.newBuilder()
                    .setDefaultRequestConfig(processor.getRequestConfig(request, response))
                    .build();

            HttpUriRequest remoteRequest = processor.getRequest(request, response, rootPage);

            CloseableHttpResponse remoteResponse = client.execute(remoteRequest);

            int statusCode = remoteResponse.getStatusLine().getStatusCode();

            if (statusCode >= 400) {
                LOGGER.warn("Remote error: code = {0} and message = {1}", statusCode, remoteResponse);
                // obfuscated message
                ServletResponseUtils.writeError(response, "<!-- Remote error: {0} -->", REMOTE_ERROR_MESSAGE);

                return;
            }

            String responseMessage = processor.processResponse(request, response, remoteResponse);

            if (StringUtils.isNotBlank(responseMessage)) {
                response.getWriter().write(responseMessage);
            }
        } catch (IOException | NullPointerException e) {
            LOGGER.warn("Error while attempting to render content server-side: {0}, {1}", e.getMessage(), e);
            // obfuscated message
            ServletResponseUtils.writeError(response, "<!-- error message: %s -->", REQUEST_EXCEPTION_MESSAGE);
        }
    }
}

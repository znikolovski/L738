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
import java.util.Map;

import javax.annotation.Nonnull;
import javax.servlet.Servlet;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.osgi.services.HttpClientBuilderFactory;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.core.models.HierarchyPage;
import com.day.cq.wcm.api.WCMMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(
        service = {Servlet.class},
        property = {
                "sling.servlet.resourceTypes=cq/remote/content/renderer",
                "sling.servlet.methods=GET"
        }
)
/**
 * Service responsible for fetching Server-Side pre-rendered content
 */
public class RemoteContentRendererServlet extends SlingSafeMethodsServlet {

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContentRendererServlet.class);

    static final String REMOTE_ERROR_MESSAGE = "An error on the remote endpoint, refer to the logs for more information";
    static final String REQUEST_EXCEPTION_MESSAGE = "An exception occurred while requesting data from the remote endpoint, refer to the logs for more information";

    @Reference
    private transient HttpClientBuilderFactory clientBuilderFactory;

    @Reference
    private transient RemoteContentRendererConfigurationFactoryConsumer remoteContentRendererConfigurationFactoryConsumer;

    @Override
    protected void doGet(@Nonnull SlingHttpServletRequest request, @Nonnull SlingHttpServletResponse response) throws IOException {
        try {
            RemoteContentRendererConfigurationFactory configuration = remoteContentRendererConfigurationFactoryConsumer.getConfiguration(request.getPathInfo());

            if (configuration == null) {
                LOGGER.warn("No OSGi configuration has been found");
                // obfuscated message
                ServletResponseUtils.writeError(response, "<!-- No configuration has been found -->");
                return;
            }

            final String URL = configuration.getRemoteHTMLRendererUrl() + request.getPathInfo();
            RequestConfig config = RequestConfig.custom().setConnectTimeout(configuration.getRequestTimeout()).build();
            CloseableHttpClient client = clientBuilderFactory.newBuilder()
                    .setDefaultRequestConfig(config)
                    .build();

            ObjectMapper mapper = new ObjectMapper();

            HierarchyPage rootPage = getRootModel(request);

            if (rootPage == null) {
                return;
            }

            StringEntity requestData = new StringEntity(
                    mapper.writeValueAsString(rootPage),
                    ContentType.APPLICATION_JSON);

            HttpPost postMethod = new HttpPost(URL);

            for (Map.Entry<String, String> additionalRequestHeader: configuration.getAdditionalRequestHeaders().entrySet()) {
                postMethod.setHeader(additionalRequestHeader.getKey(), additionalRequestHeader.getValue());
            }

            postMethod.setEntity(requestData);
            postMethod.setHeader("WCM-Mode", WCMMode.fromRequest(request).toString());
            postMethod.setHeader("Page-Model-Root-URL", rootPage.getExportedPath());

            CloseableHttpResponse remoteResponse = client.execute(postMethod);

            String responseMessage = EntityUtils.toString(remoteResponse.getEntity());

            int statusCode = remoteResponse.getStatusLine().getStatusCode();

            if (statusCode >= 400) {
                LOGGER.warn("Remote error: code = {0} and message = {1}", statusCode, remoteResponse);
                // obfuscated message
                ServletResponseUtils.writeError(response, "<!-- Remote error: {0} -->", REMOTE_ERROR_MESSAGE);

                return;
            }

            response.getWriter().write(responseMessage);

        } catch (IOException | NullPointerException e) {
            LOGGER.warn("Error while attempting to render content server-side: {0}, {1}", e.getMessage(), e);
            // obfuscated message
            ServletResponseUtils.writeError(response, "<!-- error message: %s -->", REQUEST_EXCEPTION_MESSAGE);
        }
    }

    /**
     * Returns the page converted into a JSON string
     *
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    HierarchyPage getRootModel(SlingHttpServletRequest request) {
        HierarchyPage page = request.adaptTo(HierarchyPage.class);

        if (page == null) {
            return null;
        }

        return  page.getRootModel();
    }
}

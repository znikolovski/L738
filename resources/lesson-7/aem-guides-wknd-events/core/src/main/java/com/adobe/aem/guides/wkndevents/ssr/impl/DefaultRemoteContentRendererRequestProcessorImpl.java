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

import java.io.IOException;

import javax.annotation.Nonnull;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.ssr.RemoteContentRendererRequestProcessor;
import com.adobe.cq.export.json.hierarchy.HierarchyNodeExporter;
import com.day.cq.wcm.api.WCMMode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component(immediate = true,
        service = RemoteContentRendererRequestProcessor.class,
        property={
            Constants.SERVICE_RANKING +":Integer=10"
        })
public class DefaultRemoteContentRendererRequestProcessorImpl implements RemoteContentRendererRequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRemoteContentRendererRequestProcessorImpl.class);

    private static final String DEFAULT_CONTENT_MATCHER = "/content(.*)";
    private static final String DEFAULT_REMOTE_ENDPOINT_URL = "http://localhost:4200";
    private static final int DEFAULT_REQUEST_TIMEOUT = 10000;

    @Override
    public boolean use(@Nonnull SlingHttpServletRequest servletRequest) {
        return servletRequest.getPathInfo().matches(DEFAULT_CONTENT_MATCHER);
    }

    @Override
    public HttpUriRequest getRequest(@Nonnull SlingHttpServletRequest servletRequest, @Nonnull SlingHttpServletResponse servletResponse, @Nonnull HierarchyNodeExporter hierarchyModel) throws IOException {
        try {
            ObjectMapper mapper = new ObjectMapper();

            final String URL = DEFAULT_REMOTE_ENDPOINT_URL + servletRequest.getPathInfo();

            StringEntity requestData = new StringEntity(
                    mapper.writeValueAsString(hierarchyModel),
                    ContentType.APPLICATION_JSON);

            HttpPost postMethod = new HttpPost(URL);

            postMethod.setEntity(requestData);
            postMethod.setHeader("WCM-Mode", WCMMode.fromRequest(servletRequest).toString());
            postMethod.setHeader("Page-Model-Root-URL", hierarchyModel.getExportedPath());

            return postMethod;
        } catch (JsonProcessingException e) {
            LOGGER.warn("Exception while building the HTTP request: {0}", e);
            // obfuscated message
            ServletResponseUtils.writeError(servletResponse, "<!-- Exception while building the HTTP request: {0} -->", "Please refer to the logs");

            return null;
        }
    }

    @Override
    public RequestConfig getRequestConfig(@Nonnull SlingHttpServletRequest servletRequest, @Nonnull SlingHttpServletResponse servletResponse) {
        return RequestConfig.custom().setConnectTimeout(DEFAULT_REQUEST_TIMEOUT).build();
    }

    @Override
    public String processResponse(@Nonnull SlingHttpServletRequest servletRequest, @Nonnull SlingHttpServletResponse servletResponse, @Nonnull CloseableHttpResponse HTTPResponse) {
        String responseMessage = null;

        try {
            return EntityUtils.toString(HTTPResponse.getEntity());
        } catch (IOException e) {
            LOGGER.warn("Error while processing the response: {0}", e);
            return responseMessage;
        }
    }
}

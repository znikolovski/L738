package com.adobe.aem.guides.wkndevents.ssr.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.AttributeType;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = RemoteContentRendererConfigurationFactory.class)
@Designate(ocd = RemoteContentRendererConfigurationFactoryImpl.Config.class, factory=true)
public class RemoteContentRendererConfigurationFactoryImpl implements RemoteContentRendererConfigurationFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteContentRendererConfigurationFactoryImpl.class);

    private static final String DEFAULT_CONTENT_PATH = "/content";
    private static final String DEFAULT_REMOTE_ENDPOINT_URL = "http://localhost:4200";
    private static final String DEFAULT_REQUEST_TIMEOUT = "10000";

    @ObjectClassDefinition(name = "Remote Content Renderer - Configuration Factory")
    public @interface Config {
        @AttributeDefinition(name = "Content path pattern", description = "Regular expression to match a portion of the content", defaultValue = DEFAULT_CONTENT_PATH)
        String getContentPathPattern();
        @AttributeDefinition(name = "Remote endpoint URL", description = "URL of the endpoint that is responsible for the generating the content", defaultValue = DEFAULT_REMOTE_ENDPOINT_URL)
        String getRemoteHTMLRendererUrl();
        @AttributeDefinition(name = "Additional request headers", description = "Additional headers to be added to the request sent to the remote endpoint. Pattern: key=value", defaultValue = "")
        String[] getAdditionalRequestHeaders();
        @AttributeDefinition(name = "Request timeout", description = "Remote host request timeout in milliseconds", defaultValue = DEFAULT_REQUEST_TIMEOUT, type = AttributeType.INTEGER)
        int getRequestTimeout();
    }

    private String contentPathPattern;
    private String remoteHTMLRendererUrl;
    private Map<String, String> additionalRequestHeaders = new HashMap<>();
    private int requestTimeout;

    @Activate
    @Modified
    protected void activate(final Config config) {
        contentPathPattern = config.getContentPathPattern();
        remoteHTMLRendererUrl = config.getRemoteHTMLRendererUrl();
        requestTimeout = config.getRequestTimeout();

        for (String rawRequestHeaderField: config.getAdditionalRequestHeaders()) {
            String[] requestHeaderArray = rawRequestHeaderField.split("=");
            if (requestHeaderArray.length == 2) {
                additionalRequestHeaders.put(requestHeaderArray[0], requestHeaderArray[1]);
            }
        }

        LOGGER.info("Read the content Consumer Url : {0}", remoteHTMLRendererUrl);
    }

    @Override
    public String getContentPathPattern() {
        return contentPathPattern;
    }

    @Override
    public String getRemoteHTMLRendererUrl() {
        return remoteHTMLRendererUrl;
    }

    @Override
    public Map<String, String> getAdditionalRequestHeaders() {
        return additionalRequestHeaders;
    }

    @Override
    public int getRequestTimeout() {
        return requestTimeout;
    }
}
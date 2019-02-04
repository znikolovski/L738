package com.adobe.aem.guides.wkndevents.core.models.impl;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.core.models.EventListItem;
import com.adobe.aem.guides.wkndevents.core.models.HierarchyPage;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.Page;

public class EventListItemImpl implements EventListItem {

    private static final Logger log = LoggerFactory.getLogger(EventListItemImpl.class);

    protected SlingHttpServletRequest request;
    protected ListItem listItem;
    protected Page currentPage;
    protected Image pageImage;

    public EventListItemImpl(Image pageImage, Page currentPage, @Nonnull ListItem listItem) {
        this.listItem = listItem;
        this.currentPage = currentPage;
        this.pageImage = pageImage;
    }

    public Map<String, Object> getDecoratedProperties() {
        Map<String, Object> exposedProps = new HashMap<String, Object>();
        ValueMap pageProps = currentPage.getProperties();
        for (String propertyName : HierarchyPage.DECORATED_PROPS) {
            if (pageProps.get(propertyName) != null) {
                exposedProps.put(propertyName, pageProps.get(propertyName));
            }

        }
        return exposedProps;
    }

    @Override
    public String getURL() {
        return this.listItem.getURL();
    }

    @Override
    public String getTitle() {
        return this.listItem.getTitle();
    }

    @Override
    public String getDescription() {
        return this.listItem.getDescription();
    }

    @Override
    public Calendar getLastModified() {
        return this.listItem.getLastModified();
    }

    @Override
    public String getPath() {
        return this.listItem.getPath();
    }

    @Override
    public Image getPageImage() {
        return this.pageImage;
    }

    public String getName() {
        return "listitem";
    }

}
package com.adobe.aem.guides.wkndevents.core.models.impl;

import javax.annotation.Nonnull;
import org.apache.sling.api.SlingHttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wkndevents.core.models.ActionItem;
import com.adobe.cq.wcm.core.components.models.ListItem;

public class ActionItemImpl implements ActionItem {

    private static final Logger log = LoggerFactory.getLogger(ActionItemImpl.class);

    protected SlingHttpServletRequest request;
    protected ListItem listItem;

    public ActionItemImpl( @Nonnull ListItem listItem) {
        this.listItem = listItem;
    }

    @Override
    public String getUrl() {
        return this.listItem.getURL();
    }

    @Override
    public String getTitle() {
        return this.listItem.getTitle();
    }

}
package com.adobe.aem.guides.wkndevents.core.models.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Named;

import com.adobe.aem.guides.wkndevents.core.models.ActionItem;
import com.adobe.aem.guides.wkndevents.core.models.Promo;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.adobe.cq.wcm.core.components.models.Teaser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.apache.sling.models.annotations.*;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.apache.sling.models.factory.ModelFactory;

@Model(
    adaptables = SlingHttpServletRequest.class, 
    adapters = {Promo.class, ComponentExporter.class}, 
    resourceType = PromoImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
    )
public class PromoImpl implements Promo {

    static final String RESOURCE_TYPE = "wknd-events/components/content/promo";

    @Self
    private SlingHttpServletRequest request;

    @Self @Via(type = ResourceSuperType.class)
    private Teaser teaser;

    @Inject
    private ModelFactory modelFactory;

    @Override
    public boolean isActionsEnabled() {
        return teaser.isActionsEnabled();
    }

    @Override
    public List<ListItem> getActions() {
        return null;
    }

    public List<ActionItem> getActionItems() {
        List<ActionItem> actionItems = new ArrayList<ActionItem>();
        for(ListItem listItem: teaser.getActions()) {
            ActionItem actionItem = new ActionItemImpl(listItem);
            actionItems.add(actionItem);
        }
        return actionItems;
    }

    @Override
    public String getTitle() {
        return teaser.getTitle();
    }

    @Override
    public Image getImage() {
        Resource imageRes = teaser.getImageResource();
        if(imageRes != null) {
            return modelFactory.getModelFromWrappedRequest(request, imageRes, Image.class);
        }
        return null;
    }

    @Override
    public String getDescription() {
        return teaser.getDescription();
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return PromoImpl.RESOURCE_TYPE;
    }

    @Override
    public String getLinkURL() {
        return teaser.getLinkURL();
    }

    @Override
    public boolean isImageLinkHidden() {
        return teaser.isImageLinkHidden();
    }

    @Override
    public boolean isTitleLinkHidden() {
        return teaser.isTitleLinkHidden();
    }

    @Override
    public String getTitleType() {
        return teaser.getTitleType();
    }

    @JsonIgnore
    @Override
    public Resource getImageResource() {
        return teaser.getImageResource();
    }
}
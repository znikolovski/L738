package com.adobe.aem.guides.wkndevents.core.models.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javax.annotation.Nonnull;
import javax.inject.Inject;

import com.adobe.aem.guides.wkndevents.core.models.EventListItem;
import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.List;
import com.adobe.cq.wcm.core.components.models.ListItem;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.apache.sling.models.factory.ModelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;

@Model(
    adaptables = SlingHttpServletRequest.class, 
    adapters = {List.class, ComponentExporter.class}, 
    resourceType = EventListImpl.RESOURCE_TYPE,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
@Exporter(
    name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, 
    extensions = ExporterConstants.SLING_MODEL_EXTENSION
    )
public class EventListImpl implements List {

    static final String RESOURCE_TYPE = "wknd-events/components/content/eventlist";

    static final String PN_EVENT_DATE = "eventDate";

    private static final Logger log = LoggerFactory.getLogger(EventListImpl.class);

    @Inject
    private ModelFactory modelFactory;

    @ScriptVariable
    private PageManager pageManager;

    @Self
    private SlingHttpServletRequest request;

    @Self @Via(type = ResourceSuperType.class)
    private List list;

    @Nonnull
    @JsonProperty("items")
    public Collection<EventListItem> getEventListItems() {
        java.util.List<EventListItem> eventItems = new ArrayList<>();
        
        Collection<ListItem> listItems = list.getListItems();
        for (ListItem listItem : listItems) {
            EventListItem eventListItem = getEventListItem(listItem);
            if(eventListItem != null) {
                eventItems.add(eventListItem);
            }
        }

        Collections.sort(eventItems, new EventDateComparator());

        return eventItems;
    }

    
    public Collection<ListItem> getListItems() {
        return null;
    }

    private Image getImageFromPage(Page page) {
        Resource imageRes = page.getContentResource().getChild("image");
        if(imageRes != null) {
            return modelFactory.getModelFromWrappedRequest(request, imageRes, Image.class);
        }
        return null;
    }

    private EventListItem getEventListItem(ListItem listItem) {
        if (listItem != null) {
            Page currentPage = pageManager.getPage(listItem.getPath());
            if(currentPage != null && currentPage.getProperties().get(PN_EVENT_DATE) != null) {
                Image pageImage = getImageFromPage(currentPage);
                if(pageImage != null) {
                    return new EventListItemImpl(pageImage, currentPage, listItem);
                }
            }
        }
        return null;
    }

    @Override
    @JsonProperty("linkItems")
    public boolean linkItems() {
        return list.linkItems();
    }

    @Override
    @JsonProperty("showDescription")
    public boolean showDescription() {
        return list.showDescription();
    }

    @Override
    @JsonProperty("showModificationDate")
    public boolean showModificationDate() {
        return list.showModificationDate();
    }

    @Override
    public String getDateFormatString() {
        return list.getDateFormatString();
    }

    @Nonnull
    @Override
    public String getExportedType() {
        return EventListImpl.RESOURCE_TYPE;
    }

    private class EventDateComparator implements Comparator<EventListItem> {
        @Override
        public int compare(final EventListItem e1, final EventListItem e2) {
           Calendar c1 = (Calendar)e1.getDecoratedProperties().get(PN_EVENT_DATE);
           Calendar c2 = (Calendar)e2.getDecoratedProperties().get(PN_EVENT_DATE);
           return c1.compareTo(c2);
        }
    }

}
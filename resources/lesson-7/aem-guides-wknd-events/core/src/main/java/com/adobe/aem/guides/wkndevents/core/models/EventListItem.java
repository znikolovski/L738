package com.adobe.aem.guides.wkndevents.core.models;

import java.util.Map;
import javax.annotation.Nullable;

import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.ListItem;

public interface EventListItem extends ListItem {

    @Nullable
    public Map<String, Object> getDecoratedProperties();

    @Nullable
    public Image getPageImage();

    public String getName();

}
package com.adobe.aem.guides.wkndevents.core.models;

import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.hierarchy.HierarchyNodeExporter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public interface HierarchyPage extends HierarchyNodeExporter, ContainerExporter {
    
    public static final String[] DECORATED_PROPS = {"jcr:title", "jcr:description", "eventDate", "eventCity", "eventAddress", "eventPrice", "eventVenue"};
   
    /**
     * Title of the page. The page title can be the result of multiple fallbacks
     *
     * @return
     */
     @JsonProperty("title")
     public String getTitle();


     /**
     * URL to the root model of the App
     *
     * @return
     */
     @JsonIgnore
     public String getRootUrl();

     @JsonIgnore
     public HierarchyPage getRootModel();

}

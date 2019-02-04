package com.adobe.aem.guides.wkndevents.core.models.impl;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.factory.ModelFactory;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.inject.Inject;
import com.adobe.aem.guides.wkndevents.core.models.AbstractHierarchyPage;
import com.adobe.aem.guides.wkndevents.core.models.HierarchyPage;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.adobe.cq.wcm.core.components.models.Image;

@Model(adaptables = SlingHttpServletRequest.class, adapters = { HierarchyPage.class,
        ContainerExporter.class }, resourceType = HierarchyPageImpl.RESOURCE_TYPE)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class HierarchyPageImpl extends AbstractHierarchyPage implements HierarchyPage {

    /**
     * Resource type of associated with the current implementation
     */
    protected static final String RESOURCE_TYPE = "wknd-events/components/structure/page";

    @Self
    private SlingHttpServletRequest request;

    @Inject
    private ModelFactory modelFactory;

    

    @Nullable
    public Map<String, Object> getDecoratedProperties() {
        Map<String, Object> exposedProps = new HashMap<String, Object>();
        ValueMap pageProps = currentPage.getProperties();
        for(String propertyName : HierarchyPage.DECORATED_PROPS) {
            if(pageProps.get(propertyName) != null) {
                exposedProps.put(propertyName, pageProps.get(propertyName));
            }
            
        }
        return exposedProps;
    }

    @Nullable
    public Image getImage() {
        Resource imageRes = currentPage.getContentResource().getChild("image");
        if(imageRes != null) {
            return modelFactory.getModelFromWrappedRequest(request, imageRes, Image.class);
        }
        return null;
    }
}

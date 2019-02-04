package com.adobe.aem.guides.wkndevents.core.models;

import com.adobe.cq.wcm.core.components.models.Image;
import com.adobe.cq.wcm.core.components.models.Teaser;
import javax.annotation.Nullable;

public interface Promo extends Teaser {

    String PN_OFFER_TEXT = "offerText";

    @Nullable
    default Image getImage() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    default String getOfferText() {
        throw new UnsupportedOperationException();
    }

}
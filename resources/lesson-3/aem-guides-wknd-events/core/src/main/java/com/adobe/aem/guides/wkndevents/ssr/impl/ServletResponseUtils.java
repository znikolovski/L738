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

import org.apache.sling.api.SlingHttpServletResponse;

class ServletResponseUtils {

    private ServletResponseUtils() {}

    static void writeError(SlingHttpServletResponse response, String format, Object... args) throws IOException {
        response.getWriter().write(String.format(format, args));
    }
}

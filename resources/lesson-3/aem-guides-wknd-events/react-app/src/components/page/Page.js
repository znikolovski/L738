/*
    Page.js

    - WKND specific implementation of Page
    - Maps to wknd-events/components/structure/page
*/
import React from "react";
import {Page, MapTo, withComponentMappingContext } from "@adobe/cq-react-editable-components";
import {withRoute} from '../../utils/RouteHelper';
import {EventInfo} from '../events/eventinfo/EventInfo';
import './Page.scss';


 // This component is a variant of a React Page component mapped to the "structure/page" resource type
 // No functionality is changed other than to add an app specific CSS class
 class WkndPage extends Page {
 
     get containerProps() {
         let attrs = super.containerProps;
         attrs.className = (attrs.className || '') + ' WkndPage ' + (this.props.cssClassNames || '');

         if(this.decoratedProps) {
            attrs.className = this.decoratedProps.eventDate ? attrs.className + ' PageEventInfo' : attrs.className;
         }
         
         return attrs;
     }

     get decoratedProps() {
         if(this.props && this.props.decoratedProperties ) {
             return this.props.decoratedProperties;
         }

         return null;
     }

     render() {
        return (
            <div {...this.containerProps}>
                <EventInfo {...this.decoratedProps} />
                { this.childComponents }
                { this.childPages }
            </div>
        )
    }
 }
 
 MapTo('wknd-events/components/structure/page')(withComponentMappingContext(withRoute(WkndPage)));

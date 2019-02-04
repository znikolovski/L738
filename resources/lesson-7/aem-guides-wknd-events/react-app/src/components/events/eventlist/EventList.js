
import React, {Component} from 'react';
import {MapTo} from '@adobe/cq-react-editable-components';
import { Image } from "../../image/Image";
import { Button } from "../../button/Button";
import { EventDate } from "../eventdate/EventDate";

import "./EventList.scss";



const EventListEditConfig = {

    emptyLabel: 'Event List',

    isEmpty: function(props) {
        return !props || !props.items || props.items.length < 1;
    }
};

class EventList extends Component {

    render() {
        if (!EventListEditConfig.isEmpty(this.props)) {
            // Generates the navigation links of the application
            return (
                    <div className="EventList">
                            { this.props.items.map((item, index) => {
                                let align = (index % 2 === 0) ? 'default' : 'reverse';
                                return <EventListItem key={index} 
                                                      image={item.pageImage}
                                                      itemPath={item.path}
                                                      url={item.url}
                                                      align={align}
                                                      {...item.decoratedProperties}
                                                       />
                                })
                            }
                    </div>
            );
        }

        return null;
    }
}

class EventListItem extends Component {

    render() {
        return (<article className={'Event Event--' + this.props.align} key={this.props.itemPath}>
            <div className="Event-column">
                <Image {...this.props.image} />
            </div>    
            <div className="Event-column">
                <div className="EventDetails">
                    <div className="EventDetails-wrapper">
                        <h1 className="EventDetails-title">{this.props['jcr:title']}</h1>
                        <span className="EventDetails-city">@{this.props.eventCity}</span>
                        <div className="EventDetails-description">{this.props['jcr:description']}</div>
                        <Button url={this.props.url}>Details</Button>
                    </div>
                </div>
            </div>
                <EventDate time={this.props.eventDate} />
            </article>);
    }
}

export default MapTo("wknd-events/components/content/eventlist")(EventList, EventListEditConfig);

import React, {Component} from "react";
import {EventDate} from '../eventdate/EventDate';
import './EventInfo.scss';


export class EventInfo extends Component {

    get isReady() {
        if(this.props && this.props.eventDate) {
            return true;
        }

        return false;
    }

    get eventTime() {
        if(this.props.eventDate) {
            let date = new Date(this.props.eventDate);
            return date.getHours() + ":" + date.getMinutes();
        }

        return "";
    }

    render() {
        //const event = new Event(this.props);

        if(this.isReady) {
        return ( 
            <div className="EventInfo">
            <div className="EventInfoWrapper">
                <EventTitle title={this.props['jcr:title']} time={this.props.eventDate} />
                <div className="EventDetails">
                    <EventDetail key={'city'} label={'City'} value={this.props.eventCity} />
                    <EventDetail key={'tickets'} label={'Tickets'} value={this.props.eventPrice} />
                    <EventDetail key={'time'} label={'Time'} value={this.eventTime} />
                    <EventDetail key={'venue'} label={'Venue'} value={this.props.eventVenue} />
                    <EventDetail key={'address'} label={'Address'} value={this.props.eventAddress} />
                </div>
            </div>
          </div>);
        } else {
            return null;
        }


    }
}

class EventTitle extends Component {

    render() {
        return (<div className="EventTitle">
                    <EventDate time={this.props.time} size="M" />
                    <h1 className="EventTitle-text">{ this.props.title }</h1>
                </div>);
    }
}

class EventDetail extends Component {

    get value() {
        const val = this.props.value;

        if(val != null) {
            if(val.constructor === Array) {
                let elementList = val.map(function(element, index){
                    return <li key={index}>{element}</li>
                });
                return (<ul>{elementList}</ul>);
            } else {
                return val;
            }
        }
        return null;

    }

    render() {
        return (<div className="EventDetail">
                    <div className="EventDetail-label">{ this.props.label }</div>
                    <div className="EventDetail-value">{ this.value }</div>
                </div>);
    }
}
import React, { Component } from 'react';
import "./EventDate.scss";

const sizeClasses = {
    "S": "EventDate--small",
    "M": "EventDate--medium",
    "L": "EventDate--large"
}

export class EventDate extends Component {

    constructor(props) {
        super(props);
        this.state = this.getState(this.props.time);
    }

    getState(time) {
        if (this.props.time) {
            let date = new Date(this.props.time);
            return {
                ready: true,
                day: ('0' + date.getDate()).slice(-2),
                month: date.toLocaleString("en-us", { month: "short" }),
                size: this.props.size || "L"
            };
        }

        return {ready : false};
    }

    get eventDateClasses() {
        return `EventDate ${sizeClasses[this.state.size]}`;
    }
    render() {
        if(this.state.ready) {
            return (<div className={ this.eventDateClasses }>
                <div className="EventDate-dateContainer">
                    <div className="EventDate-day">{ this.state.day }</div>
                    <div className="EventDate-month">{ this.state.month }</div>
                </div>
            </div>);
        }
        return null;       
    }
}
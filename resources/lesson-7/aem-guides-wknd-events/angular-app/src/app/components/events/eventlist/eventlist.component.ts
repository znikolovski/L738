import { Component, OnInit, Input } from '@angular/core';
import { MapTo } from '@adobe/cq-angular-editable-components';

@Component({
  selector: 'app-eventlist',
  templateUrl: './eventlist.component.html',
  styleUrls: ['./eventlist.component.scss']
})
export class EventListComponent implements OnInit {

  @Input() items: object[];

  constructor() { }

  get events(): Event[] {
    return this.items.map(item => {
      return new Event(item);
    });
  }

  ngOnInit() { }
}

class Event {
  path: string;
  url: string;
  title: string;
  description: string;
  eventtime: number;
  eventcity: string;
  image: object[];
  

  constructor(data) {
    this.path = data.path;
    this.title = data.title;
    this.description = data.description;
    this.url = data.url;
    this.eventtime = data.decoratedProperties.eventDate;
    this.eventcity = data.decoratedProperties.eventCity;
    this.image = data.pageImage;
  }
}

const EventListEditConfig = {
  emptyLabel: 'Event List',

  isEmpty: function(componentData) {
    return !componentData || !componentData.items || componentData.items.length < 1;
  }
};

MapTo('wknd-events/components/content/eventlist')(EventListComponent, EventListEditConfig);

import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-eventinfo',
  templateUrl: './eventinfo.component.html',
  styleUrls: ['./eventinfo.component.scss']
})
export class EventInfoComponent implements OnInit {

  @Input() eventProps: object[];

  constructor() { }

  get eventTime() {
    if(this.eventProps['eventDate']) {
      let date = new Date(this.eventProps['eventDate']);
      return date.getHours() + ":" + date.getMinutes();
    }

    return "";
  }

  ngOnInit() { }
}
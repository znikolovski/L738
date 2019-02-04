import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-eventdate',
  templateUrl: './eventdate.component.html',
  styleUrls: ['./eventdate.component.scss']
})
export class EventDateComponent implements OnInit {

  @Input() time: number;

  constructor() { }

  get day() {
    if(this.time) {
      let date = new Date(this.time);
      return ('0' + date.getDate()).slice(-2);
    }

    return null;
  }

  get month() {
    if(this.time) {
      let date = new Date(this.time);
      return date.toLocaleString("en-us", { month: "short" });
    }
    return null;
  }

  ngOnInit() { }
}
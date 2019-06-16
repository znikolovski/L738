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
      let date = new Date(this.eventProps['eventDate']),
      hours = date.getHours(),
      minutes = date.getMinutes(),
      ampm = hours >= 12 ? 'pm' : 'am',
      strTime,
      strMinutes;

      hours = hours % 12;
      hours = hours ? hours : 12; // the hour '0' should be '12'
      strMinutes = minutes < 10 ? '0'+minutes : minutes;
      strTime = hours + ':' + strMinutes + ' ' + ampm;
      return strTime;
    }

    return "";
  }


  get eventPrice() {
      if(this.eventProps['eventPrice']) {
          let price = this.eventProps['eventPrice'];
          return price.toLocaleString('en-US', { style: 'currency', currency: 'USD' })
      }
      return "Free";
  }

  ngOnInit() { }
}
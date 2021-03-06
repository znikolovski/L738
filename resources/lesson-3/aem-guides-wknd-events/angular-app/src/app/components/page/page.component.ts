import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ModelManager, Constants } from '@adobe/cq-spa-page-model-manager';
import 'animate.css';

@Component({
  selector: 'app-page',
  templateUrl: './page.component.html',
  styleUrls: ['./page.component.scss']
})
export class PageComponent implements OnInit {
  public path: string;
  public items: object[];
  public itemsOrder: string[];
  public title: string;
  public decoratedProps: object[];

  constructor(private route: ActivatedRoute) {
    // Get the data set by the AemPageDataResolver in the Router
    const path = route.snapshot.data.path;

    // Get the JSON data for the ActivatedRoute's path from ModelManager.
    // If the data exists in the JSON retrieved from ModelManager.initialize() that data will be used.
    // else ModelManager handles retrieving the data from AEM.
    ModelManager.getData(path).then((data) => {
      // Get the data well need to populate the template (which includes an Angular AemPageComponent

      // These 3 values, pulled from the JSON are stored as class variables allowing them to be exposed to
      this.path = data[Constants.PATH_PROP];
      this.items = data[Constants.ITEMS_PROP];
      this.itemsOrder = data[Constants.ITEMS_ORDER_PROP];
      this.title = data['title'];
      this.decoratedProps = data['decoratedProperties'];


      window.scrollTo(0, 0);
    });
  }

  get eventTitle() {
    return this.decoratedProps['jcr:title'] + "!";
  }

  get isEventPage() {
    if(this.decoratedProps && this.decoratedProps['eventDate']) {
      return true;
    }
    return false;
  }

  get cssClass() {
    if(this.isEventPage) {
      return "page-wrapper animated slideInRight";
    }
    return "page-wrapper animated slideInLeft";
  }

  ngOnInit() { }
}

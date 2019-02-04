import {Component, Input, OnInit} from '@angular/core';
import {MapTo} from '@adobe/cq-angular-editable-components';
/**
 * Promo Edit Config
 *
 * @type EditConfig
 */
const PromoEditConfig = {

  emptyLabel: 'Promo',

  isEmpty: function(componentData) {
    return !componentData || !componentData.title;
  }
};

@Component({
  selector: 'app-promo',
  templateUrl: './promo.component.html'
})
export class PromoComponent implements OnInit {
  @Input() title: string;
  @Input() description: string;
  @Input() image: any;
  @Input() actionItems: any;

  constructor() { }

  get hasTitle() {
    return this.title && this.title.trim().length > 0;
  }

  ngOnInit() {
  }

}
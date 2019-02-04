import {Component, Input, OnInit} from '@angular/core';

const sizeClasses = {
  "M" : "Button--M",
  "L" : "Button--L"
}

const variantClasses = {
  "primary": "Button--primary",
  "secondary": "Button--secondary"
}

@Component({
  selector: 'app-button',
  templateUrl: './button.component.html',
  styleUrls: ['./button.component.scss']
})

export class ButtonComponent implements OnInit {

  @Input() size: string;
  @Input() variant: string;
  @Input() url: string;
  @Input() title: string;

  constructor() { }

  get classes() {
    return `Button ` + sizeClasses[this.size] + ` ` + variantClasses[this.variant];
  }

  ngOnInit() {
    this.size = this.size || "L";
    this.variant = this.variant || "primary";
  }

}

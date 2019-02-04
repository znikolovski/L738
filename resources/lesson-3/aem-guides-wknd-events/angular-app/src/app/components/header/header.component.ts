import { ActivatedRoute } from '@angular/router';
import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  public home: boolean;

  constructor(private route: ActivatedRoute) {
      this.home = route.snapshot.data.home;
  }

  ngOnInit() {}

}

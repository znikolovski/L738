import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { SpaAngularEditableComponentsModule } from '@adobe/cq-angular-editable-components';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PageComponent } from './components/page/page.component';
import { TextComponent } from './components/text/text.component';
import { ImageComponent } from './components/image/image.component';
import { HeaderComponent } from './components/header/header.component';
import { ListComponent } from './components/list/list.component';
import { EventInfoComponent } from './components/events/eventinfo/eventinfo.component';
import { EventDateComponent } from './components/events/eventdate/eventdate.component';
import { EventListComponent } from './components/events/eventlist/eventlist.component';
import { PromoComponent } from './components/promo/promo.component';
import { ButtonComponent } from './components/button/button.component';

@NgModule({
  declarations: [
    AppComponent,
    PageComponent,
    TextComponent,
    ImageComponent,
    HeaderComponent,
    ListComponent,
    EventInfoComponent,
    EventDateComponent,
    EventListComponent,
    PromoComponent,
    ButtonComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SpaAngularEditableComponentsModule
  ],
  providers: [],
  bootstrap: [AppComponent],
  entryComponents: [ImageComponent, ListComponent, TextComponent, PromoComponent, EventListComponent]
})
export class AppModule { }

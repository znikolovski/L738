import { NgModule, Injectable } from '@angular/core';
import { Routes, RouterModule, UrlSegment, UrlMatchResult,
         Resolve, ActivatedRouteSnapshot,
         DetachedRouteHandle, RouteReuseStrategy } from '@angular/router';
import { PageComponent} from './components/page/page.component';

const CONTENT_ROOT =  'content/wknd-events/angular/';
const HOME_PAGE =  CONTENT_ROOT + 'home.html';

export function AemPageMatcher ( url: UrlSegment[] ): UrlMatchResult {
  const path = url.join('/');

  if (path.startsWith(CONTENT_ROOT)) {
    return ({
      consumed: url,
      posParams: { path: url[url.length - 1]}
    });
  }
}
@Injectable()
export class AemPageDataResolver implements Resolve<string> {
  constructor() {}

  resolve(route: ActivatedRouteSnapshot) {
    // Returns the absolute resource path with no extension, ex: /content/wknd-events/angular/home/event-1
    return '/' + route.url.join('/').replace(/\.[^/.]+$/, '');
  }
}

@Injectable()
export class AemHomePageDataResolver implements Resolve<boolean> {
  constructor() {}

  resolve(route: ActivatedRouteSnapshot) {
    return route.url.join('/') === HOME_PAGE;
  }
}

export class AemPageRouteReuseStrategy implements RouteReuseStrategy {
    shouldDetach(route: ActivatedRouteSnapshot): boolean { return false; }
    store(route: ActivatedRouteSnapshot, detachedTree: DetachedRouteHandle): void {}
    shouldAttach(route: ActivatedRouteSnapshot): boolean { return false; }
    retrieve(route: ActivatedRouteSnapshot): DetachedRouteHandle|null { return null; }
    shouldReuseRoute(future: ActivatedRouteSnapshot, curr: ActivatedRouteSnapshot): boolean { return false; }
}

const routes: Routes = [
 {
    matcher: AemPageMatcher,
    component: PageComponent,
    resolve: {
      path: AemPageDataResolver,
      home: AemHomePageDataResolver
    }
  },
  {
    path: '',
    redirectTo: HOME_PAGE,
    pathMatch: 'full'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
  providers: [
    AemPageDataResolver,
    AemHomePageDataResolver,
    {
      provide: RouteReuseStrategy,
      useClass: AemPageRouteReuseStrategy
    }
  ]
})
export class AppRoutingModule { }

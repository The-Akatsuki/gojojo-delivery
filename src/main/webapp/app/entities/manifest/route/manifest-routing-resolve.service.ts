import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IManifest, Manifest } from '../manifest.model';
import { ManifestService } from '../service/manifest.service';

@Injectable({ providedIn: 'root' })
export class ManifestRoutingResolveService implements Resolve<IManifest> {
  constructor(protected service: ManifestService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IManifest> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((manifest: HttpResponse<Manifest>) => {
          if (manifest.body) {
            return of(manifest.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Manifest());
  }
}

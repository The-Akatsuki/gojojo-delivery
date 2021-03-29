import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPinCodes, PinCodes } from '../pin-codes.model';
import { PinCodesService } from '../service/pin-codes.service';

@Injectable({ providedIn: 'root' })
export class PinCodesRoutingResolveService implements Resolve<IPinCodes> {
  constructor(protected service: PinCodesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPinCodes> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pinCodes: HttpResponse<PinCodes>) => {
          if (pinCodes.body) {
            return of(pinCodes.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PinCodes());
  }
}

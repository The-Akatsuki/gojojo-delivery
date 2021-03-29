import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReturnReason, ReturnReason } from '../return-reason.model';
import { ReturnReasonService } from '../service/return-reason.service';

@Injectable({ providedIn: 'root' })
export class ReturnReasonRoutingResolveService implements Resolve<IReturnReason> {
  constructor(protected service: ReturnReasonService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReturnReason> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((returnReason: HttpResponse<ReturnReason>) => {
          if (returnReason.body) {
            return of(returnReason.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReturnReason());
  }
}

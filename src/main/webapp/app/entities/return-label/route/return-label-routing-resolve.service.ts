import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReturnLabel, ReturnLabel } from '../return-label.model';
import { ReturnLabelService } from '../service/return-label.service';

@Injectable({ providedIn: 'root' })
export class ReturnLabelRoutingResolveService implements Resolve<IReturnLabel> {
  constructor(protected service: ReturnLabelService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReturnLabel> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((returnLabel: HttpResponse<ReturnLabel>) => {
          if (returnLabel.body) {
            return of(returnLabel.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReturnLabel());
  }
}

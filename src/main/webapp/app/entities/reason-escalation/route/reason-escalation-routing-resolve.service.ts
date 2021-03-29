import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IReasonEscalation, ReasonEscalation } from '../reason-escalation.model';
import { ReasonEscalationService } from '../service/reason-escalation.service';

@Injectable({ providedIn: 'root' })
export class ReasonEscalationRoutingResolveService implements Resolve<IReasonEscalation> {
  constructor(protected service: ReasonEscalationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IReasonEscalation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((reasonEscalation: HttpResponse<ReasonEscalation>) => {
          if (reasonEscalation.body) {
            return of(reasonEscalation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ReasonEscalation());
  }
}

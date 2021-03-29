import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEscalation, Escalation } from '../escalation.model';
import { EscalationService } from '../service/escalation.service';

@Injectable({ providedIn: 'root' })
export class EscalationRoutingResolveService implements Resolve<IEscalation> {
  constructor(protected service: EscalationService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEscalation> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((escalation: HttpResponse<Escalation>) => {
          if (escalation.body) {
            return of(escalation.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Escalation());
  }
}

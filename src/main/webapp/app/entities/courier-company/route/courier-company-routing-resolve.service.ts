import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICourierCompany, CourierCompany } from '../courier-company.model';
import { CourierCompanyService } from '../service/courier-company.service';

@Injectable({ providedIn: 'root' })
export class CourierCompanyRoutingResolveService implements Resolve<ICourierCompany> {
  constructor(protected service: CourierCompanyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICourierCompany> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((courierCompany: HttpResponse<CourierCompany>) => {
          if (courierCompany.body) {
            return of(courierCompany.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new CourierCompany());
  }
}

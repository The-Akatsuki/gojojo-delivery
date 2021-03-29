import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';
import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';

@Injectable({ providedIn: 'root' })
export class OrderBuyerDetailsRoutingResolveService implements Resolve<IOrderBuyerDetails> {
  constructor(protected service: OrderBuyerDetailsService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IOrderBuyerDetails> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((orderBuyerDetails: HttpResponse<OrderBuyerDetails>) => {
          if (orderBuyerDetails.body) {
            return of(orderBuyerDetails.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new OrderBuyerDetails());
  }
}

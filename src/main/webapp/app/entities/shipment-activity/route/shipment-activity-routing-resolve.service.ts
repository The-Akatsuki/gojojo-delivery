import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IShipmentActivity, ShipmentActivity } from '../shipment-activity.model';
import { ShipmentActivityService } from '../service/shipment-activity.service';

@Injectable({ providedIn: 'root' })
export class ShipmentActivityRoutingResolveService implements Resolve<IShipmentActivity> {
  constructor(protected service: ShipmentActivityService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IShipmentActivity> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((shipmentActivity: HttpResponse<ShipmentActivity>) => {
          if (shipmentActivity.body) {
            return of(shipmentActivity.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ShipmentActivity());
  }
}

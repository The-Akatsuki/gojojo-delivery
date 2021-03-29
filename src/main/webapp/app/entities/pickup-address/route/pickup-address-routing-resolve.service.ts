import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IPickupAddress, PickupAddress } from '../pickup-address.model';
import { PickupAddressService } from '../service/pickup-address.service';

@Injectable({ providedIn: 'root' })
export class PickupAddressRoutingResolveService implements Resolve<IPickupAddress> {
  constructor(protected service: PickupAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPickupAddress> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((pickupAddress: HttpResponse<PickupAddress>) => {
          if (pickupAddress.body) {
            return of(pickupAddress.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new PickupAddress());
  }
}

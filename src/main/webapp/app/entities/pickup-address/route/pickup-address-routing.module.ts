import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PickupAddressComponent } from '../list/pickup-address.component';
import { PickupAddressDetailComponent } from '../detail/pickup-address-detail.component';
import { PickupAddressUpdateComponent } from '../update/pickup-address-update.component';
import { PickupAddressRoutingResolveService } from './pickup-address-routing-resolve.service';

const pickupAddressRoute: Routes = [
  {
    path: '',
    component: PickupAddressComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PickupAddressDetailComponent,
    resolve: {
      pickupAddress: PickupAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PickupAddressUpdateComponent,
    resolve: {
      pickupAddress: PickupAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PickupAddressUpdateComponent,
    resolve: {
      pickupAddress: PickupAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pickupAddressRoute)],
  exports: [RouterModule],
})
export class PickupAddressRoutingModule {}

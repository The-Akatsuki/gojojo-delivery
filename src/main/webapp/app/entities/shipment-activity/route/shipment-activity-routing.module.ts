import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ShipmentActivityComponent } from '../list/shipment-activity.component';
import { ShipmentActivityDetailComponent } from '../detail/shipment-activity-detail.component';
import { ShipmentActivityUpdateComponent } from '../update/shipment-activity-update.component';
import { ShipmentActivityRoutingResolveService } from './shipment-activity-routing-resolve.service';

const shipmentActivityRoute: Routes = [
  {
    path: '',
    component: ShipmentActivityComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ShipmentActivityDetailComponent,
    resolve: {
      shipmentActivity: ShipmentActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ShipmentActivityUpdateComponent,
    resolve: {
      shipmentActivity: ShipmentActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ShipmentActivityUpdateComponent,
    resolve: {
      shipmentActivity: ShipmentActivityRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(shipmentActivityRoute)],
  exports: [RouterModule],
})
export class ShipmentActivityRoutingModule {}

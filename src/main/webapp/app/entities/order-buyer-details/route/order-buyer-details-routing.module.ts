import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrderBuyerDetailsComponent } from '../list/order-buyer-details.component';
import { OrderBuyerDetailsDetailComponent } from '../detail/order-buyer-details-detail.component';
import { OrderBuyerDetailsUpdateComponent } from '../update/order-buyer-details-update.component';
import { OrderBuyerDetailsRoutingResolveService } from './order-buyer-details-routing-resolve.service';

const orderBuyerDetailsRoute: Routes = [
  {
    path: '',
    component: OrderBuyerDetailsComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrderBuyerDetailsDetailComponent,
    resolve: {
      orderBuyerDetails: OrderBuyerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrderBuyerDetailsUpdateComponent,
    resolve: {
      orderBuyerDetails: OrderBuyerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrderBuyerDetailsUpdateComponent,
    resolve: {
      orderBuyerDetails: OrderBuyerDetailsRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(orderBuyerDetailsRoute)],
  exports: [RouterModule],
})
export class OrderBuyerDetailsRoutingModule {}

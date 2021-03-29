import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PinCodesComponent } from '../list/pin-codes.component';
import { PinCodesDetailComponent } from '../detail/pin-codes-detail.component';
import { PinCodesUpdateComponent } from '../update/pin-codes-update.component';
import { PinCodesRoutingResolveService } from './pin-codes-routing-resolve.service';

const pinCodesRoute: Routes = [
  {
    path: '',
    component: PinCodesComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PinCodesDetailComponent,
    resolve: {
      pinCodes: PinCodesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PinCodesUpdateComponent,
    resolve: {
      pinCodes: PinCodesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PinCodesUpdateComponent,
    resolve: {
      pinCodes: PinCodesRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(pinCodesRoute)],
  exports: [RouterModule],
})
export class PinCodesRoutingModule {}

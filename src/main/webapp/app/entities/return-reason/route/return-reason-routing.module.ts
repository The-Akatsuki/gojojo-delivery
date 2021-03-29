import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReturnReasonComponent } from '../list/return-reason.component';
import { ReturnReasonDetailComponent } from '../detail/return-reason-detail.component';
import { ReturnReasonUpdateComponent } from '../update/return-reason-update.component';
import { ReturnReasonRoutingResolveService } from './return-reason-routing-resolve.service';

const returnReasonRoute: Routes = [
  {
    path: '',
    component: ReturnReasonComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReturnReasonDetailComponent,
    resolve: {
      returnReason: ReturnReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReturnReasonUpdateComponent,
    resolve: {
      returnReason: ReturnReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReturnReasonUpdateComponent,
    resolve: {
      returnReason: ReturnReasonRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(returnReasonRoute)],
  exports: [RouterModule],
})
export class ReturnReasonRoutingModule {}

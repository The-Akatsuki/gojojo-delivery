import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReturnLabelComponent } from '../list/return-label.component';
import { ReturnLabelDetailComponent } from '../detail/return-label-detail.component';
import { ReturnLabelUpdateComponent } from '../update/return-label-update.component';
import { ReturnLabelRoutingResolveService } from './return-label-routing-resolve.service';

const returnLabelRoute: Routes = [
  {
    path: '',
    component: ReturnLabelComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReturnLabelDetailComponent,
    resolve: {
      returnLabel: ReturnLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReturnLabelUpdateComponent,
    resolve: {
      returnLabel: ReturnLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReturnLabelUpdateComponent,
    resolve: {
      returnLabel: ReturnLabelRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(returnLabelRoute)],
  exports: [RouterModule],
})
export class ReturnLabelRoutingModule {}

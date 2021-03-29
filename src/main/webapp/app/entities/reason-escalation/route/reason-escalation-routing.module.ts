import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ReasonEscalationComponent } from '../list/reason-escalation.component';
import { ReasonEscalationDetailComponent } from '../detail/reason-escalation-detail.component';
import { ReasonEscalationUpdateComponent } from '../update/reason-escalation-update.component';
import { ReasonEscalationRoutingResolveService } from './reason-escalation-routing-resolve.service';

const reasonEscalationRoute: Routes = [
  {
    path: '',
    component: ReasonEscalationComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ReasonEscalationDetailComponent,
    resolve: {
      reasonEscalation: ReasonEscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ReasonEscalationUpdateComponent,
    resolve: {
      reasonEscalation: ReasonEscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ReasonEscalationUpdateComponent,
    resolve: {
      reasonEscalation: ReasonEscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(reasonEscalationRoute)],
  exports: [RouterModule],
})
export class ReasonEscalationRoutingModule {}

import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EscalationComponent } from '../list/escalation.component';
import { EscalationDetailComponent } from '../detail/escalation-detail.component';
import { EscalationUpdateComponent } from '../update/escalation-update.component';
import { EscalationRoutingResolveService } from './escalation-routing-resolve.service';

const escalationRoute: Routes = [
  {
    path: '',
    component: EscalationComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EscalationDetailComponent,
    resolve: {
      escalation: EscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EscalationUpdateComponent,
    resolve: {
      escalation: EscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EscalationUpdateComponent,
    resolve: {
      escalation: EscalationRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(escalationRoute)],
  exports: [RouterModule],
})
export class EscalationRoutingModule {}

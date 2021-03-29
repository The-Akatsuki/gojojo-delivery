import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CourierCompanyComponent } from '../list/courier-company.component';
import { CourierCompanyDetailComponent } from '../detail/courier-company-detail.component';
import { CourierCompanyUpdateComponent } from '../update/courier-company-update.component';
import { CourierCompanyRoutingResolveService } from './courier-company-routing-resolve.service';

const courierCompanyRoute: Routes = [
  {
    path: '',
    component: CourierCompanyComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CourierCompanyDetailComponent,
    resolve: {
      courierCompany: CourierCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CourierCompanyUpdateComponent,
    resolve: {
      courierCompany: CourierCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CourierCompanyUpdateComponent,
    resolve: {
      courierCompany: CourierCompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(courierCompanyRoute)],
  exports: [RouterModule],
})
export class CourierCompanyRoutingModule {}

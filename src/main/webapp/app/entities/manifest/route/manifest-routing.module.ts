import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ManifestComponent } from '../list/manifest.component';
import { ManifestDetailComponent } from '../detail/manifest-detail.component';
import { ManifestUpdateComponent } from '../update/manifest-update.component';
import { ManifestRoutingResolveService } from './manifest-routing-resolve.service';

const manifestRoute: Routes = [
  {
    path: '',
    component: ManifestComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ManifestDetailComponent,
    resolve: {
      manifest: ManifestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ManifestUpdateComponent,
    resolve: {
      manifest: ManifestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ManifestUpdateComponent,
    resolve: {
      manifest: ManifestRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(manifestRoute)],
  exports: [RouterModule],
})
export class ManifestRoutingModule {}

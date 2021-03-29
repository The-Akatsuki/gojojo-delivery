import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ManifestComponent } from './list/manifest.component';
import { ManifestDetailComponent } from './detail/manifest-detail.component';
import { ManifestUpdateComponent } from './update/manifest-update.component';
import { ManifestDeleteDialogComponent } from './delete/manifest-delete-dialog.component';
import { ManifestRoutingModule } from './route/manifest-routing.module';

@NgModule({
  imports: [SharedModule, ManifestRoutingModule],
  declarations: [ManifestComponent, ManifestDetailComponent, ManifestUpdateComponent, ManifestDeleteDialogComponent],
  entryComponents: [ManifestDeleteDialogComponent],
})
export class ManifestModule {}

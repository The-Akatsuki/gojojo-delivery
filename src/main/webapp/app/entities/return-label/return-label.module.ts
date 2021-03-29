import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ReturnLabelComponent } from './list/return-label.component';
import { ReturnLabelDetailComponent } from './detail/return-label-detail.component';
import { ReturnLabelUpdateComponent } from './update/return-label-update.component';
import { ReturnLabelDeleteDialogComponent } from './delete/return-label-delete-dialog.component';
import { ReturnLabelRoutingModule } from './route/return-label-routing.module';

@NgModule({
  imports: [SharedModule, ReturnLabelRoutingModule],
  declarations: [ReturnLabelComponent, ReturnLabelDetailComponent, ReturnLabelUpdateComponent, ReturnLabelDeleteDialogComponent],
  entryComponents: [ReturnLabelDeleteDialogComponent],
})
export class ReturnLabelModule {}

import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ReturnReasonComponent } from './list/return-reason.component';
import { ReturnReasonDetailComponent } from './detail/return-reason-detail.component';
import { ReturnReasonUpdateComponent } from './update/return-reason-update.component';
import { ReturnReasonDeleteDialogComponent } from './delete/return-reason-delete-dialog.component';
import { ReturnReasonRoutingModule } from './route/return-reason-routing.module';

@NgModule({
  imports: [SharedModule, ReturnReasonRoutingModule],
  declarations: [ReturnReasonComponent, ReturnReasonDetailComponent, ReturnReasonUpdateComponent, ReturnReasonDeleteDialogComponent],
  entryComponents: [ReturnReasonDeleteDialogComponent],
})
export class ReturnReasonModule {}

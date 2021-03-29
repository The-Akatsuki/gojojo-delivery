import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PinCodesComponent } from './list/pin-codes.component';
import { PinCodesDetailComponent } from './detail/pin-codes-detail.component';
import { PinCodesUpdateComponent } from './update/pin-codes-update.component';
import { PinCodesDeleteDialogComponent } from './delete/pin-codes-delete-dialog.component';
import { PinCodesRoutingModule } from './route/pin-codes-routing.module';

@NgModule({
  imports: [SharedModule, PinCodesRoutingModule],
  declarations: [PinCodesComponent, PinCodesDetailComponent, PinCodesUpdateComponent, PinCodesDeleteDialogComponent],
  entryComponents: [PinCodesDeleteDialogComponent],
})
export class PinCodesModule {}

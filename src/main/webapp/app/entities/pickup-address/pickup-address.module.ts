import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PickupAddressComponent } from './list/pickup-address.component';
import { PickupAddressDetailComponent } from './detail/pickup-address-detail.component';
import { PickupAddressUpdateComponent } from './update/pickup-address-update.component';
import { PickupAddressDeleteDialogComponent } from './delete/pickup-address-delete-dialog.component';
import { PickupAddressRoutingModule } from './route/pickup-address-routing.module';

@NgModule({
  imports: [SharedModule, PickupAddressRoutingModule],
  declarations: [PickupAddressComponent, PickupAddressDetailComponent, PickupAddressUpdateComponent, PickupAddressDeleteDialogComponent],
  entryComponents: [PickupAddressDeleteDialogComponent],
})
export class PickupAddressModule {}

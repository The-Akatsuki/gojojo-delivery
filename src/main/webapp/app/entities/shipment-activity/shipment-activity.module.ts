import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ShipmentActivityComponent } from './list/shipment-activity.component';
import { ShipmentActivityDetailComponent } from './detail/shipment-activity-detail.component';
import { ShipmentActivityUpdateComponent } from './update/shipment-activity-update.component';
import { ShipmentActivityDeleteDialogComponent } from './delete/shipment-activity-delete-dialog.component';
import { ShipmentActivityRoutingModule } from './route/shipment-activity-routing.module';

@NgModule({
  imports: [SharedModule, ShipmentActivityRoutingModule],
  declarations: [
    ShipmentActivityComponent,
    ShipmentActivityDetailComponent,
    ShipmentActivityUpdateComponent,
    ShipmentActivityDeleteDialogComponent,
  ],
  entryComponents: [ShipmentActivityDeleteDialogComponent],
})
export class ShipmentActivityModule {}

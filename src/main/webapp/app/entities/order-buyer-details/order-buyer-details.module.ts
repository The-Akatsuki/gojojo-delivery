import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { OrderBuyerDetailsComponent } from './list/order-buyer-details.component';
import { OrderBuyerDetailsDetailComponent } from './detail/order-buyer-details-detail.component';
import { OrderBuyerDetailsUpdateComponent } from './update/order-buyer-details-update.component';
import { OrderBuyerDetailsDeleteDialogComponent } from './delete/order-buyer-details-delete-dialog.component';
import { OrderBuyerDetailsRoutingModule } from './route/order-buyer-details-routing.module';

@NgModule({
  imports: [SharedModule, OrderBuyerDetailsRoutingModule],
  declarations: [
    OrderBuyerDetailsComponent,
    OrderBuyerDetailsDetailComponent,
    OrderBuyerDetailsUpdateComponent,
    OrderBuyerDetailsDeleteDialogComponent,
  ],
  entryComponents: [OrderBuyerDetailsDeleteDialogComponent],
})
export class OrderBuyerDetailsModule {}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderBuyerDetails } from '../order-buyer-details.model';
import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';

@Component({
  templateUrl: './order-buyer-details-delete-dialog.component.html',
})
export class OrderBuyerDetailsDeleteDialogComponent {
  orderBuyerDetails?: IOrderBuyerDetails;

  constructor(protected orderBuyerDetailsService: OrderBuyerDetailsService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.orderBuyerDetailsService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

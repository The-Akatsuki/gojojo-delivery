import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

@Component({
  templateUrl: './payment-method-delete-dialog.component.html',
})
export class PaymentMethodDeleteDialogComponent {
  paymentMethod?: IPaymentMethod;

  constructor(protected paymentMethodService: PaymentMethodService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.paymentMethodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

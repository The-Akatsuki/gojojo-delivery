import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';
import { PaymentMethodDeleteDialogComponent } from '../delete/payment-method-delete-dialog.component';

@Component({
  selector: 'jhi-payment-method',
  templateUrl: './payment-method.component.html',
})
export class PaymentMethodComponent implements OnInit {
  paymentMethods?: IPaymentMethod[];
  isLoading = false;

  constructor(protected paymentMethodService: PaymentMethodService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.paymentMethodService.query().subscribe(
      (res: HttpResponse<IPaymentMethod[]>) => {
        this.isLoading = false;
        this.paymentMethods = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPaymentMethod): number {
    return item.id!;
  }

  delete(paymentMethod: IPaymentMethod): void {
    const modalRef = this.modalService.open(PaymentMethodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.paymentMethod = paymentMethod;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

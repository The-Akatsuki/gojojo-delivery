import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IOrderBuyerDetails } from '../order-buyer-details.model';
import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';
import { OrderBuyerDetailsDeleteDialogComponent } from '../delete/order-buyer-details-delete-dialog.component';

@Component({
  selector: 'jhi-order-buyer-details',
  templateUrl: './order-buyer-details.component.html',
})
export class OrderBuyerDetailsComponent implements OnInit {
  orderBuyerDetails?: IOrderBuyerDetails[];
  isLoading = false;

  constructor(protected orderBuyerDetailsService: OrderBuyerDetailsService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.orderBuyerDetailsService.query().subscribe(
      (res: HttpResponse<IOrderBuyerDetails[]>) => {
        this.isLoading = false;
        this.orderBuyerDetails = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IOrderBuyerDetails): number {
    return item.id!;
  }

  delete(orderBuyerDetails: IOrderBuyerDetails): void {
    const modalRef = this.modalService.open(OrderBuyerDetailsDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.orderBuyerDetails = orderBuyerDetails;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

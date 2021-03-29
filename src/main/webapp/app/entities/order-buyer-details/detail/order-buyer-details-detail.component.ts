import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IOrderBuyerDetails } from '../order-buyer-details.model';

@Component({
  selector: 'jhi-order-buyer-details-detail',
  templateUrl: './order-buyer-details-detail.component.html',
})
export class OrderBuyerDetailsDetailComponent implements OnInit {
  orderBuyerDetails: IOrderBuyerDetails | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderBuyerDetails }) => {
      this.orderBuyerDetails = orderBuyerDetails;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

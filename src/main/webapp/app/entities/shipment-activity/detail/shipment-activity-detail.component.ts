import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IShipmentActivity } from '../shipment-activity.model';

@Component({
  selector: 'jhi-shipment-activity-detail',
  templateUrl: './shipment-activity-detail.component.html',
})
export class ShipmentActivityDetailComponent implements OnInit {
  shipmentActivity: IShipmentActivity | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentActivity }) => {
      this.shipmentActivity = shipmentActivity;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

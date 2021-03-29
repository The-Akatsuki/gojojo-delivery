import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPickupAddress } from '../pickup-address.model';

@Component({
  selector: 'jhi-pickup-address-detail',
  templateUrl: './pickup-address-detail.component.html',
})
export class PickupAddressDetailComponent implements OnInit {
  pickupAddress: IPickupAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pickupAddress }) => {
      this.pickupAddress = pickupAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

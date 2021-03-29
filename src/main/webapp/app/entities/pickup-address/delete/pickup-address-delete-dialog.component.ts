import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPickupAddress } from '../pickup-address.model';
import { PickupAddressService } from '../service/pickup-address.service';

@Component({
  templateUrl: './pickup-address-delete-dialog.component.html',
})
export class PickupAddressDeleteDialogComponent {
  pickupAddress?: IPickupAddress;

  constructor(protected pickupAddressService: PickupAddressService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pickupAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

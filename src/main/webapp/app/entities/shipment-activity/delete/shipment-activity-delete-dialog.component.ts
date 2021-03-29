import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IShipmentActivity } from '../shipment-activity.model';
import { ShipmentActivityService } from '../service/shipment-activity.service';

@Component({
  templateUrl: './shipment-activity-delete-dialog.component.html',
})
export class ShipmentActivityDeleteDialogComponent {
  shipmentActivity?: IShipmentActivity;

  constructor(protected shipmentActivityService: ShipmentActivityService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.shipmentActivityService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

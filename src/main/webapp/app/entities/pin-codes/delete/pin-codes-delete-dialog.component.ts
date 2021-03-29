import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPinCodes } from '../pin-codes.model';
import { PinCodesService } from '../service/pin-codes.service';

@Component({
  templateUrl: './pin-codes-delete-dialog.component.html',
})
export class PinCodesDeleteDialogComponent {
  pinCodes?: IPinCodes;

  constructor(protected pinCodesService: PinCodesService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.pinCodesService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

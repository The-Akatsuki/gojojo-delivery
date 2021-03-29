import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReturnLabel } from '../return-label.model';
import { ReturnLabelService } from '../service/return-label.service';

@Component({
  templateUrl: './return-label-delete-dialog.component.html',
})
export class ReturnLabelDeleteDialogComponent {
  returnLabel?: IReturnLabel;

  constructor(protected returnLabelService: ReturnLabelService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.returnLabelService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

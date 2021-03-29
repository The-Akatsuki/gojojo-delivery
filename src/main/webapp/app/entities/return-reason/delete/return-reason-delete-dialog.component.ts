import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReturnReason } from '../return-reason.model';
import { ReturnReasonService } from '../service/return-reason.service';

@Component({
  templateUrl: './return-reason-delete-dialog.component.html',
})
export class ReturnReasonDeleteDialogComponent {
  returnReason?: IReturnReason;

  constructor(protected returnReasonService: ReturnReasonService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.returnReasonService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

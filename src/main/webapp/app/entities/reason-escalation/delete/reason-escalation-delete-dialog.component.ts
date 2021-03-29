import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IReasonEscalation } from '../reason-escalation.model';
import { ReasonEscalationService } from '../service/reason-escalation.service';

@Component({
  templateUrl: './reason-escalation-delete-dialog.component.html',
})
export class ReasonEscalationDeleteDialogComponent {
  reasonEscalation?: IReasonEscalation;

  constructor(protected reasonEscalationService: ReasonEscalationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.reasonEscalationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

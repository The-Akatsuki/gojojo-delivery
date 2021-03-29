import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IEscalation } from '../escalation.model';
import { EscalationService } from '../service/escalation.service';

@Component({
  templateUrl: './escalation-delete-dialog.component.html',
})
export class EscalationDeleteDialogComponent {
  escalation?: IEscalation;

  constructor(protected escalationService: EscalationService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.escalationService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

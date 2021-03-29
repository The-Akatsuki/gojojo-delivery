import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICourierCompany } from '../courier-company.model';
import { CourierCompanyService } from '../service/courier-company.service';

@Component({
  templateUrl: './courier-company-delete-dialog.component.html',
})
export class CourierCompanyDeleteDialogComponent {
  courierCompany?: ICourierCompany;

  constructor(protected courierCompanyService: CourierCompanyService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.courierCompanyService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

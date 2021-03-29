import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IManifest } from '../manifest.model';
import { ManifestService } from '../service/manifest.service';

@Component({
  templateUrl: './manifest-delete-dialog.component.html',
})
export class ManifestDeleteDialogComponent {
  manifest?: IManifest;

  constructor(protected manifestService: ManifestService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.manifestService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}

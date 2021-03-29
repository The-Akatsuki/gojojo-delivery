import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IManifest } from '../manifest.model';
import { ManifestService } from '../service/manifest.service';
import { ManifestDeleteDialogComponent } from '../delete/manifest-delete-dialog.component';

@Component({
  selector: 'jhi-manifest',
  templateUrl: './manifest.component.html',
})
export class ManifestComponent implements OnInit {
  manifests?: IManifest[];
  isLoading = false;

  constructor(protected manifestService: ManifestService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.manifestService.query().subscribe(
      (res: HttpResponse<IManifest[]>) => {
        this.isLoading = false;
        this.manifests = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IManifest): number {
    return item.id!;
  }

  delete(manifest: IManifest): void {
    const modalRef = this.modalService.open(ManifestDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.manifest = manifest;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

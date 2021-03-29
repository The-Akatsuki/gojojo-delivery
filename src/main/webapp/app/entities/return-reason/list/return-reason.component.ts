import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IReturnReason } from '../return-reason.model';
import { ReturnReasonService } from '../service/return-reason.service';
import { ReturnReasonDeleteDialogComponent } from '../delete/return-reason-delete-dialog.component';

@Component({
  selector: 'jhi-return-reason',
  templateUrl: './return-reason.component.html',
})
export class ReturnReasonComponent implements OnInit {
  returnReasons?: IReturnReason[];
  isLoading = false;

  constructor(protected returnReasonService: ReturnReasonService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.returnReasonService.query().subscribe(
      (res: HttpResponse<IReturnReason[]>) => {
        this.isLoading = false;
        this.returnReasons = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IReturnReason): number {
    return item.id!;
  }

  delete(returnReason: IReturnReason): void {
    const modalRef = this.modalService.open(ReturnReasonDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.returnReason = returnReason;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IEscalation } from '../escalation.model';
import { EscalationService } from '../service/escalation.service';
import { EscalationDeleteDialogComponent } from '../delete/escalation-delete-dialog.component';

@Component({
  selector: 'jhi-escalation',
  templateUrl: './escalation.component.html',
})
export class EscalationComponent implements OnInit {
  escalations?: IEscalation[];
  isLoading = false;

  constructor(protected escalationService: EscalationService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.escalationService.query().subscribe(
      (res: HttpResponse<IEscalation[]>) => {
        this.isLoading = false;
        this.escalations = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IEscalation): number {
    return item.id!;
  }

  delete(escalation: IEscalation): void {
    const modalRef = this.modalService.open(EscalationDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.escalation = escalation;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}

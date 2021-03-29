import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IEscalation, Escalation } from '../escalation.model';
import { EscalationService } from '../service/escalation.service';
import { IReasonEscalation } from 'app/entities/reason-escalation/reason-escalation.model';
import { ReasonEscalationService } from 'app/entities/reason-escalation/service/reason-escalation.service';

@Component({
  selector: 'jhi-escalation-update',
  templateUrl: './escalation-update.component.html',
})
export class EscalationUpdateComponent implements OnInit {
  isSaving = false;

  reasonEscalationsSharedCollection: IReasonEscalation[] = [];

  editForm = this.fb.group({
    id: [],
    remark: [],
    reason: [],
  });

  constructor(
    protected escalationService: EscalationService,
    protected reasonEscalationService: ReasonEscalationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ escalation }) => {
      this.updateForm(escalation);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const escalation = this.createFromForm();
    if (escalation.id !== undefined) {
      this.subscribeToSaveResponse(this.escalationService.update(escalation));
    } else {
      this.subscribeToSaveResponse(this.escalationService.create(escalation));
    }
  }

  trackReasonEscalationById(index: number, item: IReasonEscalation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IEscalation>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(escalation: IEscalation): void {
    this.editForm.patchValue({
      id: escalation.id,
      remark: escalation.remark,
      reason: escalation.reason,
    });

    this.reasonEscalationsSharedCollection = this.reasonEscalationService.addReasonEscalationToCollectionIfMissing(
      this.reasonEscalationsSharedCollection,
      escalation.reason
    );
  }

  protected loadRelationshipsOptions(): void {
    this.reasonEscalationService
      .query()
      .pipe(map((res: HttpResponse<IReasonEscalation[]>) => res.body ?? []))
      .pipe(
        map((reasonEscalations: IReasonEscalation[]) =>
          this.reasonEscalationService.addReasonEscalationToCollectionIfMissing(reasonEscalations, this.editForm.get('reason')!.value)
        )
      )
      .subscribe((reasonEscalations: IReasonEscalation[]) => (this.reasonEscalationsSharedCollection = reasonEscalations));
  }

  protected createFromForm(): IEscalation {
    return {
      ...new Escalation(),
      id: this.editForm.get(['id'])!.value,
      remark: this.editForm.get(['remark'])!.value,
      reason: this.editForm.get(['reason'])!.value,
    };
  }
}

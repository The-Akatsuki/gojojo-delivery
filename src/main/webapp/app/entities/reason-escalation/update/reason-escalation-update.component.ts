import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IReasonEscalation, ReasonEscalation } from '../reason-escalation.model';
import { ReasonEscalationService } from '../service/reason-escalation.service';

@Component({
  selector: 'jhi-reason-escalation-update',
  templateUrl: './reason-escalation-update.component.html',
})
export class ReasonEscalationUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
  });

  constructor(
    protected reasonEscalationService: ReasonEscalationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reasonEscalation }) => {
      this.updateForm(reasonEscalation);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const reasonEscalation = this.createFromForm();
    if (reasonEscalation.id !== undefined) {
      this.subscribeToSaveResponse(this.reasonEscalationService.update(reasonEscalation));
    } else {
      this.subscribeToSaveResponse(this.reasonEscalationService.create(reasonEscalation));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReasonEscalation>>): void {
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

  protected updateForm(reasonEscalation: IReasonEscalation): void {
    this.editForm.patchValue({
      id: reasonEscalation.id,
      title: reasonEscalation.title,
    });
  }

  protected createFromForm(): IReasonEscalation {
    return {
      ...new ReasonEscalation(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
    };
  }
}

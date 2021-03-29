import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IReturnLabel, ReturnLabel } from '../return-label.model';
import { ReturnLabelService } from '../service/return-label.service';

@Component({
  selector: 'jhi-return-label-update',
  templateUrl: './return-label-update.component.html',
})
export class ReturnLabelUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    title: [],
  });

  constructor(protected returnLabelService: ReturnLabelService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnLabel }) => {
      this.updateForm(returnLabel);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const returnLabel = this.createFromForm();
    if (returnLabel.id !== undefined) {
      this.subscribeToSaveResponse(this.returnLabelService.update(returnLabel));
    } else {
      this.subscribeToSaveResponse(this.returnLabelService.create(returnLabel));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnLabel>>): void {
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

  protected updateForm(returnLabel: IReturnLabel): void {
    this.editForm.patchValue({
      id: returnLabel.id,
      title: returnLabel.title,
    });
  }

  protected createFromForm(): IReturnLabel {
    return {
      ...new ReturnLabel(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
    };
  }
}

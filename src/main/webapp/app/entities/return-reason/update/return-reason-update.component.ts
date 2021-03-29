import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IReturnReason, ReturnReason } from '../return-reason.model';
import { ReturnReasonService } from '../service/return-reason.service';
import { IReturnLabel } from 'app/entities/return-label/return-label.model';
import { ReturnLabelService } from 'app/entities/return-label/service/return-label.service';

@Component({
  selector: 'jhi-return-reason-update',
  templateUrl: './return-reason-update.component.html',
})
export class ReturnReasonUpdateComponent implements OnInit {
  isSaving = false;

  returnLabelsSharedCollection: IReturnLabel[] = [];

  editForm = this.fb.group({
    id: [],
    comment: [],
    image: [],
    label: [],
  });

  constructor(
    protected returnReasonService: ReturnReasonService,
    protected returnLabelService: ReturnLabelService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnReason }) => {
      this.updateForm(returnReason);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const returnReason = this.createFromForm();
    if (returnReason.id !== undefined) {
      this.subscribeToSaveResponse(this.returnReasonService.update(returnReason));
    } else {
      this.subscribeToSaveResponse(this.returnReasonService.create(returnReason));
    }
  }

  trackReturnLabelById(index: number, item: IReturnLabel): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IReturnReason>>): void {
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

  protected updateForm(returnReason: IReturnReason): void {
    this.editForm.patchValue({
      id: returnReason.id,
      comment: returnReason.comment,
      image: returnReason.image,
      label: returnReason.label,
    });

    this.returnLabelsSharedCollection = this.returnLabelService.addReturnLabelToCollectionIfMissing(
      this.returnLabelsSharedCollection,
      returnReason.label
    );
  }

  protected loadRelationshipsOptions(): void {
    this.returnLabelService
      .query()
      .pipe(map((res: HttpResponse<IReturnLabel[]>) => res.body ?? []))
      .pipe(
        map((returnLabels: IReturnLabel[]) =>
          this.returnLabelService.addReturnLabelToCollectionIfMissing(returnLabels, this.editForm.get('label')!.value)
        )
      )
      .subscribe((returnLabels: IReturnLabel[]) => (this.returnLabelsSharedCollection = returnLabels));
  }

  protected createFromForm(): IReturnReason {
    return {
      ...new ReturnReason(),
      id: this.editForm.get(['id'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      image: this.editForm.get(['image'])!.value,
      label: this.editForm.get(['label'])!.value,
    };
  }
}

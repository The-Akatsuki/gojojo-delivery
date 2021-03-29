import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPinCodes, PinCodes } from '../pin-codes.model';
import { PinCodesService } from '../service/pin-codes.service';

@Component({
  selector: 'jhi-pin-codes-update',
  templateUrl: './pin-codes-update.component.html',
})
export class PinCodesUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    pin: [],
    city: [],
    state: [],
    country: [],
  });

  constructor(protected pinCodesService: PinCodesService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pinCodes }) => {
      this.updateForm(pinCodes);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pinCodes = this.createFromForm();
    if (pinCodes.id !== undefined) {
      this.subscribeToSaveResponse(this.pinCodesService.update(pinCodes));
    } else {
      this.subscribeToSaveResponse(this.pinCodesService.create(pinCodes));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPinCodes>>): void {
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

  protected updateForm(pinCodes: IPinCodes): void {
    this.editForm.patchValue({
      id: pinCodes.id,
      pin: pinCodes.pin,
      city: pinCodes.city,
      state: pinCodes.state,
      country: pinCodes.country,
    });
  }

  protected createFromForm(): IPinCodes {
    return {
      ...new PinCodes(),
      id: this.editForm.get(['id'])!.value,
      pin: this.editForm.get(['pin'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}

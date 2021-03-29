import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ICourierCompany, CourierCompany } from '../courier-company.model';
import { CourierCompanyService } from '../service/courier-company.service';

@Component({
  selector: 'jhi-courier-company-update',
  templateUrl: './courier-company-update.component.html',
})
export class CourierCompanyUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [],
    mobile: [],
    alternateMobile: [],
    email: [],
    addressline1: [],
    addressline2: [],
    pincode: [],
    city: [],
    state: [],
    country: [],
    image: [],
  });

  constructor(
    protected courierCompanyService: CourierCompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courierCompany }) => {
      this.updateForm(courierCompany);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const courierCompany = this.createFromForm();
    if (courierCompany.id !== undefined) {
      this.subscribeToSaveResponse(this.courierCompanyService.update(courierCompany));
    } else {
      this.subscribeToSaveResponse(this.courierCompanyService.create(courierCompany));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICourierCompany>>): void {
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

  protected updateForm(courierCompany: ICourierCompany): void {
    this.editForm.patchValue({
      id: courierCompany.id,
      name: courierCompany.name,
      mobile: courierCompany.mobile,
      alternateMobile: courierCompany.alternateMobile,
      email: courierCompany.email,
      addressline1: courierCompany.addressline1,
      addressline2: courierCompany.addressline2,
      pincode: courierCompany.pincode,
      city: courierCompany.city,
      state: courierCompany.state,
      country: courierCompany.country,
      image: courierCompany.image,
    });
  }

  protected createFromForm(): ICourierCompany {
    return {
      ...new CourierCompany(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      alternateMobile: this.editForm.get(['alternateMobile'])!.value,
      email: this.editForm.get(['email'])!.value,
      addressline1: this.editForm.get(['addressline1'])!.value,
      addressline2: this.editForm.get(['addressline2'])!.value,
      pincode: this.editForm.get(['pincode'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
      image: this.editForm.get(['image'])!.value,
    };
  }
}

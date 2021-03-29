import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPickupAddress, PickupAddress } from '../pickup-address.model';
import { PickupAddressService } from '../service/pickup-address.service';

@Component({
  selector: 'jhi-pickup-address-update',
  templateUrl: './pickup-address-update.component.html',
})
export class PickupAddressUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    nickName: [],
    mobile: [],
    alternateMobile: [],
    email: [],
    addressline1: [],
    addressline2: [],
    pincode: [],
    city: [],
    state: [],
    country: [],
    companyName: [],
    isMobileVerified: [],
    otp: [],
  });

  constructor(protected pickupAddressService: PickupAddressService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pickupAddress }) => {
      this.updateForm(pickupAddress);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const pickupAddress = this.createFromForm();
    if (pickupAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.pickupAddressService.update(pickupAddress));
    } else {
      this.subscribeToSaveResponse(this.pickupAddressService.create(pickupAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPickupAddress>>): void {
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

  protected updateForm(pickupAddress: IPickupAddress): void {
    this.editForm.patchValue({
      id: pickupAddress.id,
      nickName: pickupAddress.nickName,
      mobile: pickupAddress.mobile,
      alternateMobile: pickupAddress.alternateMobile,
      email: pickupAddress.email,
      addressline1: pickupAddress.addressline1,
      addressline2: pickupAddress.addressline2,
      pincode: pickupAddress.pincode,
      city: pickupAddress.city,
      state: pickupAddress.state,
      country: pickupAddress.country,
      companyName: pickupAddress.companyName,
      isMobileVerified: pickupAddress.isMobileVerified,
      otp: pickupAddress.otp,
    });
  }

  protected createFromForm(): IPickupAddress {
    return {
      ...new PickupAddress(),
      id: this.editForm.get(['id'])!.value,
      nickName: this.editForm.get(['nickName'])!.value,
      mobile: this.editForm.get(['mobile'])!.value,
      alternateMobile: this.editForm.get(['alternateMobile'])!.value,
      email: this.editForm.get(['email'])!.value,
      addressline1: this.editForm.get(['addressline1'])!.value,
      addressline2: this.editForm.get(['addressline2'])!.value,
      pincode: this.editForm.get(['pincode'])!.value,
      city: this.editForm.get(['city'])!.value,
      state: this.editForm.get(['state'])!.value,
      country: this.editForm.get(['country'])!.value,
      companyName: this.editForm.get(['companyName'])!.value,
      isMobileVerified: this.editForm.get(['isMobileVerified'])!.value,
      otp: this.editForm.get(['otp'])!.value,
    };
  }
}

import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';
import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

@Component({
  selector: 'jhi-order-buyer-details-update',
  templateUrl: './order-buyer-details-update.component.html',
})
export class OrderBuyerDetailsUpdateComponent implements OnInit {
  isSaving = false;

  ordersCollection: IOrder[] = [];

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
    companyName: [],
    isBillingSame: [],
    billAddressline1: [],
    billAddressline2: [],
    billPincode: [],
    billCity: [],
    billState: [],
    billCountry: [],
    order: [],
  });

  constructor(
    protected orderBuyerDetailsService: OrderBuyerDetailsService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orderBuyerDetails }) => {
      this.updateForm(orderBuyerDetails);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orderBuyerDetails = this.createFromForm();
    if (orderBuyerDetails.id !== undefined) {
      this.subscribeToSaveResponse(this.orderBuyerDetailsService.update(orderBuyerDetails));
    } else {
      this.subscribeToSaveResponse(this.orderBuyerDetailsService.create(orderBuyerDetails));
    }
  }

  trackOrderById(index: number, item: IOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrderBuyerDetails>>): void {
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

  protected updateForm(orderBuyerDetails: IOrderBuyerDetails): void {
    this.editForm.patchValue({
      id: orderBuyerDetails.id,
      name: orderBuyerDetails.name,
      mobile: orderBuyerDetails.mobile,
      alternateMobile: orderBuyerDetails.alternateMobile,
      email: orderBuyerDetails.email,
      addressline1: orderBuyerDetails.addressline1,
      addressline2: orderBuyerDetails.addressline2,
      pincode: orderBuyerDetails.pincode,
      city: orderBuyerDetails.city,
      state: orderBuyerDetails.state,
      country: orderBuyerDetails.country,
      companyName: orderBuyerDetails.companyName,
      isBillingSame: orderBuyerDetails.isBillingSame,
      billAddressline1: orderBuyerDetails.billAddressline1,
      billAddressline2: orderBuyerDetails.billAddressline2,
      billPincode: orderBuyerDetails.billPincode,
      billCity: orderBuyerDetails.billCity,
      billState: orderBuyerDetails.billState,
      billCountry: orderBuyerDetails.billCountry,
      order: orderBuyerDetails.order,
    });

    this.ordersCollection = this.orderService.addOrderToCollectionIfMissing(this.ordersCollection, orderBuyerDetails.order);
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query({ filter: 'buyerdetails-is-null' })
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing(orders, this.editForm.get('order')!.value)))
      .subscribe((orders: IOrder[]) => (this.ordersCollection = orders));
  }

  protected createFromForm(): IOrderBuyerDetails {
    return {
      ...new OrderBuyerDetails(),
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
      companyName: this.editForm.get(['companyName'])!.value,
      isBillingSame: this.editForm.get(['isBillingSame'])!.value,
      billAddressline1: this.editForm.get(['billAddressline1'])!.value,
      billAddressline2: this.editForm.get(['billAddressline2'])!.value,
      billPincode: this.editForm.get(['billPincode'])!.value,
      billCity: this.editForm.get(['billCity'])!.value,
      billState: this.editForm.get(['billState'])!.value,
      billCountry: this.editForm.get(['billCountry'])!.value,
      order: this.editForm.get(['order'])!.value,
    };
  }
}

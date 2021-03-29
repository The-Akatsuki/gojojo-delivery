import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IShipmentActivity, ShipmentActivity } from '../shipment-activity.model';
import { ShipmentActivityService } from '../service/shipment-activity.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

@Component({
  selector: 'jhi-shipment-activity-update',
  templateUrl: './shipment-activity-update.component.html',
})
export class ShipmentActivityUpdateComponent implements OnInit {
  isSaving = false;

  ordersSharedCollection: IOrder[] = [];

  editForm = this.fb.group({
    id: [],
    status: [],
    pincode: [],
    location: [],
    order: [],
  });

  constructor(
    protected shipmentActivityService: ShipmentActivityService,
    protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ shipmentActivity }) => {
      this.updateForm(shipmentActivity);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const shipmentActivity = this.createFromForm();
    if (shipmentActivity.id !== undefined) {
      this.subscribeToSaveResponse(this.shipmentActivityService.update(shipmentActivity));
    } else {
      this.subscribeToSaveResponse(this.shipmentActivityService.create(shipmentActivity));
    }
  }

  trackOrderById(index: number, item: IOrder): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IShipmentActivity>>): void {
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

  protected updateForm(shipmentActivity: IShipmentActivity): void {
    this.editForm.patchValue({
      id: shipmentActivity.id,
      status: shipmentActivity.status,
      pincode: shipmentActivity.pincode,
      location: shipmentActivity.location,
      order: shipmentActivity.order,
    });

    this.ordersSharedCollection = this.orderService.addOrderToCollectionIfMissing(this.ordersSharedCollection, shipmentActivity.order);
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query()
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing(orders, this.editForm.get('order')!.value)))
      .subscribe((orders: IOrder[]) => (this.ordersSharedCollection = orders));
  }

  protected createFromForm(): IShipmentActivity {
    return {
      ...new ShipmentActivity(),
      id: this.editForm.get(['id'])!.value,
      status: this.editForm.get(['status'])!.value,
      pincode: this.editForm.get(['pincode'])!.value,
      location: this.editForm.get(['location'])!.value,
      order: this.editForm.get(['order'])!.value,
    };
  }
}

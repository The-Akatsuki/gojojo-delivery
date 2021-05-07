import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IManifest, Manifest } from '../manifest.model';
import { ManifestService } from '../service/manifest.service';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import { ICourierCompany } from 'app/entities/courier-company/courier-company.model';
import { CourierCompanyService } from 'app/entities/courier-company/service/courier-company.service';
import { IEscalation } from 'app/entities/escalation/escalation.model';
import { EscalationService } from 'app/entities/escalation/service/escalation.service';

@Component({
  selector: 'jhi-manifest-update',
  templateUrl: './manifest-update.component.html',
})
export class ManifestUpdateComponent implements OnInit {
  isSaving = false;

  ordersCollection: IOrder[] = [];
  courierCompaniesSharedCollection: ICourierCompany[] = [];
  escalationsSharedCollection: IEscalation[] = [];

  editForm = this.fb.group({
    id: [],
    manifestId: [],
    awb: [],
    awbAssignedDate: [],
    pickupException: [],
    remarks: [],
    pickupReferenceNumber: [],
    status: [],
    order: [],
    courier: [],
    escalation: [],
  });

  constructor(
    protected manifestService: ManifestService,
    protected orderService: OrderService,
    protected courierCompanyService: CourierCompanyService,
    protected escalationService: EscalationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manifest }) => {
      if (manifest.id === undefined) {
        const today = dayjs().startOf('day');
        manifest.awbAssignedDate = today;
      }

      this.updateForm(manifest);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const manifest = this.createFromForm();
    if (manifest.id !== undefined) {
      this.subscribeToSaveResponse(this.manifestService.update(manifest));
    } else {
      this.subscribeToSaveResponse(this.manifestService.create(manifest));
    }
  }

  trackOrderById(index: number, item: IOrder): number {
    return item.id!;
  }

  trackCourierCompanyById(index: number, item: ICourierCompany): number {
    return item.id!;
  }

  trackEscalationById(index: number, item: IEscalation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IManifest>>): void {
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

  protected updateForm(manifest: IManifest): void {
    this.editForm.patchValue({
      id: manifest.id,
      manifestId: manifest.manifestId,
      awb: manifest.awb,
      awbAssignedDate: manifest.awbAssignedDate ? manifest.awbAssignedDate.format(DATE_TIME_FORMAT) : null,
      pickupException: manifest.pickupException,
      remarks: manifest.remarks,
      pickupReferenceNumber: manifest.pickupReferenceNumber,
      status: manifest.status,
      order: manifest.order,
      courier: manifest.courier,
      escalation: manifest.escalation,
    });

    this.ordersCollection = this.orderService.addOrderToCollectionIfMissing(this.ordersCollection, manifest.order);
    this.courierCompaniesSharedCollection = this.courierCompanyService.addCourierCompanyToCollectionIfMissing(
      this.courierCompaniesSharedCollection,
      manifest.courier
    );
    this.escalationsSharedCollection = this.escalationService.addEscalationToCollectionIfMissing(
      this.escalationsSharedCollection,
      manifest.escalation
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orderService
      .query({ filter: 'manifest-is-null' })
      .pipe(map((res: HttpResponse<IOrder[]>) => res.body ?? []))
      .pipe(map((orders: IOrder[]) => this.orderService.addOrderToCollectionIfMissing(orders, this.editForm.get('order')!.value)))
      .subscribe((orders: IOrder[]) => (this.ordersCollection = orders));

    this.courierCompanyService
      .query()
      .pipe(map((res: HttpResponse<ICourierCompany[]>) => res.body ?? []))
      .pipe(
        map((courierCompanies: ICourierCompany[]) =>
          this.courierCompanyService.addCourierCompanyToCollectionIfMissing(courierCompanies, this.editForm.get('courier')!.value)
        )
      )
      .subscribe((courierCompanies: ICourierCompany[]) => (this.courierCompaniesSharedCollection = courierCompanies));

    this.escalationService
      .query()
      .pipe(map((res: HttpResponse<IEscalation[]>) => res.body ?? []))
      .pipe(
        map((escalations: IEscalation[]) =>
          this.escalationService.addEscalationToCollectionIfMissing(escalations, this.editForm.get('escalation')!.value)
        )
      )
      .subscribe((escalations: IEscalation[]) => (this.escalationsSharedCollection = escalations));
  }

  protected createFromForm(): IManifest {
    return {
      ...new Manifest(),
      id: this.editForm.get(['id'])!.value,
      manifestId: this.editForm.get(['manifestId'])!.value,
      awb: this.editForm.get(['awb'])!.value,
      awbAssignedDate: this.editForm.get(['awbAssignedDate'])!.value
        ? dayjs(this.editForm.get(['awbAssignedDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      pickupException: this.editForm.get(['pickupException'])!.value,
      remarks: this.editForm.get(['remarks'])!.value,
      pickupReferenceNumber: this.editForm.get(['pickupReferenceNumber'])!.value,
      status: this.editForm.get(['status'])!.value,
      order: this.editForm.get(['order'])!.value,
      courier: this.editForm.get(['courier'])!.value,
      escalation: this.editForm.get(['escalation'])!.value,
    };
  }
}

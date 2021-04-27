jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ShipmentActivityService } from '../service/shipment-activity.service';
import { IShipmentActivity, ShipmentActivity } from '../shipment-activity.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

import { ShipmentActivityUpdateComponent } from './shipment-activity-update.component';

describe('Component Tests', () => {
  describe('ShipmentActivity Management Update Component', () => {
    let comp: ShipmentActivityUpdateComponent;
    let fixture: ComponentFixture<ShipmentActivityUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let shipmentActivityService: ShipmentActivityService;
    let orderService: OrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ShipmentActivityUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ShipmentActivityUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ShipmentActivityUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      shipmentActivityService = TestBed.inject(ShipmentActivityService);
      orderService = TestBed.inject(OrderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Order query and add missing value', () => {
        const shipmentActivity: IShipmentActivity = { id: 456 };
        const order: IOrder = { id: 96360 };
        shipmentActivity.order = order;

        const orderCollection: IOrder[] = [{ id: 61547 }];
        spyOn(orderService, 'query').and.returnValue(of(new HttpResponse({ body: orderCollection })));
        const additionalOrders = [order];
        const expectedCollection: IOrder[] = [...additionalOrders, ...orderCollection];
        spyOn(orderService, 'addOrderToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ shipmentActivity });
        comp.ngOnInit();

        expect(orderService.query).toHaveBeenCalled();
        expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderCollection, ...additionalOrders);
        expect(comp.ordersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const shipmentActivity: IShipmentActivity = { id: 456 };
        const order: IOrder = { id: 80077 };
        shipmentActivity.order = order;

        activatedRoute.data = of({ shipmentActivity });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(shipmentActivity));
        expect(comp.ordersSharedCollection).toContain(order);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shipmentActivity = { id: 123 };
        spyOn(shipmentActivityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shipmentActivity }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(shipmentActivityService.update).toHaveBeenCalledWith(shipmentActivity);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shipmentActivity = new ShipmentActivity();
        spyOn(shipmentActivityService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: shipmentActivity }));
        saveSubject.complete();

        // THEN
        expect(shipmentActivityService.create).toHaveBeenCalledWith(shipmentActivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const shipmentActivity = { id: 123 };
        spyOn(shipmentActivityService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ shipmentActivity });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(shipmentActivityService.update).toHaveBeenCalledWith(shipmentActivity);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackOrderById', () => {
        it('Should return tracked Order primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackOrderById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

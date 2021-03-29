jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';
import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';
import { IOrder } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';

import { OrderBuyerDetailsUpdateComponent } from './order-buyer-details-update.component';

describe('Component Tests', () => {
  describe('OrderBuyerDetails Management Update Component', () => {
    let comp: OrderBuyerDetailsUpdateComponent;
    let fixture: ComponentFixture<OrderBuyerDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderBuyerDetailsService: OrderBuyerDetailsService;
    let orderService: OrderService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderBuyerDetailsUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(OrderBuyerDetailsUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderBuyerDetailsUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      orderBuyerDetailsService = TestBed.inject(OrderBuyerDetailsService);
      orderService = TestBed.inject(OrderService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call order query and add missing value', () => {
        const orderBuyerDetails: IOrderBuyerDetails = { id: 456 };
        const order: IOrder = { id: 611 };
        orderBuyerDetails.order = order;

        const orderCollection: IOrder[] = [{ id: 55618 }];
        spyOn(orderService, 'query').and.returnValue(of(new HttpResponse({ body: orderCollection })));
        const expectedCollection: IOrder[] = [order, ...orderCollection];
        spyOn(orderService, 'addOrderToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        expect(orderService.query).toHaveBeenCalled();
        expect(orderService.addOrderToCollectionIfMissing).toHaveBeenCalledWith(orderCollection, order);
        expect(comp.ordersCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const orderBuyerDetails: IOrderBuyerDetails = { id: 456 };
        const order: IOrder = { id: 97554 };
        orderBuyerDetails.order = order;

        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderBuyerDetails));
        expect(comp.ordersCollection).toContain(order);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orderBuyerDetails = { id: 123 };
        spyOn(orderBuyerDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderBuyerDetails }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(orderBuyerDetailsService.update).toHaveBeenCalledWith(orderBuyerDetails);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orderBuyerDetails = new OrderBuyerDetails();
        spyOn(orderBuyerDetailsService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: orderBuyerDetails }));
        saveSubject.complete();

        // THEN
        expect(orderBuyerDetailsService.create).toHaveBeenCalledWith(orderBuyerDetails);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const orderBuyerDetails = { id: 123 };
        spyOn(orderBuyerDetailsService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(orderBuyerDetailsService.update).toHaveBeenCalledWith(orderBuyerDetails);
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

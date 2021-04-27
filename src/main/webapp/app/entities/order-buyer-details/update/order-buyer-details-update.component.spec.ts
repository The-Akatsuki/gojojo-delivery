jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';
import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';

import { OrderBuyerDetailsUpdateComponent } from './order-buyer-details-update.component';

describe('Component Tests', () => {
  describe('OrderBuyerDetails Management Update Component', () => {
    let comp: OrderBuyerDetailsUpdateComponent;
    let fixture: ComponentFixture<OrderBuyerDetailsUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let orderBuyerDetailsService: OrderBuyerDetailsService;

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

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const orderBuyerDetails: IOrderBuyerDetails = { id: 456 };

        activatedRoute.data = of({ orderBuyerDetails });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(orderBuyerDetails));
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
  });
});

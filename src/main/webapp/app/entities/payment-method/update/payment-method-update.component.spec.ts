jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PaymentMethodService } from '../service/payment-method.service';
import { IPaymentMethod, PaymentMethod } from '../payment-method.model';

import { PaymentMethodUpdateComponent } from './payment-method-update.component';

describe('Component Tests', () => {
  describe('PaymentMethod Management Update Component', () => {
    let comp: PaymentMethodUpdateComponent;
    let fixture: ComponentFixture<PaymentMethodUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let paymentMethodService: PaymentMethodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentMethodUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PaymentMethodUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentMethodUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      paymentMethodService = TestBed.inject(PaymentMethodService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const paymentMethod: IPaymentMethod = { id: 456 };

        activatedRoute.data = of({ paymentMethod });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(paymentMethod));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paymentMethod = { id: 123 };
        spyOn(paymentMethodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentMethod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentMethod }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(paymentMethodService.update).toHaveBeenCalledWith(paymentMethod);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paymentMethod = new PaymentMethod();
        spyOn(paymentMethodService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentMethod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: paymentMethod }));
        saveSubject.complete();

        // THEN
        expect(paymentMethodService.create).toHaveBeenCalledWith(paymentMethod);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const paymentMethod = { id: 123 };
        spyOn(paymentMethodService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ paymentMethod });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(paymentMethodService.update).toHaveBeenCalledWith(paymentMethod);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

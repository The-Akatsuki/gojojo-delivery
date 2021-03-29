jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PickupAddressService } from '../service/pickup-address.service';
import { IPickupAddress, PickupAddress } from '../pickup-address.model';

import { PickupAddressUpdateComponent } from './pickup-address-update.component';

describe('Component Tests', () => {
  describe('PickupAddress Management Update Component', () => {
    let comp: PickupAddressUpdateComponent;
    let fixture: ComponentFixture<PickupAddressUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pickupAddressService: PickupAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PickupAddressUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PickupAddressUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PickupAddressUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pickupAddressService = TestBed.inject(PickupAddressService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const pickupAddress: IPickupAddress = { id: 456 };

        activatedRoute.data = of({ pickupAddress });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pickupAddress));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pickupAddress = { id: 123 };
        spyOn(pickupAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pickupAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pickupAddress }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pickupAddressService.update).toHaveBeenCalledWith(pickupAddress);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pickupAddress = new PickupAddress();
        spyOn(pickupAddressService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pickupAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pickupAddress }));
        saveSubject.complete();

        // THEN
        expect(pickupAddressService.create).toHaveBeenCalledWith(pickupAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pickupAddress = { id: 123 };
        spyOn(pickupAddressService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pickupAddress });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pickupAddressService.update).toHaveBeenCalledWith(pickupAddress);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

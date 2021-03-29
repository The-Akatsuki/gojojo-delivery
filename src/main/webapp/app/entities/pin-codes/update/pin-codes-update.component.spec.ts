jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PinCodesService } from '../service/pin-codes.service';
import { IPinCodes, PinCodes } from '../pin-codes.model';

import { PinCodesUpdateComponent } from './pin-codes-update.component';

describe('Component Tests', () => {
  describe('PinCodes Management Update Component', () => {
    let comp: PinCodesUpdateComponent;
    let fixture: ComponentFixture<PinCodesUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let pinCodesService: PinCodesService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PinCodesUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PinCodesUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PinCodesUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      pinCodesService = TestBed.inject(PinCodesService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const pinCodes: IPinCodes = { id: 456 };

        activatedRoute.data = of({ pinCodes });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(pinCodes));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pinCodes = { id: 123 };
        spyOn(pinCodesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pinCodes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pinCodes }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(pinCodesService.update).toHaveBeenCalledWith(pinCodes);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pinCodes = new PinCodes();
        spyOn(pinCodesService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pinCodes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: pinCodes }));
        saveSubject.complete();

        // THEN
        expect(pinCodesService.create).toHaveBeenCalledWith(pinCodes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const pinCodes = { id: 123 };
        spyOn(pinCodesService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ pinCodes });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(pinCodesService.update).toHaveBeenCalledWith(pinCodes);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReturnLabelService } from '../service/return-label.service';
import { IReturnLabel, ReturnLabel } from '../return-label.model';

import { ReturnLabelUpdateComponent } from './return-label-update.component';

describe('Component Tests', () => {
  describe('ReturnLabel Management Update Component', () => {
    let comp: ReturnLabelUpdateComponent;
    let fixture: ComponentFixture<ReturnLabelUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let returnLabelService: ReturnLabelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReturnLabelUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReturnLabelUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReturnLabelUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      returnLabelService = TestBed.inject(ReturnLabelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const returnLabel: IReturnLabel = { id: 456 };

        activatedRoute.data = of({ returnLabel });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(returnLabel));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnLabel = { id: 123 };
        spyOn(returnLabelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnLabel }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(returnLabelService.update).toHaveBeenCalledWith(returnLabel);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnLabel = new ReturnLabel();
        spyOn(returnLabelService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnLabel }));
        saveSubject.complete();

        // THEN
        expect(returnLabelService.create).toHaveBeenCalledWith(returnLabel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnLabel = { id: 123 };
        spyOn(returnLabelService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnLabel });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(returnLabelService.update).toHaveBeenCalledWith(returnLabel);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

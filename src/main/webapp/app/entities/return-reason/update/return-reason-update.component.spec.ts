jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReturnReasonService } from '../service/return-reason.service';
import { IReturnReason, ReturnReason } from '../return-reason.model';
import { IReturnLabel } from 'app/entities/return-label/return-label.model';
import { ReturnLabelService } from 'app/entities/return-label/service/return-label.service';

import { ReturnReasonUpdateComponent } from './return-reason-update.component';

describe('Component Tests', () => {
  describe('ReturnReason Management Update Component', () => {
    let comp: ReturnReasonUpdateComponent;
    let fixture: ComponentFixture<ReturnReasonUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let returnReasonService: ReturnReasonService;
    let returnLabelService: ReturnLabelService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReturnReasonUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReturnReasonUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReturnReasonUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      returnReasonService = TestBed.inject(ReturnReasonService);
      returnLabelService = TestBed.inject(ReturnLabelService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ReturnLabel query and add missing value', () => {
        const returnReason: IReturnReason = { id: 456 };
        const label: IReturnLabel = { id: 2741 };
        returnReason.label = label;

        const returnLabelCollection: IReturnLabel[] = [{ id: 81428 }];
        spyOn(returnLabelService, 'query').and.returnValue(of(new HttpResponse({ body: returnLabelCollection })));
        const additionalReturnLabels = [label];
        const expectedCollection: IReturnLabel[] = [...additionalReturnLabels, ...returnLabelCollection];
        spyOn(returnLabelService, 'addReturnLabelToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ returnReason });
        comp.ngOnInit();

        expect(returnLabelService.query).toHaveBeenCalled();
        expect(returnLabelService.addReturnLabelToCollectionIfMissing).toHaveBeenCalledWith(
          returnLabelCollection,
          ...additionalReturnLabels
        );
        expect(comp.returnLabelsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const returnReason: IReturnReason = { id: 456 };
        const label: IReturnLabel = { id: 33374 };
        returnReason.label = label;

        activatedRoute.data = of({ returnReason });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(returnReason));
        expect(comp.returnLabelsSharedCollection).toContain(label);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnReason = { id: 123 };
        spyOn(returnReasonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnReason });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnReason }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(returnReasonService.update).toHaveBeenCalledWith(returnReason);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnReason = new ReturnReason();
        spyOn(returnReasonService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnReason });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: returnReason }));
        saveSubject.complete();

        // THEN
        expect(returnReasonService.create).toHaveBeenCalledWith(returnReason);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const returnReason = { id: 123 };
        spyOn(returnReasonService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ returnReason });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(returnReasonService.update).toHaveBeenCalledWith(returnReason);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackReturnLabelById', () => {
        it('Should return tracked ReturnLabel primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReturnLabelById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

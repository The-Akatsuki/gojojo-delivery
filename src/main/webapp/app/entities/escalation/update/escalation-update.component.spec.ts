jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { EscalationService } from '../service/escalation.service';
import { IEscalation, Escalation } from '../escalation.model';
import { IReasonEscalation } from 'app/entities/reason-escalation/reason-escalation.model';
import { ReasonEscalationService } from 'app/entities/reason-escalation/service/reason-escalation.service';

import { EscalationUpdateComponent } from './escalation-update.component';

describe('Component Tests', () => {
  describe('Escalation Management Update Component', () => {
    let comp: EscalationUpdateComponent;
    let fixture: ComponentFixture<EscalationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let escalationService: EscalationService;
    let reasonEscalationService: ReasonEscalationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EscalationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(EscalationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EscalationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      escalationService = TestBed.inject(EscalationService);
      reasonEscalationService = TestBed.inject(ReasonEscalationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call ReasonEscalation query and add missing value', () => {
        const escalation: IEscalation = { id: 456 };
        const reason: IReasonEscalation = { id: 36839 };
        escalation.reason = reason;

        const reasonEscalationCollection: IReasonEscalation[] = [{ id: 30841 }];
        spyOn(reasonEscalationService, 'query').and.returnValue(of(new HttpResponse({ body: reasonEscalationCollection })));
        const additionalReasonEscalations = [reason];
        const expectedCollection: IReasonEscalation[] = [...additionalReasonEscalations, ...reasonEscalationCollection];
        spyOn(reasonEscalationService, 'addReasonEscalationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ escalation });
        comp.ngOnInit();

        expect(reasonEscalationService.query).toHaveBeenCalled();
        expect(reasonEscalationService.addReasonEscalationToCollectionIfMissing).toHaveBeenCalledWith(
          reasonEscalationCollection,
          ...additionalReasonEscalations
        );
        expect(comp.reasonEscalationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const escalation: IEscalation = { id: 456 };
        const reason: IReasonEscalation = { id: 90357 };
        escalation.reason = reason;

        activatedRoute.data = of({ escalation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(escalation));
        expect(comp.reasonEscalationsSharedCollection).toContain(reason);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const escalation = { id: 123 };
        spyOn(escalationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ escalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: escalation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(escalationService.update).toHaveBeenCalledWith(escalation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const escalation = new Escalation();
        spyOn(escalationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ escalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: escalation }));
        saveSubject.complete();

        // THEN
        expect(escalationService.create).toHaveBeenCalledWith(escalation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const escalation = { id: 123 };
        spyOn(escalationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ escalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(escalationService.update).toHaveBeenCalledWith(escalation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackReasonEscalationById', () => {
        it('Should return tracked ReasonEscalation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackReasonEscalationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

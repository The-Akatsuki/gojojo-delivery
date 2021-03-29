jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ReasonEscalationService } from '../service/reason-escalation.service';
import { IReasonEscalation, ReasonEscalation } from '../reason-escalation.model';

import { ReasonEscalationUpdateComponent } from './reason-escalation-update.component';

describe('Component Tests', () => {
  describe('ReasonEscalation Management Update Component', () => {
    let comp: ReasonEscalationUpdateComponent;
    let fixture: ComponentFixture<ReasonEscalationUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let reasonEscalationService: ReasonEscalationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReasonEscalationUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ReasonEscalationUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReasonEscalationUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      reasonEscalationService = TestBed.inject(ReasonEscalationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const reasonEscalation: IReasonEscalation = { id: 456 };

        activatedRoute.data = of({ reasonEscalation });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(reasonEscalation));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reasonEscalation = { id: 123 };
        spyOn(reasonEscalationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reasonEscalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reasonEscalation }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(reasonEscalationService.update).toHaveBeenCalledWith(reasonEscalation);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reasonEscalation = new ReasonEscalation();
        spyOn(reasonEscalationService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reasonEscalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: reasonEscalation }));
        saveSubject.complete();

        // THEN
        expect(reasonEscalationService.create).toHaveBeenCalledWith(reasonEscalation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const reasonEscalation = { id: 123 };
        spyOn(reasonEscalationService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ reasonEscalation });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(reasonEscalationService.update).toHaveBeenCalledWith(reasonEscalation);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});

jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ManifestService } from '../service/manifest.service';
import { IManifest, Manifest } from '../manifest.model';
import { ICourierCompany } from 'app/entities/courier-company/courier-company.model';
import { CourierCompanyService } from 'app/entities/courier-company/service/courier-company.service';
import { IEscalation } from 'app/entities/escalation/escalation.model';
import { EscalationService } from 'app/entities/escalation/service/escalation.service';

import { ManifestUpdateComponent } from './manifest-update.component';

describe('Component Tests', () => {
  describe('Manifest Management Update Component', () => {
    let comp: ManifestUpdateComponent;
    let fixture: ComponentFixture<ManifestUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let manifestService: ManifestService;
    let courierCompanyService: CourierCompanyService;
    let escalationService: EscalationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ManifestUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ManifestUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManifestUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      manifestService = TestBed.inject(ManifestService);
      courierCompanyService = TestBed.inject(CourierCompanyService);
      escalationService = TestBed.inject(EscalationService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call CourierCompany query and add missing value', () => {
        const manifest: IManifest = { id: 456 };
        const courier: ICourierCompany = { id: 52461 };
        manifest.courier = courier;

        const courierCompanyCollection: ICourierCompany[] = [{ id: 13774 }];
        spyOn(courierCompanyService, 'query').and.returnValue(of(new HttpResponse({ body: courierCompanyCollection })));
        const additionalCourierCompanies = [courier];
        const expectedCollection: ICourierCompany[] = [...additionalCourierCompanies, ...courierCompanyCollection];
        spyOn(courierCompanyService, 'addCourierCompanyToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        expect(courierCompanyService.query).toHaveBeenCalled();
        expect(courierCompanyService.addCourierCompanyToCollectionIfMissing).toHaveBeenCalledWith(
          courierCompanyCollection,
          ...additionalCourierCompanies
        );
        expect(comp.courierCompaniesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Escalation query and add missing value', () => {
        const manifest: IManifest = { id: 456 };
        const escalation: IEscalation = { id: 77544 };
        manifest.escalation = escalation;

        const escalationCollection: IEscalation[] = [{ id: 38916 }];
        spyOn(escalationService, 'query').and.returnValue(of(new HttpResponse({ body: escalationCollection })));
        const additionalEscalations = [escalation];
        const expectedCollection: IEscalation[] = [...additionalEscalations, ...escalationCollection];
        spyOn(escalationService, 'addEscalationToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        expect(escalationService.query).toHaveBeenCalled();
        expect(escalationService.addEscalationToCollectionIfMissing).toHaveBeenCalledWith(escalationCollection, ...additionalEscalations);
        expect(comp.escalationsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const manifest: IManifest = { id: 456 };
        const courier: ICourierCompany = { id: 683 };
        manifest.courier = courier;
        const escalation: IEscalation = { id: 88444 };
        manifest.escalation = escalation;

        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(manifest));
        expect(comp.courierCompaniesSharedCollection).toContain(courier);
        expect(comp.escalationsSharedCollection).toContain(escalation);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manifest = { id: 123 };
        spyOn(manifestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: manifest }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(manifestService.update).toHaveBeenCalledWith(manifest);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manifest = new Manifest();
        spyOn(manifestService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: manifest }));
        saveSubject.complete();

        // THEN
        expect(manifestService.create).toHaveBeenCalledWith(manifest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const manifest = { id: 123 };
        spyOn(manifestService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ manifest });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(manifestService.update).toHaveBeenCalledWith(manifest);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackCourierCompanyById', () => {
        it('Should return tracked CourierCompany primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCourierCompanyById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackEscalationById', () => {
        it('Should return tracked Escalation primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackEscalationById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});

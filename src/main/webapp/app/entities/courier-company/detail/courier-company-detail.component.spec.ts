import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CourierCompanyDetailComponent } from './courier-company-detail.component';

describe('Component Tests', () => {
  describe('CourierCompany Management Detail Component', () => {
    let comp: CourierCompanyDetailComponent;
    let fixture: ComponentFixture<CourierCompanyDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CourierCompanyDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ courierCompany: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CourierCompanyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CourierCompanyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load courierCompany on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.courierCompany).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

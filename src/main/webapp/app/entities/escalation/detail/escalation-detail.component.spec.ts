import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EscalationDetailComponent } from './escalation-detail.component';

describe('Component Tests', () => {
  describe('Escalation Management Detail Component', () => {
    let comp: EscalationDetailComponent;
    let fixture: ComponentFixture<EscalationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [EscalationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ escalation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(EscalationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(EscalationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load escalation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.escalation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

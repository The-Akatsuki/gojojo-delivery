import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReasonEscalationDetailComponent } from './reason-escalation-detail.component';

describe('Component Tests', () => {
  describe('ReasonEscalation Management Detail Component', () => {
    let comp: ReasonEscalationDetailComponent;
    let fixture: ComponentFixture<ReasonEscalationDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReasonEscalationDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ reasonEscalation: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReasonEscalationDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReasonEscalationDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load reasonEscalation on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.reasonEscalation).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

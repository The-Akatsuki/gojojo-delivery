import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReturnReasonDetailComponent } from './return-reason-detail.component';

describe('Component Tests', () => {
  describe('ReturnReason Management Detail Component', () => {
    let comp: ReturnReasonDetailComponent;
    let fixture: ComponentFixture<ReturnReasonDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReturnReasonDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ returnReason: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReturnReasonDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReturnReasonDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load returnReason on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.returnReason).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

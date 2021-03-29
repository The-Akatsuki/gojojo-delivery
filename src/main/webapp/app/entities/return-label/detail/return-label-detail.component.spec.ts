import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ReturnLabelDetailComponent } from './return-label-detail.component';

describe('Component Tests', () => {
  describe('ReturnLabel Management Detail Component', () => {
    let comp: ReturnLabelDetailComponent;
    let fixture: ComponentFixture<ReturnLabelDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ReturnLabelDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ returnLabel: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ReturnLabelDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ReturnLabelDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load returnLabel on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.returnLabel).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

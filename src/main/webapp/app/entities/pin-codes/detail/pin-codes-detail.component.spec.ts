import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PinCodesDetailComponent } from './pin-codes-detail.component';

describe('Component Tests', () => {
  describe('PinCodes Management Detail Component', () => {
    let comp: PinCodesDetailComponent;
    let fixture: ComponentFixture<PinCodesDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PinCodesDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pinCodes: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PinCodesDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PinCodesDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pinCodes on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pinCodes).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

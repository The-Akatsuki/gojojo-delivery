import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PickupAddressDetailComponent } from './pickup-address-detail.component';

describe('Component Tests', () => {
  describe('PickupAddress Management Detail Component', () => {
    let comp: PickupAddressDetailComponent;
    let fixture: ComponentFixture<PickupAddressDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PickupAddressDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ pickupAddress: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PickupAddressDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PickupAddressDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load pickupAddress on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.pickupAddress).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

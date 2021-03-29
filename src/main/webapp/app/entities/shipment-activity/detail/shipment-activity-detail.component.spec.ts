import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShipmentActivityDetailComponent } from './shipment-activity-detail.component';

describe('Component Tests', () => {
  describe('ShipmentActivity Management Detail Component', () => {
    let comp: ShipmentActivityDetailComponent;
    let fixture: ComponentFixture<ShipmentActivityDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ShipmentActivityDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ shipmentActivity: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ShipmentActivityDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ShipmentActivityDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load shipmentActivity on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.shipmentActivity).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

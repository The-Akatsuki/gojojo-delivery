import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { OrderBuyerDetailsDetailComponent } from './order-buyer-details-detail.component';

describe('Component Tests', () => {
  describe('OrderBuyerDetails Management Detail Component', () => {
    let comp: OrderBuyerDetailsDetailComponent;
    let fixture: ComponentFixture<OrderBuyerDetailsDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [OrderBuyerDetailsDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ orderBuyerDetails: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(OrderBuyerDetailsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(OrderBuyerDetailsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load orderBuyerDetails on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.orderBuyerDetails).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

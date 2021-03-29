import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';

import { OrderBuyerDetailsComponent } from './order-buyer-details.component';

describe('Component Tests', () => {
  describe('OrderBuyerDetails Management Component', () => {
    let comp: OrderBuyerDetailsComponent;
    let fixture: ComponentFixture<OrderBuyerDetailsComponent>;
    let service: OrderBuyerDetailsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [OrderBuyerDetailsComponent],
      })
        .overrideTemplate(OrderBuyerDetailsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(OrderBuyerDetailsComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(OrderBuyerDetailsService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.orderBuyerDetails?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

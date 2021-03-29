import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PaymentMethodService } from '../service/payment-method.service';

import { PaymentMethodComponent } from './payment-method.component';

describe('Component Tests', () => {
  describe('PaymentMethod Management Component', () => {
    let comp: PaymentMethodComponent;
    let fixture: ComponentFixture<PaymentMethodComponent>;
    let service: PaymentMethodService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PaymentMethodComponent],
      })
        .overrideTemplate(PaymentMethodComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PaymentMethodComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PaymentMethodService);

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
      expect(comp.paymentMethods?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

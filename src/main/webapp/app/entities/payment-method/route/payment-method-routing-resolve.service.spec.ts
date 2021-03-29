jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPaymentMethod, PaymentMethod } from '../payment-method.model';
import { PaymentMethodService } from '../service/payment-method.service';

import { PaymentMethodRoutingResolveService } from './payment-method-routing-resolve.service';

describe('Service Tests', () => {
  describe('PaymentMethod routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PaymentMethodRoutingResolveService;
    let service: PaymentMethodService;
    let resultPaymentMethod: IPaymentMethod | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PaymentMethodRoutingResolveService);
      service = TestBed.inject(PaymentMethodService);
      resultPaymentMethod = undefined;
    });

    describe('resolve', () => {
      it('should return IPaymentMethod returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentMethod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentMethod).toEqual({ id: 123 });
      });

      it('should return new IPaymentMethod if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentMethod = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPaymentMethod).toEqual(new PaymentMethod());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPaymentMethod = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPaymentMethod).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IOrderBuyerDetails, OrderBuyerDetails } from '../order-buyer-details.model';
import { OrderBuyerDetailsService } from '../service/order-buyer-details.service';

import { OrderBuyerDetailsRoutingResolveService } from './order-buyer-details-routing-resolve.service';

describe('Service Tests', () => {
  describe('OrderBuyerDetails routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: OrderBuyerDetailsRoutingResolveService;
    let service: OrderBuyerDetailsService;
    let resultOrderBuyerDetails: IOrderBuyerDetails | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(OrderBuyerDetailsRoutingResolveService);
      service = TestBed.inject(OrderBuyerDetailsService);
      resultOrderBuyerDetails = undefined;
    });

    describe('resolve', () => {
      it('should return IOrderBuyerDetails returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderBuyerDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderBuyerDetails).toEqual({ id: 123 });
      });

      it('should return new IOrderBuyerDetails if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderBuyerDetails = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultOrderBuyerDetails).toEqual(new OrderBuyerDetails());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultOrderBuyerDetails = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultOrderBuyerDetails).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

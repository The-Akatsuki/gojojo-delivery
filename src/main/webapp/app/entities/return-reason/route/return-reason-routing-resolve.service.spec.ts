jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReturnReason, ReturnReason } from '../return-reason.model';
import { ReturnReasonService } from '../service/return-reason.service';

import { ReturnReasonRoutingResolveService } from './return-reason-routing-resolve.service';

describe('Service Tests', () => {
  describe('ReturnReason routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReturnReasonRoutingResolveService;
    let service: ReturnReasonService;
    let resultReturnReason: IReturnReason | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReturnReasonRoutingResolveService);
      service = TestBed.inject(ReturnReasonService);
      resultReturnReason = undefined;
    });

    describe('resolve', () => {
      it('should return IReturnReason returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnReason = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnReason).toEqual({ id: 123 });
      });

      it('should return new IReturnReason if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnReason = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReturnReason).toEqual(new ReturnReason());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnReason = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnReason).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReturnLabel, ReturnLabel } from '../return-label.model';
import { ReturnLabelService } from '../service/return-label.service';

import { ReturnLabelRoutingResolveService } from './return-label-routing-resolve.service';

describe('Service Tests', () => {
  describe('ReturnLabel routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReturnLabelRoutingResolveService;
    let service: ReturnLabelService;
    let resultReturnLabel: IReturnLabel | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReturnLabelRoutingResolveService);
      service = TestBed.inject(ReturnLabelService);
      resultReturnLabel = undefined;
    });

    describe('resolve', () => {
      it('should return IReturnLabel returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnLabel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnLabel).toEqual({ id: 123 });
      });

      it('should return new IReturnLabel if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnLabel = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReturnLabel).toEqual(new ReturnLabel());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReturnLabel = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReturnLabel).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

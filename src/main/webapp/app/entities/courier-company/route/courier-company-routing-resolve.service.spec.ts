jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICourierCompany, CourierCompany } from '../courier-company.model';
import { CourierCompanyService } from '../service/courier-company.service';

import { CourierCompanyRoutingResolveService } from './courier-company-routing-resolve.service';

describe('Service Tests', () => {
  describe('CourierCompany routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CourierCompanyRoutingResolveService;
    let service: CourierCompanyService;
    let resultCourierCompany: ICourierCompany | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CourierCompanyRoutingResolveService);
      service = TestBed.inject(CourierCompanyService);
      resultCourierCompany = undefined;
    });

    describe('resolve', () => {
      it('should return ICourierCompany returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourierCompany = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourierCompany).toEqual({ id: 123 });
      });

      it('should return new ICourierCompany if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourierCompany = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCourierCompany).toEqual(new CourierCompany());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCourierCompany = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCourierCompany).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

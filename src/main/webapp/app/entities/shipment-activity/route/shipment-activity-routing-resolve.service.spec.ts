jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IShipmentActivity, ShipmentActivity } from '../shipment-activity.model';
import { ShipmentActivityService } from '../service/shipment-activity.service';

import { ShipmentActivityRoutingResolveService } from './shipment-activity-routing-resolve.service';

describe('Service Tests', () => {
  describe('ShipmentActivity routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ShipmentActivityRoutingResolveService;
    let service: ShipmentActivityService;
    let resultShipmentActivity: IShipmentActivity | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ShipmentActivityRoutingResolveService);
      service = TestBed.inject(ShipmentActivityService);
      resultShipmentActivity = undefined;
    });

    describe('resolve', () => {
      it('should return IShipmentActivity returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShipmentActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultShipmentActivity).toEqual({ id: 123 });
      });

      it('should return new IShipmentActivity if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShipmentActivity = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultShipmentActivity).toEqual(new ShipmentActivity());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultShipmentActivity = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultShipmentActivity).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

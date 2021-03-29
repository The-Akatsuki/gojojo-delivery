jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPickupAddress, PickupAddress } from '../pickup-address.model';
import { PickupAddressService } from '../service/pickup-address.service';

import { PickupAddressRoutingResolveService } from './pickup-address-routing-resolve.service';

describe('Service Tests', () => {
  describe('PickupAddress routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PickupAddressRoutingResolveService;
    let service: PickupAddressService;
    let resultPickupAddress: IPickupAddress | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PickupAddressRoutingResolveService);
      service = TestBed.inject(PickupAddressService);
      resultPickupAddress = undefined;
    });

    describe('resolve', () => {
      it('should return IPickupAddress returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPickupAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPickupAddress).toEqual({ id: 123 });
      });

      it('should return new IPickupAddress if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPickupAddress = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPickupAddress).toEqual(new PickupAddress());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPickupAddress = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPickupAddress).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

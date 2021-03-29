jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IManifest, Manifest } from '../manifest.model';
import { ManifestService } from '../service/manifest.service';

import { ManifestRoutingResolveService } from './manifest-routing-resolve.service';

describe('Service Tests', () => {
  describe('Manifest routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ManifestRoutingResolveService;
    let service: ManifestService;
    let resultManifest: IManifest | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ManifestRoutingResolveService);
      service = TestBed.inject(ManifestService);
      resultManifest = undefined;
    });

    describe('resolve', () => {
      it('should return IManifest returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManifest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultManifest).toEqual({ id: 123 });
      });

      it('should return new IManifest if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManifest = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultManifest).toEqual(new Manifest());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultManifest = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultManifest).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IReasonEscalation, ReasonEscalation } from '../reason-escalation.model';
import { ReasonEscalationService } from '../service/reason-escalation.service';

import { ReasonEscalationRoutingResolveService } from './reason-escalation-routing-resolve.service';

describe('Service Tests', () => {
  describe('ReasonEscalation routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ReasonEscalationRoutingResolveService;
    let service: ReasonEscalationService;
    let resultReasonEscalation: IReasonEscalation | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ReasonEscalationRoutingResolveService);
      service = TestBed.inject(ReasonEscalationService);
      resultReasonEscalation = undefined;
    });

    describe('resolve', () => {
      it('should return IReasonEscalation returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReasonEscalation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReasonEscalation).toEqual({ id: 123 });
      });

      it('should return new IReasonEscalation if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReasonEscalation = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultReasonEscalation).toEqual(new ReasonEscalation());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultReasonEscalation = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultReasonEscalation).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});

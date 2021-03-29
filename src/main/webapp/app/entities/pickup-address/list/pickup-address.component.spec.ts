import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { PickupAddressService } from '../service/pickup-address.service';

import { PickupAddressComponent } from './pickup-address.component';

describe('Component Tests', () => {
  describe('PickupAddress Management Component', () => {
    let comp: PickupAddressComponent;
    let fixture: ComponentFixture<PickupAddressComponent>;
    let service: PickupAddressService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PickupAddressComponent],
      })
        .overrideTemplate(PickupAddressComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PickupAddressComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(PickupAddressService);

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
      expect(comp.pickupAddresses?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ReturnReasonService } from '../service/return-reason.service';

import { ReturnReasonComponent } from './return-reason.component';

describe('Component Tests', () => {
  describe('ReturnReason Management Component', () => {
    let comp: ReturnReasonComponent;
    let fixture: ComponentFixture<ReturnReasonComponent>;
    let service: ReturnReasonService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ReturnReasonComponent],
      })
        .overrideTemplate(ReturnReasonComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ReturnReasonComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ReturnReasonService);

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
      expect(comp.returnReasons?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

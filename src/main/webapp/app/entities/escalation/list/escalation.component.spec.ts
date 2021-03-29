import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { EscalationService } from '../service/escalation.service';

import { EscalationComponent } from './escalation.component';

describe('Component Tests', () => {
  describe('Escalation Management Component', () => {
    let comp: EscalationComponent;
    let fixture: ComponentFixture<EscalationComponent>;
    let service: EscalationService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [EscalationComponent],
      })
        .overrideTemplate(EscalationComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(EscalationComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(EscalationService);

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
      expect(comp.escalations?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

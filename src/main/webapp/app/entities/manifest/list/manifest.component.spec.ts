import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ManifestService } from '../service/manifest.service';

import { ManifestComponent } from './manifest.component';

describe('Component Tests', () => {
  describe('Manifest Management Component', () => {
    let comp: ManifestComponent;
    let fixture: ComponentFixture<ManifestComponent>;
    let service: ManifestService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ManifestComponent],
      })
        .overrideTemplate(ManifestComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ManifestComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ManifestService);

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
      expect(comp.manifests?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ManifestDetailComponent } from './manifest-detail.component';

describe('Component Tests', () => {
  describe('Manifest Management Detail Component', () => {
    let comp: ManifestDetailComponent;
    let fixture: ComponentFixture<ManifestDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ManifestDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ manifest: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ManifestDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ManifestDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load manifest on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.manifest).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});

import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManifest } from '../manifest.model';

@Component({
  selector: 'jhi-manifest-detail',
  templateUrl: './manifest-detail.component.html',
})
export class ManifestDetailComponent implements OnInit {
  manifest: IManifest | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ manifest }) => {
      this.manifest = manifest;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

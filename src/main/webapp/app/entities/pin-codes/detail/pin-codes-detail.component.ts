import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPinCodes } from '../pin-codes.model';

@Component({
  selector: 'jhi-pin-codes-detail',
  templateUrl: './pin-codes-detail.component.html',
})
export class PinCodesDetailComponent implements OnInit {
  pinCodes: IPinCodes | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ pinCodes }) => {
      this.pinCodes = pinCodes;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

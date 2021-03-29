import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReturnLabel } from '../return-label.model';

@Component({
  selector: 'jhi-return-label-detail',
  templateUrl: './return-label-detail.component.html',
})
export class ReturnLabelDetailComponent implements OnInit {
  returnLabel: IReturnLabel | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnLabel }) => {
      this.returnLabel = returnLabel;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

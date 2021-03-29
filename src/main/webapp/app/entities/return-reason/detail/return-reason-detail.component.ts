import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReturnReason } from '../return-reason.model';

@Component({
  selector: 'jhi-return-reason-detail',
  templateUrl: './return-reason-detail.component.html',
})
export class ReturnReasonDetailComponent implements OnInit {
  returnReason: IReturnReason | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ returnReason }) => {
      this.returnReason = returnReason;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

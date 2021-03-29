import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IReasonEscalation } from '../reason-escalation.model';

@Component({
  selector: 'jhi-reason-escalation-detail',
  templateUrl: './reason-escalation-detail.component.html',
})
export class ReasonEscalationDetailComponent implements OnInit {
  reasonEscalation: IReasonEscalation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ reasonEscalation }) => {
      this.reasonEscalation = reasonEscalation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

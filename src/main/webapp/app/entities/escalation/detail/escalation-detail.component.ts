import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IEscalation } from '../escalation.model';

@Component({
  selector: 'jhi-escalation-detail',
  templateUrl: './escalation-detail.component.html',
})
export class EscalationDetailComponent implements OnInit {
  escalation: IEscalation | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ escalation }) => {
      this.escalation = escalation;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

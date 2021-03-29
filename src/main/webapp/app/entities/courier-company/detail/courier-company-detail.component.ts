import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICourierCompany } from '../courier-company.model';

@Component({
  selector: 'jhi-courier-company-detail',
  templateUrl: './courier-company-detail.component.html',
})
export class CourierCompanyDetailComponent implements OnInit {
  courierCompany: ICourierCompany | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ courierCompany }) => {
      this.courierCompany = courierCompany;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

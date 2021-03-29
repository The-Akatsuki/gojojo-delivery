import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { CourierCompanyComponent } from './list/courier-company.component';
import { CourierCompanyDetailComponent } from './detail/courier-company-detail.component';
import { CourierCompanyUpdateComponent } from './update/courier-company-update.component';
import { CourierCompanyDeleteDialogComponent } from './delete/courier-company-delete-dialog.component';
import { CourierCompanyRoutingModule } from './route/courier-company-routing.module';

@NgModule({
  imports: [SharedModule, CourierCompanyRoutingModule],
  declarations: [
    CourierCompanyComponent,
    CourierCompanyDetailComponent,
    CourierCompanyUpdateComponent,
    CourierCompanyDeleteDialogComponent,
  ],
  entryComponents: [CourierCompanyDeleteDialogComponent],
})
export class CourierCompanyModule {}

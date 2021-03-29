import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { EscalationComponent } from './list/escalation.component';
import { EscalationDetailComponent } from './detail/escalation-detail.component';
import { EscalationUpdateComponent } from './update/escalation-update.component';
import { EscalationDeleteDialogComponent } from './delete/escalation-delete-dialog.component';
import { EscalationRoutingModule } from './route/escalation-routing.module';

@NgModule({
  imports: [SharedModule, EscalationRoutingModule],
  declarations: [EscalationComponent, EscalationDetailComponent, EscalationUpdateComponent, EscalationDeleteDialogComponent],
  entryComponents: [EscalationDeleteDialogComponent],
})
export class EscalationModule {}

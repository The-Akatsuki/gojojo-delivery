import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ReasonEscalationComponent } from './list/reason-escalation.component';
import { ReasonEscalationDetailComponent } from './detail/reason-escalation-detail.component';
import { ReasonEscalationUpdateComponent } from './update/reason-escalation-update.component';
import { ReasonEscalationDeleteDialogComponent } from './delete/reason-escalation-delete-dialog.component';
import { ReasonEscalationRoutingModule } from './route/reason-escalation-routing.module';

@NgModule({
  imports: [SharedModule, ReasonEscalationRoutingModule],
  declarations: [
    ReasonEscalationComponent,
    ReasonEscalationDetailComponent,
    ReasonEscalationUpdateComponent,
    ReasonEscalationDeleteDialogComponent,
  ],
  entryComponents: [ReasonEscalationDeleteDialogComponent],
})
export class ReasonEscalationModule {}

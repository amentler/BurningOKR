import { Component, Inject, OnInit, ViewEncapsulation } from '@angular/core';
import { OkrTopicDraft } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft';
import { status } from '../../shared/model/ui/OrganizationalUnit/okr-topic-draft/okr-topic-draft-status-enum';
import { User } from '../../shared/model/api/user';
import { NEVER, of } from 'rxjs';
import { MatDialogRef } from '@angular/material/dialog';
import { MAT_DIALOG_DATA } from '@angular/material';
import { FormControl, FormGroup } from '@angular/forms';
import { OkrChildUnitRoleService } from '../../shared/services/helper/okr-child-unit-role.service';
import { CurrentUserService } from '../../core/services/current-user.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/internal/operators';
import { shareReplay, switchMap } from 'rxjs/operators';

export interface SubmittedTopicDraftDetailsFormData {
  topicDraft: OkrTopicDraft;
}

@Component({
  selector: 'app-submitted-topic-draft-details',
  templateUrl: './submitted-topic-draft-details.component.html',
  styleUrls: ['./submitted-topic-draft-details.component.css'],
  encapsulation: ViewEncapsulation.None
})

export class SubmittedTopicDraftDetailsComponent implements OnInit {

  enumStatus = status;
  topicDraft: OkrTopicDraft;
  submittedTopicDraftDetailsForm: FormGroup;
  canEdit$: Observable<boolean>;

  constructor(private dialogRef: MatDialogRef<SubmittedTopicDraftDetailsComponent>,
              private okrChildUnitRoleService: OkrChildUnitRoleService,
              private currentUserService: CurrentUserService,
              @Inject(MAT_DIALOG_DATA) private formData: (SubmittedTopicDraftDetailsFormData | any)) {
  }

  ngOnInit(): void {
    this.topicDraft = this.formData.topicDraft;
    this.submittedTopicDraftDetailsForm = new FormGroup({
        name: new FormControl(this.topicDraft.name),
        currentStatus: new FormControl(this.topicDraft.currentStatus),
        beginning: new FormControl(this.topicDraft.beginning.toLocaleDateString()),
        initiator: new FormControl(this.topicDraft.initiator),
        contributesTo: new FormControl(this.topicDraft.contributesTo),
        handoverPlan: new FormControl(this.topicDraft.handoverPlan),
        dependencies: new FormControl(this.topicDraft.dependencies),
        resources: new FormControl(this.topicDraft.resources)
      }
    );
    this.canEdit$ = this.currentUserService.getCurrentUser$()
      .pipe(
        map((currentUser: User) => {
          return currentUser.id === this.topicDraft.initiatorId;
        }),
        switchMap((canEdit: boolean) => {
          if (canEdit) {
            return of(canEdit);
          } else {
            return this.currentUserService.isCurrentUserAdmin$();
          }
        }),
        shareReplay()
    );
  }

  // TODO Methode wird in anderer Task bearbeitet
  editDialog(): void {
    // tslint:disable-next-line
    console.log('Not implemented');
  }

  closeDialog(): void {
    this.dialogRef.close(NEVER);
  }
}

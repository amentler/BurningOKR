import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SubmittedTopicDraftCardsWrapperComponent } from './submitted-topic-draft-cards-wrapper.component';
import { CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

describe('SubmittedTopicDraftCardsWrapperComponent', () => {
  let component: SubmittedTopicDraftCardsWrapperComponent;
  let fixture: ComponentFixture<SubmittedTopicDraftCardsWrapperComponent>;
  const topicDraftMock: any = jest.fn();

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      schemas: [ CUSTOM_ELEMENTS_SCHEMA ],
      declarations: [ SubmittedTopicDraftCardsWrapperComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmittedTopicDraftCardsWrapperComponent);
    component = fixture.componentInstance;
    component.topicDrafts = topicDraftMock;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component)
        .toBeTruthy();
  });
});

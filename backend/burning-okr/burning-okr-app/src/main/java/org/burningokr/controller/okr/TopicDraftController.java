package org.burningokr.controller.okr;

import java.util.Collection;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.service.okr.OkrTopicDraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestApiController
public class TopicDraftController {
  private OkrTopicDraftService okrTopicDraftService;
  private DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper;

  /**
   * Initialize TopicDraftController
   *
   * @param okrTopicDraftService a {@Link OkrTopicDraftService} object
   * @param okrTopicDraftMapper {@link DataMapper} object with {@link OkrTopicDraft} and {@link
   *     OkrTopicDraftDto}
   */
  @Autowired
  public TopicDraftController(
      OkrTopicDraftService okrTopicDraftService,
      DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper) {
    this.okrTopicDraftService = okrTopicDraftService;
    this.okrTopicDraftMapper = okrTopicDraftMapper;
  }

  /**
   * API Endpoint to get all TopicDrafts.
   *
   * @return a {@link ResponseEntity} ok with a {@link Collection} of TopicDrafts
   */
  @GetMapping("/topicDrafts/all")
  public ResponseEntity<Collection<OkrTopicDraftDto>> getAllCompanies() {
    Collection<OkrTopicDraft> topicDrafts = okrTopicDraftService.getAllTopicDrafts();
    return ResponseEntity.ok(okrTopicDraftMapper.mapEntitiesToDtos(topicDrafts));
  }
}

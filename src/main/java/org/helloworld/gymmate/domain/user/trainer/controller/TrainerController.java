package org.helloworld.gymmate.domain.user.trainer.controller;

import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class TrainerController {

	private final TrainerService trainerService;

}

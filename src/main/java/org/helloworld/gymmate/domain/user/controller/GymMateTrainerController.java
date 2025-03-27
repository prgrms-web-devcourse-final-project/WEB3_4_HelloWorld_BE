package org.helloworld.gymmate.domain.user.controller;

import org.helloworld.gymmate.domain.user.service.GymMateTrainerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/trainer")
@RequiredArgsConstructor
public class GymMateTrainerController {

	private final GymMateTrainerService trainerService;

}

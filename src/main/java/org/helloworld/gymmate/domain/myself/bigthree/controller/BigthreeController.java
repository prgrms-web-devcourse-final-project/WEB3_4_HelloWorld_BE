package org.helloworld.gymmate.domain.myself.bigthree.controller;

import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.service.BigthreeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bigthree")
@RequiredArgsConstructor
public class BigthreeController {
    private BigthreeService bigthreeService;

}

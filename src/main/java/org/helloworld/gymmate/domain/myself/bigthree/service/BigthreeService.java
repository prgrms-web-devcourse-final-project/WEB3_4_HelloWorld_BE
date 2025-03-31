package org.helloworld.gymmate.domain.myself.bigthree.service;

import lombok.RequiredArgsConstructor;
import org.helloworld.gymmate.domain.myself.bigthree.repository.BigthreeRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BigthreeService {
    private BigthreeRepository bigthreeRepository;
    
}

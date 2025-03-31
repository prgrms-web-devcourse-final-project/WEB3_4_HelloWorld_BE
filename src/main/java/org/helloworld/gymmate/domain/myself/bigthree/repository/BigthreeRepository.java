package org.helloworld.gymmate.domain.myself.bigthree.repository;

import org.helloworld.gymmate.domain.myself.bigthree.entity.Bigthree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BigthreeRepository extends JpaRepository<Bigthree, Long> {
}

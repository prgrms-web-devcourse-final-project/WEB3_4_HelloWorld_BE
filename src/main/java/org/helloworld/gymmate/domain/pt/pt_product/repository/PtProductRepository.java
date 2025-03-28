package org.helloworld.gymmate.domain.pt.pt_product.repository;

import org.helloworld.gymmate.domain.pt.pt_product.entity.PtProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PtProductRepository extends JpaRepository<PtProduct, Long> {
}

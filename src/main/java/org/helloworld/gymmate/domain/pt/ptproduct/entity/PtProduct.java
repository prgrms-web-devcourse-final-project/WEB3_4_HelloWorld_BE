package org.helloworld.gymmate.domain.pt.ptproduct.entity;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductModifyRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "pt_product")
public class PtProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pt_product_id", nullable = false)
    private Long ptProductId;

    @Column(name = "pt_product_name", nullable = false)
    private String ptProductName;

    @Column(name = "info", nullable = false)
    private String info;

    @Column(name = "pt_product_fee", nullable = false)
    private Long ptProductFee;

    @Column(name = "trainer_id", nullable = false)
    private Long trainerId;

    @OneToMany(mappedBy = "ptProduct", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<PtProductImage> ptProductImages = new ArrayList<>();

    // ====== Business Logic ======

    public void update(PtProductModifyRequest request) {
        this.ptProductName = request.ptProductName();
        this.info = request.info();
        this.ptProductFee = request.ptProductFee();
    }

    public void removeImageByUrl(String imageUrl) {
        this.ptProductImages.removeIf(image -> image.getUrl().equals(imageUrl));
    }
}

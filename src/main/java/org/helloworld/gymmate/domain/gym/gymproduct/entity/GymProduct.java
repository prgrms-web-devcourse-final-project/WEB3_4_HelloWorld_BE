package org.helloworld.gymmate.domain.gym.gymproduct.entity;

import org.helloworld.gymmate.domain.gym.gymproduct.dto.GymProductRequest;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "gym_product")
public class GymProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gym_product_id")
    private Long gymProductId;

    @Column(name = "gym_product_name", nullable = false)
    private String gymProductName;

    @Column(name = "gym_product_fee", nullable = false)
    private Integer gymProductFee;

    @Column(name = "gym_product_month", nullable = false)
    private Integer gymProductMonth;

    @Column(name = "on_sale", nullable = false)
    private Boolean onSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "partner_gym_id")
    private PartnerGym partnerGym;

    // ====== Business Logic ======
	
    public void update(GymProductRequest request) {
        this.gymProductName = request.gymProductName();
        this.gymProductFee = request.gymProductFee();
        this.gymProductMonth = request.gymProductMonth();
    }

}

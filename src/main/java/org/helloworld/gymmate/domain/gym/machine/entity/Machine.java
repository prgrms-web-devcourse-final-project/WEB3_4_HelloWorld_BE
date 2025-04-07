package org.helloworld.gymmate.domain.gym.machine.entity;

import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.machine.dto.MachineRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.Valid;
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
@Table(name = "machine")
public class Machine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "machine_id")
    private Long machineId;

    @Column(name = "machine_name", nullable = false)
    private String machineName;

    @Column(name = "amount", nullable = false)
    private Integer amount;

    @Column(name = "machine_image")
    private String machineImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gym_id")
    private Gym gym;

    // ====== Business Logic ======
	
    public void update(@Valid MachineRequest request) {
        this.machineName = request.machineName();
        this.amount = request.amount();
    }

    public void updateImage(String imageUrl) {
        this.machineImage = imageUrl;
    }
}

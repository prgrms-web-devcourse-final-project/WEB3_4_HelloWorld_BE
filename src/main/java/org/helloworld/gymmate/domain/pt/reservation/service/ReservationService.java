package org.helloworld.gymmate.domain.pt.reservation.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.service.PtProductService;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationByMonthResponse;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationRequest;
import org.helloworld.gymmate.domain.pt.reservation.dto.ReservationResponse;
import org.helloworld.gymmate.domain.pt.reservation.entity.Reservation;
import org.helloworld.gymmate.domain.pt.reservation.mapper.ReservationMapper;
import org.helloworld.gymmate.domain.pt.reservation.repository.ReservationRepository;
import org.helloworld.gymmate.domain.pt.student.service.StudentService;
import org.helloworld.gymmate.domain.user.member.entity.Member;
import org.helloworld.gymmate.domain.user.member.service.MemberService;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.service.TrainerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReservationService {

	private final ReservationRepository reservationRepository;
	private final PtProductService ptProductService;
	private final StudentService studentService;
	private final MemberService memberService;
	private final TrainerService trainerService;

    /*
     예약 생성 로직
     1. ptProduct_id로 ptProduct 객체 조회
     2. ptProduct 정보, reservationRequest 정보, member_id 로 reservation객체 생성&저장
      - ptProduct 객체 에서 ptProductName,TrainerId,ptProductFee 조회
     */
    @Transactional
    public Long register(Long userId, Long ptProductId, ReservationRequest request) {
        // 1) PT 상품 조회
        PtProduct ptProduct = ptProductService.findProductOrThrow(ptProductId);

        // 2) classTime에 존재하는지 확인 -> 프론트에서 예약가능한 시간만 보여주므로, 일단 생략

        // 3) 예약 엔티티 생성
        Reservation reservation = ReservationMapper.toEntity(ptProduct, request, userId);


		// 4) Student 정보 생성
		Member member = memberService.findByUserId(userId);
		Trainer trainer = trainerService.findByUserId(ptProduct.getTrainerId());
		studentService.makeStudent(trainer, member);
		// 5) 저장 및 ID 반환
		return reservationRepository.save(reservation).getReservationId();
	}

    /*
     회원의 예약 삭제 로직
      - param : reservationId
     */
    public void deleteMemberReservation(Long reservationId) {
        //1. 예약 객체 조회
        Reservation reservation = findReservationOrThrow(reservationId);

        //2. 예약 객체 삭제
        reservationRepository.delete(reservation);

    }

    // 예약 조회 메서드 분리
    private Reservation findReservationOrThrow(Long reservationId) {
        return reservationRepository.findById(reservationId)
            .orElseThrow(() -> new BusinessException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    /*
      회원의 예약 목록 조회 로직
       - 매개변수 : 회원 ID
       - 리턴값 : 회원의 예약 목록
     */
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getMemberReservations(
        Long memberId,
        int page,
        int pageSize
    ) {
        // 정렬 조건 고정 (예약 날짜 내림차순)
        Sort sort = Sort.by(Sort.Direction.DESC, "date");

        // 페이징 요청 생성
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // 페이징된 데이터 조회 및 DTO 변환
        return reservationRepository.findByMemberId(memberId, pageable)
            .map(ReservationMapper::toDto);
    }

    /*
      트레이너의 예약 목록 조회 로직
       - 매개변수 : 회원 ID
       - 리턴값 : 회원의 예약 목록
     */
    @Transactional(readOnly = true)
    public Page<ReservationResponse> getTrainerReservations(
        Long trainerId,
        int page,
        int pageSize
    ) {
        // 정렬 조건 고정 (예약 날짜 내림차순)
        Sort sort = Sort.by(Sort.Direction.DESC, "date");

        // 페이징 요청 생성
        Pageable pageable = PageRequest.of(page, pageSize, sort);

        // 페이징된 데이터 조회 및 DTO 변환
        return reservationRepository.findByTrainerId(trainerId, pageable)
            .map(ReservationMapper::toDto);
    }

    /*
        특정 트레이너의 특정 달의 예약 조회
        - param : trainerId, 연도, 달
        - return : ReservationByMonthResponse DTO
     */
    @Transactional(readOnly = true)
    public ReservationByMonthResponse getTrainerReservationsByMonth(Long trainerId, int year, int month) {

        // 1. 해당 트레이너의 모든 예약 객체를 조회
        List<Reservation> reservations = reservationRepository.findByTrainerId(trainerId);

        // 2. 월별 예약 목록을 그룹화
        Map<LocalDate, List<Integer>> reservationsByDate = new HashMap<>();

        for (Reservation reservation : reservations) { // 각 예약 객체별로
            LocalDate reservationDate = reservation.getDate(); //예약 날짜 조회

            if (reservationDate.getYear() == year && reservationDate.getMonthValue() == month) {
                //만약 예약 객체의 연도와 달이 year,month(매개변수로 받은)와 같다면,

                reservationsByDate.computeIfAbsent(reservationDate, k -> new ArrayList<>())
                    // a) 해당날짜(reservationDate)가 키로 존재하지 않는다면,
                    //   해당 키(reservatoinDate)에 새로운ArrayList를 매핑해 리스트 반환
                    // b) 해당날짜가 키로 존재한다면, 기존에 매핑된 리스트 반환
                    .add(reservation.getTime());
                //예약된 시간을 해당날짜(키)의 리스트에 추가
            }
        }

        // 결과를 DTO로 변환하여 반환
        return new ReservationByMonthResponse(reservationsByDate);
    }

    public ReservationResponse updateReservation(Long reservationId, LocalDate newDate, Integer newTime) {
        //1.Reservation객체 조회
        Reservation reservation = findReservationOrThrow(reservationId);

        // 2. 날짜와 시간 변경
        Reservation updatedReservation = reservation.modifyDateAndTime(newDate, newTime);

        // 3. 수정된 예약 저장
        Reservation savedReservation = reservationRepository.save(updatedReservation);

        // 4. ReservationResponse 객체 반환
        return ReservationMapper.toDto(savedReservation);
    }

}

package org.helloworld.gymmate.dummy.trainer;

import java.util.ArrayList;
import java.util.List;

import org.helloworld.gymmate.domain.pt.classtime.dto.ClasstimeRequest;
import org.helloworld.gymmate.domain.pt.classtime.entity.Classtime;
import org.helloworld.gymmate.domain.pt.classtime.mapper.ClasstimeMapper;
import org.helloworld.gymmate.domain.pt.ptproduct.dto.PtProductCreateRequest;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProduct;
import org.helloworld.gymmate.domain.pt.ptproduct.entity.PtProductImage;
import org.helloworld.gymmate.domain.pt.ptproduct.mapper.PtProductMapper;
import org.helloworld.gymmate.domain.pt.ptproduct.repository.PtProductRepository;
import org.helloworld.gymmate.domain.user.trainer.award.entity.Award;
import org.helloworld.gymmate.domain.user.trainer.award.enums.AwardData;
import org.helloworld.gymmate.domain.user.trainer.award.mapper.AwardMapper;
import org.helloworld.gymmate.domain.user.trainer.award.repository.AwardRepository;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TrainerDummyCreate {

    private static final int BATCH_SIZE = 500;
    private final JdbcTemplate jdbcTemplate;
    private final AwardRepository awardRepository;
    private final TrainerRepository trainerRepository;
    private final PtProductRepository ptProductRepository;

    @Transactional
    public void createTrainerDummy() {
        List<Trainer> trainers = trainerRepository.findAll();
        processAwardsForTrainers(trainers);
        processClassTimeForTrainers(trainers);
        processPtProductsForTrainers(trainers);
        List<PtProduct> ptProducts = ptProductRepository.findAll();
        processPtProductImagesForPtProduct(ptProducts);
    }

    private void processAwardsForTrainers(List<Trainer> trainers) {
        if (trainers.isEmpty()) {
            log.debug("저장할 트레이너 데이터가 없습니다.");
            return;
        }
        //db에 insert할 award 리스트
        List<Award> awardsToInsert = new ArrayList<>();

        //트레이너마다 랜덤한 award 3개 생성
        for (Trainer trainer : trainers) {
            // Enum에서 AwardData3개 가져오기
            List<AwardData> selectedAwards = AwardData.getRandomAwards(3);

            for (AwardData awardData : selectedAwards) { // 각 어워드마다

                Award award = AwardMapper.toEntity(awardData, trainer.getTrainerId()); // Award 객체 생성

                awardsToInsert.add(award); // 리스트에 어워드 저장

                //awardRepository.save(award);

            }
        }

        // award INSERT 쿼리 ( year, name, info, trainer 정보 )
        String sql =
            "Insert into award (award_year, award_name, award_info, trainer_id) "
                + "values (?, ?, ? ,?)";
        try {
            jdbcTemplate.batchUpdate(sql, awardsToInsert, BATCH_SIZE, (ps, award) -> {
                //awardsToInsert의 award객체를 꺼내서 sql의 '?'부분의 값 설정
                ps.setString(1, award.getAwardYear());
                ps.setString(2, award.getAwardName());
                ps.setString(3, award.getAwardInfo());
                ps.setLong(4, award.getTrainerId());
            });

            log.debug("총 {}개의 award 데이터 저장 완료!", awardsToInsert.size());

        } catch (Exception e) {

            log.error("award 데이터 저장 중 오류 발생: ", e);
        }

    }

    @Transactional
    public void processClassTimeForTrainers(List<Trainer> trainers) {
        if (trainers.isEmpty()) {
            return;
        }
        List<Classtime> classTimesToInsert = new ArrayList<>();

        // 모든 트레이너에 대해 각각 월~금의 9~20시의 classTime 객체 생성
        // -> ClassTimeToInsert리스트에 저장
        for (Trainer trainer : trainers) {
            Long trainerId = trainer.getTrainerId();
            for (int dayOfWeek = 0; dayOfWeek <= 4; dayOfWeek++) { // 월(0)~금(4)
                for (int time = 9; time <= 20; time++) {            // 9~20시
                    ClasstimeRequest request = new ClasstimeRequest(dayOfWeek, time);
                    Classtime classtime = ClasstimeMapper.toEntity(request, trainerId);
                    classTimesToInsert.add(classtime);
                }
            }
        }
        // class_time INSERT 쿼리
        String sql = "INSERT INTO class_time (day_of_week, time, trainer_id) VALUES (?, ?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, classTimesToInsert, BATCH_SIZE, (ps, classtime) -> {
                ps.setInt(1, classtime.getDayOfWeek());
                ps.setInt(2, classtime.getTime());
                ps.setLong(3, classtime.getTrainerId());
            });

            log.debug("총 {}개의 class_time 데이터 저장 완료!", classTimesToInsert.size());

        } catch (Exception e) {
            log.error("class_time 데이터 저장 중 오류 발생: ", e);
        }

    }

    @Transactional
    public void processPtProductsForTrainers(List<Trainer> trainers) {
        if (trainers.isEmpty()) {
            log.debug("저장할 트레이너 데이터가 없습니다.");
            return;
        }

        List<PtProduct> ptProductsToInsert = new ArrayList<>();

        //Pt 상품을 리스트에 저장
        for (Trainer trainer : trainers) { //각 트레이너마다
            //1. pt상품 생성
            List<PtProductDummy> ptProductDummys = PtProductDummy.getRandomPtProductDummy(
                2); //무작위 ptProduct 정보 가져오기(name,info,fee)
            for (PtProductDummy ptProductDummy : ptProductDummys) {
                PtProductCreateRequest request = new PtProductCreateRequest
                    (ptProductDummy.getPtProductName(), ptProductDummy.getPtProductInfo(),
                        ptProductDummy.getPtProductFee());
                PtProduct ptProduct = PtProductMapper.toEntity(request, trainer.getTrainerId()); //ptProduct객체

                //리스트에 ptProduct객체 추가
                if (ptProduct != null) {
                    ptProductsToInsert.add(ptProduct);
                }
            }

        }

        String sql = "INSERT INTO pt_product (pt_product_name, info, pt_product_fee, trainer_id) VALUES (?, ?, ?, ?)";
        //sql문 실행
        try {
            jdbcTemplate.batchUpdate(sql, ptProductsToInsert, BATCH_SIZE, (ps, ptProduct) -> {
                //awardsToInsert의 award객체를 꺼내서 sql의 '?'부분의 값 설정
                ps.setString(1, ptProduct.getPtProductName());
                ps.setString(2, ptProduct.getInfo());
                ps.setLong(3, ptProduct.getPtProductFee());
                ps.setLong(4, ptProduct.getTrainerId());
            });

            log.debug("총 {}개의 ptProduct 데이터 저장 완료!", ptProductsToInsert.size());

        } catch (Exception e) {

            log.error("ptProduct 데이터 저장 중 오류 발생: ", e);
        }

    }

    private void processPtProductImagesForPtProduct(List<PtProduct> ptProducts) {
        if (ptProducts.isEmpty()) {
            return;
        }

        List<PtProductImage> ptProductImagesToInsert = new ArrayList<>();

        //pt 상품 이미지를 하나씩 리스트에 저장
        for (PtProduct ptProduct : ptProducts) {
            //PT상품 하나당 pt상품 이미지 5개 저장
            List<String> imageUrls = PtProductImageDummy.getRandomPtProductImageUrls(5);
            for (String imageUrl : imageUrls) {
                PtProductImage ptProductImage = PtProductMapper.toEntity(imageUrl, ptProduct);
                ptProductImagesToInsert.add(ptProductImage);
            }
        }

        String sql = "INSERT INTO pt_product_image (url,pt_product_id) VALUES (?, ?)";
        try {
            jdbcTemplate.batchUpdate(sql, ptProductImagesToInsert, BATCH_SIZE, (ps, ptProductImage) -> {
                //awardsToInsert의 award객체를 꺼내서 sql의 '?'부분의 값 설정
                ps.setString(1, ptProductImage.getUrl());
                ps.setLong(2, ptProductImage.getPtProduct().getPtProductId());
            });

            log.debug("총 {}개의 ptProductImage 데이터 저장 완료!", ptProductImagesToInsert.size());

        } catch (Exception e) {

            log.error("ptProductImage 데이터 저장 중 오류 발생: ", e);
        }
    }
}
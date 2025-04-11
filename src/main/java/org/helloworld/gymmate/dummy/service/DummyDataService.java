package org.helloworld.gymmate.dummy.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.helloworld.gymmate.common.exception.BusinessException;
import org.helloworld.gymmate.common.exception.ErrorCode;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.Gym;
import org.helloworld.gymmate.domain.gym.gyminfo.entity.GymImage;
import org.helloworld.gymmate.domain.gym.gyminfo.repository.GymImageRepository;
import org.helloworld.gymmate.domain.gym.gyminfo.repository.GymRepository;
import org.helloworld.gymmate.domain.gym.gymproduct.entity.GymProduct;
import org.helloworld.gymmate.domain.gym.gymproduct.repository.GymProductRepository;
import org.helloworld.gymmate.domain.gym.partnergym.entity.PartnerGym;
import org.helloworld.gymmate.domain.gym.partnergym.repository.PartnerGymRepository;
import org.helloworld.gymmate.domain.user.enums.GenderType;
import org.helloworld.gymmate.domain.user.enums.SocialProviderType;
import org.helloworld.gymmate.domain.user.enums.UserType;
import org.helloworld.gymmate.domain.user.trainer.entity.Trainer;
import org.helloworld.gymmate.domain.user.trainer.repository.TrainerRepository;
import org.helloworld.gymmate.dummy.entity.GymDummyText;
import org.helloworld.gymmate.dummy.entity.PartnerGymDummyImage;
import org.helloworld.gymmate.dummy.entity.TrainerDummyImage;
import org.helloworld.gymmate.dummy.entity.TrainerDummyText;
import org.helloworld.gymmate.security.oauth.entity.Oauth;
import org.helloworld.gymmate.security.oauth.repository.OauthRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DummyDataService {

    private final OauthRepository oauthRepository;
    private final TrainerRepository trainerRepository;
    private final GymRepository gymRepository;
    private final PartnerGymRepository partnerGymRepository;
    private final GymProductRepository gymProductRepository;
    private final GymImageRepository gymImageRepository;
    private final int GYM_COUNT = 500;
    // 선택된 PartnerGym 후보 Gym ID 목록 (크기 1000)
    private List<Long> selectedPartnerGymIds;

    @Transactional
    public void generateDummyData() {
        int gymCount = (int)gymRepository.count();
        final int normalTrainersCount = GYM_COUNT * 2; // 일반 트레이너 수 고정
        final int totalTrainers = GYM_COUNT * 3;        // 총 트레이너 수 (일반 2000 + 사장 1000)

        // 모든 gym ID를 1부터 gymCount까지 생성
        // TODO: 변경 필요
        List<Long> allGymIds = new ArrayList<>();
        for (long i = 1; i <= gymCount; i++) {
            allGymIds.add(i);
        }
        Random random = new Random();
        // 무작위로 섞은 후, 제휴 헬스장 후보로 1000개 Gym ID 선택
        Collections.shuffle(allGymIds, random);
        selectedPartnerGymIds = new ArrayList<>(allGymIds.subList(0, Math.min(GYM_COUNT, allGymIds.size())));

        // 일반 트레이너의 Gym 할당을 위해, 각 partner gym ID가 두 번씩 나타나는 리스트 생성 (2000개)
        List<Long> partnerGymCandidateIds = new ArrayList<>();
        for (Long id : selectedPartnerGymIds) {
            partnerGymCandidateIds.add(id);
            partnerGymCandidateIds.add(id);
        }
        Collections.shuffle(partnerGymCandidateIds, random);

        Set<String> usedPhoneNumbers = new HashSet<>();

        // 가능한 은행 목록
        String[] bankOptions = {"국민은행", "신한은행", "카카오뱅크", "우리은행", "하나은행"};

        for (int i = 0; i < totalTrainers; i++) {
            Oauth savedOAuth = oauthRepository.save(
                Oauth.builder()
                    .provider(SocialProviderType.GOOGLE)
                    .providerId(generateRandomAlphanumeric(random))
                    .userType(UserType.TRAINER)
                    .build()
            );

            // 성별 및 이름 생성 (성+이름)
            GenderType gender = GenderType.valueOf(random.nextBoolean() ? "MALE" : "FEMALE");
            String fullName;
            if (gender == GenderType.MALE) {
                List<String> surnames = TrainerDummyText.MALE_SURNAMES;
                List<String> givenNames = TrainerDummyText.MALE_GIVEN_NAMES;
                String surname = surnames.get(random.nextInt(surnames.size()));
                String givenName = givenNames.get(random.nextInt(givenNames.size()));
                fullName = surname + givenName;
            } else {
                List<String> surnames = TrainerDummyText.FEMALE_SURNAMES;
                List<String> givenNames = TrainerDummyText.FEMALE_GIVEN_NAMES;
                String surname = surnames.get(random.nextInt(surnames.size()));
                String givenName = givenNames.get(random.nextInt(givenNames.size()));
                fullName = surname + givenName;
            }

            String email = "trainer" + (i + 1) + "@example.com";
            String phone = generateUniquePhone(usedPhoneNumbers, random);
            String bank = bankOptions[random.nextInt(bankOptions.length)];
            String account = generateRandomAccountNumber(random);

            Trainer trainer;
            if (i < normalTrainersCount) {
                // 일반 트레이너: gym 할당, isOwner false
                // TODO : Award 등 trainer 정보 추가 (저도 여기에 넣는게 확실하지 않아서 유진님 코드 봐야 알 것 같습니다!)
                Gym gym = gymRepository.findById(partnerGymCandidateIds.get(i))
                    .orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));
                trainer = Trainer.builder()
                    .trainerName(fullName)
                    .email(email)
                    .phoneNumber(phone)
                    .genderType(gender)
                    .bank(bank)
                    .account(account)
                    .isOwner(false)
                    .businessNumber(null)
                    .gym(gym)
                    .build();
            } else {
                // 사장: gym 미할당, isOwner true, 사업자등록번호 및 businessDate 생성
                String dateStr = random.nextInt(2023 - 2000 + 1) + 2000 +
                    String.format("%02d", random.nextInt(12) + 1) +
                    String.format("%02d", random.nextInt(28) + 1);
                LocalDate businessDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
                trainer = Trainer.builder()
                    .trainerName(fullName)
                    .email(email)
                    .phoneNumber(phone)
                    .genderType(gender)
                    .bank(bank)
                    .account(account)
                    .isOwner(true)
                    .businessNumber(generateRandomNumericString(random))
                    .businessDate(businessDate)
                    .build();
            }
            // 기타 공통 필드: intro, career, field, profileUrl
            String intro = TrainerDummyText.INTRO_TEXTS.get(random.nextInt(TrainerDummyText.INTRO_TEXTS.size()));
            String career = TrainerDummyText.CAREER_TEXTS.get(random.nextInt(TrainerDummyText.CAREER_TEXTS.size()));
            String field = (gender == GenderType.MALE) ? "헬스" : List.of("요가", "필라테스").get(random.nextInt(2));
            String profileUrl;
            if (gender == GenderType.MALE) {
                profileUrl = TrainerDummyImage.MALE_IMAGE_URLS.get(
                    random.nextInt(TrainerDummyImage.MALE_IMAGE_URLS.size()));
            } else {
                profileUrl = TrainerDummyImage.FEMALE_IMAGE_URLS.get(
                    random.nextInt(TrainerDummyImage.FEMALE_IMAGE_URLS.size()));
            }
            trainer = Trainer.builder()
                .trainerName(trainer.getTrainerName())
                .email(trainer.getEmail())
                .phoneNumber(trainer.getPhoneNumber())
                .genderType(trainer.getGenderType())
                .bank(trainer.getBank())
                .account(trainer.getAccount())
                .isOwner(trainer.getIsOwner())
                .businessNumber(trainer.getBusinessNumber())
                .gym(trainer.getGym())
                .isCheck(true)
                .isAccountNonLocked(true)
                .cash((long)(random.nextInt(1000000) + 1))
                .score(roundDouble(3.5 + random.nextDouble() * 1.5))
                .intro(intro)
                .career(career)
                .field(field)
                .profileUrl(profileUrl)
                .additionalInfoCompleted(true)
                .oauth(savedOAuth)
                .build();

            trainerRepository.save(trainer);
        }
    }

    // 제휴 헬스장 1000개 생성
    @Transactional
    public void generatePartnerGyms() {
        // 제휴 헬스장 소유자(사장)만 사용: 1000명 필요
        List<Trainer> ownerTrainers = trainerRepository.findByIsOwnerTrue();
        if (ownerTrainers.size() < GYM_COUNT) {
            throw new IllegalStateException("Owner trainer 수가 부족합니다. 필요한 1000개, 실제: " + ownerTrainers.size());
        }
        Collections.shuffle(ownerTrainers, new Random());
        List<Trainer> selectedOwners = ownerTrainers.subList(0, GYM_COUNT);

        if (selectedPartnerGymIds == null || selectedPartnerGymIds.size() < GYM_COUNT) {
            throw new BusinessException(ErrorCode.GYM_NOT_FOUND); // 적어도 1000개의 PartnerGym 후보
        }

        Random random = new Random();

        for (int i = 0; i < GYM_COUNT; i++) {
            Gym gym = gymRepository.findById(selectedPartnerGymIds.get(i))
                .orElseThrow(() -> new BusinessException(ErrorCode.GYM_NOT_FOUND));

            // 제휴 헬스장으로 업데이트
            gym.updatePartner(true);

            // PartnerGym용 이미지 5장 추가 (PartnerGymDummyImage의 30개 URL 중 랜덤 5개)
            List<String> imageUrlCandidates = new ArrayList<>(PartnerGymDummyImage.PARTNER_GYM_IMAGE_URLS);
            Collections.shuffle(imageUrlCandidates, random);
            List<String> selectedImageUrls = imageUrlCandidates.subList(0, 5);
            for (String imageUrl : selectedImageUrls) {
                GymImage gymImage = GymImage.builder()
                    .url(imageUrl)
                    .gym(gym)
                    .build();
                gymImageRepository.save(gymImage);
            }
            gymRepository.save(gym);

            // PartnerGym 생성: 소유자와 gym 연결
            PartnerGym partnerGym = PartnerGym.builder()
                .ownerId(selectedOwners.get(i).getTrainerId())
                .gym(gym)
                .build();

            // 운영시간 설정
            // 시작시간: 허용된 값 "00:00", "06:00", "07:00", "08:00"
            List<String> allowedStartTimes = List.of("00:00", "06:00", "07:00", "08:00");
            // 종료시간: 일반은 "22:00", "23:00", "24:00" 중 선택, 단 "00:00" 시작이면 무조건 "24:00"
            List<String> allowedEndTimes = List.of("22:00", "23:00", "24:00");
            String startTime = allowedStartTimes.get(random.nextInt(allowedStartTimes.size()));
            String endTime =
                "00:00".equals(startTime) ? "24:00" : allowedEndTimes.get(random.nextInt(allowedEndTimes.size()));

            String gymIntro = GymDummyText.INTRO_TEXTS.get(random.nextInt(GymDummyText.INTRO_TEXTS.size()));
            gym.updateInfo(startTime, endTime, gymIntro);
            gymRepository.save(gym);
            partnerGymRepository.save(partnerGym);

            // GymProduct 생성
            int[] possibleMonths = {1, 3, 6, 12};
            int selectedMonth = possibleMonths[random.nextInt(possibleMonths.length)];
            String productName = selectedMonth + "개월 이용권";
            int[] possibleFeeOptions = {30000, 35000, 40000, 45000, 50000};
            int baseFee = possibleFeeOptions[random.nextInt(possibleFeeOptions.length)];
            int fee = baseFee * selectedMonth;
            GymProduct gymProduct = GymProduct.builder()
                .gymProductName(productName)
                .gymProductFee(fee)
                .gymProductMonth(selectedMonth)
                .onSale(true)
                .partnerGym(partnerGym)
                .build();

            gymProductRepository.save(gymProduct);
        }
    }

    // 헬퍼 메서드들

    private String generateRandomAlphanumeric(Random random) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private String generateUniquePhone(Set<String> usedPhones, Random random) {
        String phone;
        do {
            StringBuilder sb = new StringBuilder("010");
            for (int i = 0; i < 8; i++) {
                sb.append(random.nextInt(10));
            }
            phone = sb.toString();
        } while (usedPhones.contains(phone));
        usedPhones.add(phone);
        return phone;
    }

    private String generateRandomNumericString(Random random) {
        StringBuilder sb = new StringBuilder(11);
        for (int i = 0; i < 11; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private String generateRandomAccountNumber(Random random) {
        StringBuilder sb = new StringBuilder(10);
        for (int i = 0; i < 10; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    private double roundDouble(double value) {
        long factor = (long)Math.pow(10, 2);
        return (double)Math.round(value * factor) / factor;
    }
}

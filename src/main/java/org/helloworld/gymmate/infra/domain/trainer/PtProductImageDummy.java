package org.helloworld.gymmate.infra.domain.trainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PtProductImageDummy {
    public static final List<String> PT_PRODUCT_IMAGE_URLS = List.of(
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/8789c776-3f40-4172-9ffe-fed3230e49b1.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/67086ad4-d4ec-42fe-8fcd-3603a89d3c96.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/d736d0e7-b2af-4e9d-86e3-f17402d9ede6.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/db76e3f4-ac5c-466c-b86e-d3448edf9c8a.jpeg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/d5068bf1-d9bd-4167-926c-4a14394b5019.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/a143c6ce-3ddf-4914-9bc6-99f095bc2ec4.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/a029391a-0e96-4046-8619-30eed32d5c3d.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/d0886950-0d55-497c-8fec-2b87d8a4974b.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/b48b2ba3-b107-44a0-9c4a-309e65dfabc3.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/41d475dd-2ae2-4bb2-962f-eb3fffa36b1d.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/7b5017aa-7c47-45d0-a82c-371ebfc8ad4a.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/ptProduct/20250410/39875ad8-799a-488e-82a3-eb8b53c81b68.jpg"
    );

    // 랜덤 5개 가져오는 메서드
    public static List<String> getRandomPtProductImageUrls(int count) {
        List<String> shuffled = new ArrayList<>(PT_PRODUCT_IMAGE_URLS);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }

}

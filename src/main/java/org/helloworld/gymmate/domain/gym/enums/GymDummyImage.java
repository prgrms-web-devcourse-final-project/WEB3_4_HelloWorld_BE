package org.helloworld.gymmate.domain.gym.enums;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GymDummyImage {
    public static final List<String> IMAGE_URLS = List.of(
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/35030f6c-acc7-4b27-b6c4-5f45a8951b61.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/bbe62f1f-776b-4e5b-9996-ebb09edb713f.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/2e6fdde2-5a06-4d19-b685-e2bc992c24af.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/4a3409fc-459e-4063-b330-26794f3c165f.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/90afe230-85fe-48b4-9662-3267a31046bf.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/9e306656-8696-4819-8d9f-36bcc2f2aa80.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/394715cf-1e93-4c93-947d-68574305e41b.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/b7325cd8-75c4-40d2-a9af-a9c7d4d9527c.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/bb0807d6-1e7c-4c55-ae2b-6b47c44a96c5.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/ee0fb96e-e38f-4835-a2cb-2630fd56bf48.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/c4e47e49-6f21-4033-bef9-6653d6ffc6a2.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/883ab96a-7f9b-43f2-a300-f50c795d0915.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/5687871b-5b08-449f-b263-45846a402699.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/a62e1a7e-2280-4d8a-831d-bbb4400a3562.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/6bb80083-3077-47c6-bc4b-f59a43e05f49.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/13f53d90-a625-460a-aebe-351b4a519e62.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/53632ec3-6fb0-4288-a30a-e4d664f80b0d.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/06ca87b7-d684-4b38-855a-bf9fa6036573.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/06ca87b7-d684-4b38-855a-bf9fa6036573.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/e600af84-8d19-4466-aa72-a6c6a677abd5.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/f1198716-87fe-496f-b41a-eae8df5f584d.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/cb95fa06-f65e-418f-a5b7-98fb7ab3d211.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/dfebe693-fd83-4318-9218-c92dba1c5882.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/1dd1a5a1-5dc8-4645-a567-e0da7bae25f5.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/bdea64a1-46a1-4cde-95c9-fbd1137d48bc.jpg",
        "https://devcouse4-team12-bucket.s3.ap-northeast-2.amazonaws.com/gym/20250408/0b26b74f-59ad-402f-94f5-9657e8c7d4b8.jpg"
    );

    public static List<String> getRandomImageUrls(int count) {
        List<String> shuffled = new ArrayList<>(IMAGE_URLS);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, Math.min(count, shuffled.size()));
    }
}
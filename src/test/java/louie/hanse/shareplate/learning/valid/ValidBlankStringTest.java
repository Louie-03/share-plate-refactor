package louie.hanse.shareplate.learning.valid;

import static net.datafaker.transformations.Field.field;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import louie.hanse.shareplate.core.share.domain.ShareType;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Bool;
import net.datafaker.providers.base.DateAndTime;
import net.datafaker.providers.base.Internet;
import net.datafaker.providers.base.Name;
import net.datafaker.providers.food.Food;
import net.datafaker.service.RandomService;
import net.datafaker.transformations.CsvTransformer;
import net.datafaker.transformations.Schema;
import org.junit.jupiter.api.Test;
import org.springframework.util.ObjectUtils;

class ValidBlankStringTest {

    Faker faker = new Faker(Locale.KOREA);

    String emptyString = "";
    String blankString = " ";

    @Test
    void stringIsEmpty() {
        assertThat(emptyString.isEmpty()).isTrue();
        assertThat(blankString.isEmpty()).isFalse();
    }

    @Test
    void stringIsBlank() {
        assertThat(emptyString.isBlank()).isTrue();
        assertThat(blankString.isBlank()).isTrue();
    }

    @Test
    void objectUtilsIsEmpty() {
        assertThat(ObjectUtils.isEmpty(emptyString)).isTrue();
        assertThat(ObjectUtils.isEmpty(blankString)).isFalse();
        assertThat(ObjectUtils.isEmpty(null)).isTrue();
    }

    //    ex : 서울 강남구 강남대로 지하 396
//             city
//    fullAddress : Suite 134 41111 중계읍, 태안구, 전북 06706
//    fullAddress : 917 안양읍, 목포군, 경북 36517
//    fullAddress : 8993 창읍, 나주군, 인천 45256
//    fullAddress : Suite 593 19533 행신동, 제주시, 세종 70173
//
//    StreetAddress : 지번주소, 아파트, 동호수
//    Apartment : 아파트명
//    Suite : 동
//    Unit : 호
//    building : 건물명
//    floor : 층수
    @Test
    void name() {
        LocalTime start = LocalTime.now();
        Address address = faker.address();
        for (int i = 0; i < 1000000; i++) {
            address.fullAddress();
//            System.out.println(address.fullAddress());
        }
        System.out.println(LocalTime.now().getSecond() - start.getSecond());
//        System.out.println("city : " + address.city());
//        System.out.println("cityName : " + address.cityName());
//        System.out.println("cityPrefix : " + address.cityPrefix());
//        System.out.println("citySuffix : " + address.citySuffix());
//        System.out.println("streetAddress : " + address.streetAddress());
//        System.out.println("streetAddress(true) : " + address.streetAddress(true));
//        System.out.println("streetPrefix : " + address.streetPrefix());
//        System.out.println("streetSuffix : " + address.streetSuffix());
//        System.out.println("fullAddress : " + address.fullAddress());
//        System.out.println("mailBox : " + address.mailBox());
//        System.out.println("buildingNumber : " + address.buildingNumber());
//        System.out.println("latitude : " + address.latitude());
//        System.out.println("longitude : " + address.longitude());
//        System.out.println("latLon : " + address.latLon());
//        System.out.println("postcode : " + address.postcode());
    }

    @Test
    void name2() {
        // transformer could be the same for both
        CsvTransformer<Name> transformer =
            CsvTransformer.<Name>builder().header(true).separator(",").build();
        // Schema for from scratch
        Schema<Name, String> fromScratch =
            Schema.of(field("firstName", () -> faker.name().firstName()),
                field("lastname", () -> faker.name().lastName()));
        System.out.println(transformer.generate(fromScratch, 2));
        System.out.println("==============");
        // POSSIBLE OUTPUT
        // "first_name" ; "last_name"
        // "Kimberely" ; "Considine"
        // "Mariela" ; "Krajcik"
        // ----------------------
        // Schema for transformations
        Schema<Name, String> schemaForTransformations =
            Schema.of(field("firstName", Name::firstName),
                field("lastname", Name::lastName));
        // Here we pass a collection of Name objects and extract first and lastnames from each element
        System.out.println(transformer.generate(
            faker.collection(faker::name).maxLen(5).generate(), schemaForTransformations));
        // POSSIBLE OUTPUT
        // "first_name" ; "last_name"
        // "Kimberely" ; "Considine"
        // "Mariela" ; "Krajcik"
    }

    @Test
    void name3() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(true).separator(",").build();
        Schema<String, String> fromScratch =
            Schema.of(field("firstName", () -> faker.name().firstName()),
                field("lastname", () -> faker.name().lastName()));
        System.out.println(transformer.generate(fromScratch, 2));
        System.out.println("==============");
    }

    @Test
    void name4() {
        String NEWLINE = System.lineSeparator(); // 줄바꿈(\n)
        try {
            File file = new File("./csv_demo.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            bw.write("번호,이름,지역");
            bw.write(NEWLINE);

            bw.write("1,김철수,서울");
            bw.write("\n");

            bw.write("2,김영희,경기");
            bw.write("\r\n");

            bw.write("3,이철희,경북");

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void name5() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        Schema<String, String> fromScratch =
            Schema.of(
                field("firstName", () -> faker.name().firstName()),
                field("lastname", () -> faker.name().lastName())
            );
        String generate = transformer.generate(fromScratch, 2);
        try {
            File file = new File("./hi.csv");
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(generate);

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void name6() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        Internet internet = faker.internet();

        IdSequence idSequence = new IdSequence();
        Schema<String, Object> memberSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("email", () -> internet.emailAddress()),
            field("nickname", () -> faker.name().fullName()),
            field("profile_image_url", () -> internet.image()),
            field("refresh_token", () -> null),
            field("thumbnail_image_url", () -> internet.image())
        );
        String memberCsvString = transformer.generate(memberSchema, 100000);

        idSequence.init();
        FoodSequence foodSequence = new FoodSequence();
        Bool bool = faker.bool();
        DateAndTime dateAndTime = faker.date();
        RandomService randomService = faker.random();
        Address address = faker.address();
        Schema<String, Object> shareSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("cancel", () -> bool.bool()),
            field("closed_date_time", () -> dateAndTime.future(30, TimeUnit.DAYS)),
            field("created_date_time", () -> LocalDateTime.now()),
            field("description", () -> foodSequence.getNextDish()),
            field("latitude", () -> randomService.nextDouble(33.1, 38.45)),
            field("location", () -> address.fullAddress()),
            field("location_guide", () -> address.fullAddress()),
            field("location_negotiation", () -> bool.bool()),
            field("longitude", () -> randomService.nextDouble(125.06666667, 131.87222222)),
            field("original_price", () -> randomService.nextInt(1000, 1000000)),
            field("price", () -> randomService.nextInt(1000, 1000000)),
            field("price_negotiation", () -> bool.bool()),
            field("recruitment", () -> randomService.nextInt(2, 10)),
            field("title", () -> foodSequence.getDish()),
            field("type", () -> bool.bool() ? ShareType.DELIVERY : ShareType.INGREDIENT),
            field("writer_id", () -> idSequence.getSequence())
        );

        String shareCsvString = transformer.generate(shareSchema, 10);
        System.out.println(shareCsvString);
    }

    @Test
    void bool() {
        Bool bool = faker.bool();
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
        System.out.println(bool.bool());
    }

    @Test
    void DateAndTime() {
        DateAndTime dateAndTime = faker.date();
        // 현재 시간 이후의 시간 생성, 매개변수로 입력한 시간/단위가 반환값의 최대값이다.
        for (int i = 0; i < 10; i++) {
            System.out.println(dateAndTime.future(1, TimeUnit.HOURS));
        }
    }

    @Test
    void food() {
        Food food = faker.food();
        System.out.println("dish : " + food.dish());
        System.out.println("fruit : " + food.fruit());
        System.out.println("ingredient : " + food.ingredient());
        System.out.println("measurement : " + food.measurement());
        System.out.println("spice : " + food.spice());
        System.out.println("sushi : " + food.sushi());
        System.out.println("vegetable : " + food.vegetable());
    }

    @Test
    void latitudeAndLongitude() {
        for (int i = 0; i < 1000; i++) {
            double latitude = faker.random().nextDouble(33.1, 38.45);
            System.out.println(latitude);
            assertThat(33.1 > latitude || latitude > 38.45).isFalse();
        }
        System.out.println("=========");
        for (int i = 0; i < 1000; i++) {
            double longitude = faker.random().nextDouble(125.06666667, 131.87222222);
            System.out.println(longitude);
            assertThat(125.06666667 > longitude || longitude > 131.87222222).isFalse();
        }
    }

    @Test
    void nextInt() {
        RandomService randomService = faker.random();
        for (int i = 0; i < 10000; i++) {
            Integer nextInt = randomService.nextInt(1000, 1000000);
            System.out.println(nextInt);
            assertThat(nextInt).isGreaterThanOrEqualTo(1000);
        }
    }

    @Test
    void convertTimestampToLocalDateTime() {
        // LocalDateTime to Timestamp
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
        assertThat(timestamp.toLocalDateTime()).isInstanceOf(LocalDateTime.class);
    }

    @Test
    void createMemberCsvFile() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        Internet internet = faker.internet();

        IdSequence idSequence = new IdSequence();
        Schema<String, Object> memberSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("email", () -> internet.emailAddress()), // TODO: 2023/04/23 이메일 주소 영어로 나오도록 수정
            field("nickname", () -> faker.name().fullName().replace(" ", "")),
            field("profile_image_url", () -> internet.image()),
            field("refresh_token", () -> null),
            field("thumbnail_image_url", () -> internet.image())
        );
        String memberCsvString = transformer.generate(memberSchema, 100);
        System.out.println(memberCsvString);
        createCsvFile("./members.csv", memberCsvString);
    }

    @Test
    void createShareCsvFile() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();

        IdSequence idSequence = new IdSequence();
        FoodSequence foodSequence = new FoodSequence();
        Bool bool = faker.bool();
        DateAndTime dateAndTime = faker.date();
        RandomService randomService = faker.random();
        Address address = faker.address();
        Schema<String, Object> shareSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("cancel", () -> bool.bool() ? 1 : 0),
            field("closed_date_time", () -> dateAndTime.future(30, TimeUnit.DAYS)),
            field("created_date_time", () -> LocalDateTime.now()),
            field("description", () -> foodSequence.getNextDish()),
            field("latitude", () -> randomService.nextDouble(33.1, 38.45)),
            field("location", () -> address.fullAddress().replace(",", "")),
            field("location_guide", () -> address.fullAddress().replace(",", "")),
            field("location_negotiation", () -> bool.bool() ? 1 : 0),
            field("longitude", () -> randomService.nextDouble(125.06666667, 131.87222222)),
            field("original_price", () -> randomService.nextInt(1000, 1000000)),
            field("price", () -> randomService.nextInt(1000, 1000000)),
            field("price_negotiation", () -> bool.bool() ? 1 : 0),
            field("recruitment", () -> randomService.nextInt(2, 10)),
            field("title", () -> foodSequence.getDish()),
            field("type", () -> bool.bool() ? ShareType.DELIVERY : ShareType.INGREDIENT),
            field("writer_id", () -> idSequence.getSequence())
        );

        String shareCsvString = transformer.generate(shareSchema, 100);
        System.out.println(shareCsvString);
        createCsvFile("./share.csv", shareCsvString);
    }

    @Test
    void createShareImageCsvFile() {
        // 쉐어에 대한 이미지를 1~5개 생성하도록 구현
        // ID를 지정하지 않고 csv 파일로 insert 할 수 있는지 시도해보기
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        StringBuilder sb = new StringBuilder();

        Internet internet = faker.internet();
        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            Schema<String, Object> shareImageSchema = Schema.of(
                field("image_url", () -> internet.image()),
                field("share_id", () -> finalI)
            );
            String shareImageCsvString = transformer.generate(shareImageSchema, faker.random().nextInt(1, 5));
            sb.append(shareImageCsvString).append("\n");
        }

        System.out.println(sb);
        createCsvFile("./share_image.csv", sb.toString());
    }

    // TODO: 샘플 데이터의 개수 상수처리하기
    @Test
    void createEntryCsvFile() {
//        entry
//        쉐어 작성자 또는 이미 쉐어에 참여한 회원은 또 쉐어에 참여할 수 없다.
//        쉐어 모집 인원을 넘지 않아야 한다.
//        작성한 쉐어에는 꼭 작성자가 참여하고 있어야 한다.
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        StringBuilder sb = new StringBuilder();

        Internet internet = faker.internet();
        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            Schema<String, Object> entrySchema = Schema.of(
                field("member_id", () -> finalI),
                field("share_id", () -> finalI)
            );
            String entryCsvString = transformer.generate(entrySchema, 1);
            sb.append(entryCsvString).append("\n");
        }

        for (int i = 1; i <= 100; i++) {
            int finalI = i;
            Schema<String, Object> entrySchema = Schema.of(
                field("member_id", () -> finalI),
                field("share_id", () -> finalI)
            );
            String entryCsvString = transformer.generate(entrySchema, 1);
            sb.append(entryCsvString).append("\n");
        }

        System.out.println(sb);
        createCsvFile("./share_image.csv", sb.toString());
    }

    @Test
    void address() {
        Address address = faker.address();
        System.out.println(address.streetName());
        System.out.println(address.streetAddressNumber());
        System.out.println(address.streetAddress());
        System.out.println(address.streetAddress());
        System.out.println(address.secondaryAddress());
        System.out.println(address.zipCode());
        System.out.println(address.postcode());
        System.out.println(address.zipCodePlus4());
        System.out.println(address.streetSuffix());
        System.out.println(address.streetPrefix());
        System.out.println(address.citySuffix());
        System.out.println(address.cityPrefix());
        System.out.println(address.city());
        System.out.println(address.cityName());
        System.out.println(address.state());
        System.out.println(address.stateAbbr());
        System.out.println(address.latitude());
        System.out.println(address.longitude());
        System.out.println(address.latLon());
        System.out.println(address.latLon());
        System.out.println(address.lonLat());
        System.out.println(address.lonLat());
        System.out.println(address.timeZone());
        System.out.println(address.country());
        System.out.println(address.countryCode());
        System.out.println(address.buildingNumber());
        System.out.println(address.fullAddress());
        System.out.println(address.mailBox());
    }

    @Test
    void nextInt2() {
        RandomService randomService = faker.random();
        for (int i = 0; i < 10000; i++) {
            Integer nextInt = randomService.nextInt(1, 5);
            assertThat(nextInt).isGreaterThanOrEqualTo(1);
            assertThat(nextInt).isLessThanOrEqualTo(5);
        }
    }

    void createCsvFile(String filename, String contents) {
        contents = contents.replace("\"", "");
        try {
            File file = new File(filename);
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(contents);

            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class IdSequence {

        private long sequence;

        public void init() {
            sequence = 0;
        }

        public long getNextSequence() {
            return ++sequence;
        }

        public long getSequence() {
            return sequence;
        }
    }

    static class FoodSequence {

        private final Food food = new Faker().food();

        private String dish;

        public String getNextDish() {
            return dish = food.dish();
        }

        public String getDish() {
            return dish;
        }
    }

}


















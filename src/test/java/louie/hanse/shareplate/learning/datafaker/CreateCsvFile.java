package louie.hanse.shareplate.learning.datafaker;

import static net.datafaker.transformations.Field.field;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import louie.hanse.shareplate.core.chatroom.domain.ChatRoomType;
import louie.hanse.shareplate.core.share.domain.ShareType;
import net.datafaker.Faker;
import net.datafaker.providers.base.Address;
import net.datafaker.providers.base.Bool;
import net.datafaker.providers.base.DateAndTime;
import net.datafaker.providers.base.Internet;
import net.datafaker.providers.food.Food;
import net.datafaker.service.RandomService;
import net.datafaker.transformations.CsvTransformer;
import net.datafaker.transformations.Schema;
import org.junit.jupiter.api.Test;

class CreateCsvFile {

    public static final int LIMIT = 1000000;
    private Faker faker = new Faker(Locale.KOREA);
    private Internet internet = faker.internet();
    private Bool bool = faker.bool();
    private DateAndTime dateAndTime = faker.date();
    private RandomService randomService = faker.random();
    private Address address = faker.address();

    // TODO: 이메일 주소 영어로 나오도록 수정
    @Test
    void create() {
        CsvTransformer<String> transformer =
            CsvTransformer.<String>builder().header(false).separator(",").build();
        Internet internet = faker.internet();

        IdSequence idSequence = new IdSequence();
        Schema<String, Object> memberSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("email", () -> internet.emailAddress()),
            field("nickname", () -> faker.name().fullName().replace(" ", "")),
            field("profile_image_url", () -> internet.image()),
            field("refresh_token", () -> null),
            field("thumbnail_image_url", () -> internet.image())
        );
        String memberCsvString = transformer.generate(memberSchema, LIMIT);
        createCsvFile("./members.csv", memberCsvString);

        idSequence.init();
        FoodSequence foodSequence = new FoodSequence();
        Map<Integer, Integer> recruitmentMap = new HashMap<>();
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
            field("recruitment", () -> {
                Integer recruitment = randomService.nextInt(2, 10);
                recruitmentMap.put(idSequence.getSequence(), recruitment);
                return recruitment;
            }),
            field("title", () -> foodSequence.getDish()),
            field("type", () -> bool.bool() ? ShareType.DELIVERY : ShareType.INGREDIENT),
            field("writer_id", () -> idSequence.getSequence())
        );

        String shareCsvString = transformer.generate(shareSchema, LIMIT);
        createCsvFile("./share.csv", shareCsvString);

//        ShareImage
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= LIMIT; i++) {
            int finalI = i;
            Schema<String, Object> shareImageSchema = Schema.of(
                field("image_url", () -> internet.image()),
                field("share_id", () -> finalI)
            );
            Integer limit = randomService.nextInt(1, 5);
            String shareImageCsvString = transformer.generate(shareImageSchema, limit);
            sb.append(shareImageCsvString).append("\n");
        }
        createCsvFile("./share_image.csv", sb.toString());

//        Entry
//        쉐어 작성자 또는 이미 쉐어에 참여한 회원은 또 쉐어에 참여할 수 없다.
//        쉐어 모집 인원을 넘지 않아야 한다.
//        작성한 쉐어에는 꼭 작성자가 참여하고 있어야 한다.
        sb.setLength(0);
        Map<Integer, List<Integer>> entryMap = new HashMap<>();
        for (int i = 1; i <= LIMIT; i++) {
            int finalI = i;
            entryMap.put(i, new ArrayList<>());
            entryMap.get(i).add(i);
            Schema<String, Object> entrySchema = Schema.of(
                field("member_id", () -> finalI),
                field("share_id", () -> finalI)
            );
            String entryCsvString = transformer.generate(entrySchema, 1);
            sb.append(entryCsvString).append("\n");
        }

        for (int i = 1; i <= LIMIT; i++) {
            int finalI = i;
            Schema<String, Object> entrySchema = Schema.of(
                field("member_id", () -> {
                    int memberId = getNotEntryMemberId(finalI, entryMap);
                    entryMap.get(finalI).add(memberId);
                    return memberId;
                }),
                field("share_id", () -> finalI)
            );
            Integer limit = randomService.nextInt(1, recruitmentMap.get(i) - 1);
            String entryCsvString = transformer.generate(entrySchema, limit);
            sb.append(entryCsvString).append("\n");
        }
        createCsvFile("./entry.csv", sb.toString());

//        ChatRoom
        idSequence.init();
        Schema<String, Object> entryChatRoomSchema = Schema.of(
            field("id", () -> idSequence.getNextSequence()),
            field("share_id", () -> idSequence.getSequence()),
            field("type", () -> ChatRoomType.ENTRY)
        );
        String entryChatRoomCsvString = transformer.generate(entryChatRoomSchema, LIMIT);

        createCsvFile("./chat_room.csv", entryChatRoomCsvString);

//        ChatRoomMember
        sb.setLength(0);
        for (Integer key : entryMap.keySet()) {
            for (Integer memberId : entryMap.get(key)) {
                Schema<String, Object> chatRoomMemberSchema = Schema.of(
                    field("chat_room_id", () -> key),
                    field("member_id", () -> memberId)
                );
                sb.append(transformer.generate(chatRoomMemberSchema, 1)).append("\n");
            }
        }
        createCsvFile("./chat_room_member.csv", sb.toString());

//        Hashtag
        sb.setLength(0);
        for (int i = 1; i <= LIMIT; i++) {
            for (int j = 1; j <= randomService.nextInt(0, 5); j++) {
                int shareId = i;
                int finalJ = j;
                Schema<String, Object> hashtagSchema = Schema.of(
                    field("contents", () -> "해시태그" + finalJ),
                    field("share_id", () -> shareId)
                );
                sb.append(transformer.generate(hashtagSchema, 1)).append("\n");
            }
        }
        createCsvFile("./hashtag.csv", sb.toString());
    }

    private int getNotEntryMemberId(int shareId, Map<Integer, List<Integer>> entryMap) {
        List<Integer> entryMemberIds = entryMap.get(shareId);
        Integer memberId = randomService.nextInt(1, LIMIT);
        if (entryMemberIds.contains(memberId)) {
            return getNotEntryMemberId(shareId, entryMap);
        }
        return memberId;
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

        private int sequence;

        public void init() {
            sequence = 0;
        }

        public int getNextSequence() {
            return ++sequence;
        }

        public int getSequence() {
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

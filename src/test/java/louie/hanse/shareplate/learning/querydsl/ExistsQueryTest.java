package louie.hanse.shareplate.learning.querydsl;

import static louie.hanse.shareplate.core.keyword.domain.QKeyword.keyword;
import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ExistsQueryTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    @DisplayName("만약 데이터가 존재한다면 1을 반환한다")
    void if_data_exists_return_one() {
        Integer result = queryFactory
            .selectOne()
            .from(keyword)
            .where(keyword.id.eq(1L))
            .fetchFirst();

        assertThat(result).isOne();
    }

    @Test
    @DisplayName("만약 데이터가 존재하지 않는다면 null 값을 반환한다")
    void if_data_not_exists_return_null() {
        Integer result = queryFactory
            .selectOne()
            .from(keyword)
            .where(keyword.id.eq(Long.MAX_VALUE))
            .fetchFirst();

        assertThat(result).isNull();
    }
}

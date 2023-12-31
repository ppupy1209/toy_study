package toyproject.qna.module.member.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import toyproject.qna.module.member.entity.Member;
import toyproject.qna.module.member.repository.MemberRepository;
import toyproject.qna.module.order.dto.OrderResponseDto;
import toyproject.qna.module.order.entity.Order;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class MemberResponseDto {

    private Long id;
    private String name;
    private Integer age;
    private List<OrderResponseDto> orders;

    public static MemberResponseDto of(Member member, List<Order> orders) {
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getAge(),
                OrderResponseDto.of(orders)
        );
    }


}

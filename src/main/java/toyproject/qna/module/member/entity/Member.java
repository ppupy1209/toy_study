package toyproject.qna.module.member.entity;

import com.sun.xml.bind.v2.TODO;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import toyproject.qna.global.entity.BaseEntity;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "members")
public class Member extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id",updatable = false)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Builder
    public Member(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    public void changeName(String name) {
        this.name = name;
    }

    public void changeAge(Integer age) {
        this.age = age;
    }
}

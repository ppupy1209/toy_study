package toyproject.qna.module.item.entity;

import lombok.Getter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@Getter
public class Album extends Item {

    private String artist;
    private String etc;
}

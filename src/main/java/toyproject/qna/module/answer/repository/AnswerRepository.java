package toyproject.qna.module.answer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import toyproject.qna.module.answer.entity.Answer;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long>, AnswerRepositoryCustom{


}

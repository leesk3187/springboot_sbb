package com.mysite.sbb;

import com.mysite.sbb.answer.Answer;
import com.mysite.sbb.answer.AnswerRepository;
import com.mysite.sbb.question.Question;
import com.mysite.sbb.question.QuestionRepository;
import com.mysite.sbb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SbbApplicationTests {

	@Autowired
	private QuestionRepository questionRepository;

	@Autowired
	private AnswerRepository answerRepository;

	@Autowired
	private QuestionService questionService;

	@Test
	void testJpa() {
		Question q1 = new Question();
		q1.setSubject("sbb가 무엇인가요?");
		q1.setContent("sbb에 대해서 알고 싶습니다.");
		q1.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q1);  // 첫번째 질문 저장

		Question q2 = new Question();
		q2.setSubject("스프링부트 모델 질문입니다.");
		q2.setContent("id는 자동으로 생성되나요?");
		q2.setCreateDate(LocalDateTime.now());
		this.questionRepository.save(q2);  // 두번째 질문 저장
	}


	@Test
	void testJpa2(){
		List<Question> all = this.questionRepository.findAll();
		assertEquals(2, all.size());

		Question q = all.get(0);
		assertEquals("수정된 제목", q.getSubject());
	}

	@Test
	void testJpa3(){
		Optional<Question> oq = this.questionRepository.findById(1);
		if(oq.isPresent()){
			Question q = oq.get();
			assertEquals("수정된 제목", q.getSubject());
		}
	}

	@Test
	void testJpa4(){
		Question q = this.questionRepository.findBySubject("수정된 제목");
		assertEquals(1, q.getId());
	}

	@Test
	void testJpa5(){
		Question q = this.questionRepository.findBySubjectAndContent(
				"수정된 제목", "sbb에 대해서 알고 싶습니다."
		);
		assertEquals(1, q.getId());
	}

	@Test
	void testJpa6(){
		List<Question> qList = this.questionRepository.findBySubjectLike("수정된%");
		Question q = qList.get(0);
		assertEquals("수정된 제목", q.getSubject());
	}

	@Test
	void testJpa7(){
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		q.setSubject("수정된 제목");
		this.questionRepository.save(q);
	}

	@Test
	void testJpa8(){
		assertEquals(2, this.questionRepository.count());
		Optional<Question> oq = this.questionRepository.findById(1);
		assertTrue(oq.isPresent());
		Question q = oq.get();
		this.questionRepository.delete(q);
		assertEquals(1, this.questionRepository.count());
	}

	@Test
	void testJpa9(){
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		Answer a = new Answer();
		a.setContent("네 자동으로 생성됩니다.");
		a.setQuestion(q);  // 어떤 질문의 답변인지 알기위해서 Question 객체가 필요하다.
		a.setCreateDate(LocalDateTime.now());
		this.answerRepository.save(a);
	}

	@Test
	void testJpa10(){
		Optional<Answer> oq = this.answerRepository.findById(1);
		assertTrue(oq.isPresent());
		Answer a = oq.get();
		assertEquals(2, a.getQuestion().getId());
	}

	@Transactional
	@Test
	void testJpa11(){
		Optional<Question> oq = this.questionRepository.findById(2);
		assertTrue(oq.isPresent());
		Question q = oq.get();

		List<Answer> answerList = q.getAnswerList();

		assertEquals(1, answerList.size());
		assertEquals("네 자동으로 생성됩니다.", answerList.get(0).getContent());
	}

	@Test
	void testJpa12() {
		for (int i = 1; i <= 300; i++) {
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "내용무";
			this.questionService.create(subject, content, null);
		}
	}
}


## 프로젝트 개요

Spring Boot 기반 QnA 게시판 토이 프로젝트

질문과 답변 기능을 구현하면서 Spring MVC, JPA, Validation, QueryDSL, Thymeleaf 사용법 학습  
기능 자체는 단순하지만 Controller, Service, Repository 계층 분리와 DTO 기반 요청 응답 분리에 집중

---

## 기술 스택

### Backend

- Java 11
- Spring Boot 2.7.16
- Spring Web
- Spring Data JPA
- Spring Validation
- QueryDSL
- Lombok

### View

- Thymeleaf

### Database

- H2 Database

### Test

- JUnit 5
- Mockito
- Spring Boot Test

### Build

- Gradle

---

## 주요 기능

## 1. 질문 기능

질문 등록, 목록 조회, 상세 조회, 수정, 삭제 기능 구현

질문은 QnA 서비스의 중심 도메인  
사용자가 질문을 작성하고 다른 사용자가 해당 질문을 조회한 뒤 답변을 남길 수 있는 흐름 기준으로 구성

---

## 2. 답변 기능

질문에 대한 답변 등록, 조회, 수정, 삭제 기능 구현

답변은 질문에 종속되는 데이터로 설계  
질문 상세 화면에서 답변을 함께 확인할 수 있도록 구성  
질문과 답변의 관계를 JPA로 관리

---

## 3. 입력값 검증

사용자가 잘못된 값을 입력하지 않도록 Validation 적용

- 필수값 검증
- 제목과 내용 길이 검증
- 잘못된 요청 처리

사용자 입력을 그대로 저장하지 않고 검증을 거친 뒤 저장하는 구조 구성

---

## 4. QueryDSL 기반 조회

단순 Repository 메서드만으로 처리하기 어려운 조회 로직을 QueryDSL로 분리

QueryDSL을 사용해 동적 조회 조건 처리  
문자열 기반 쿼리보다 타입 안정성 확보

---

## 프로젝트 구조

```text
src/main/java/toyproject/qna
├── QnaApplication.java
├── controller
├── service
├── repository
├── domain
├── dto
├── config
└── exception
```


---

## 아키텍처

```text
Client
  ↓
Controller
  ↓
Service
  ↓
Repository
  ↓
Database
```

### 계층별 책임

| Layer | Responsibility |
|---|---|
| Controller | HTTP 요청 처리와 화면 응답 |
| Service | 비즈니스 로직과 트랜잭션 처리 |
| Repository | 데이터 조회와 저장 |
| Domain | 핵심 엔티티와 도메인 규칙 |
| DTO | 요청과 응답 데이터 전달 |

---

## 핵심 구현 흐름

## 1. 질문 등록

```text
질문 등록 요청
  ↓
Controller
  ↓
Request DTO 검증
  ↓
Service
  ↓
Question Entity 생성
  ↓
Repository 저장
  ↓
상세 페이지 또는 목록 페이지로 이동
```

예시 코드

```java
@Transactional
public Long createQuestion(QuestionCreateRequest request) {
    Question question = Question.create(
            request.getTitle(),
            request.getContent(),
            request.getWriter()
    );

    questionRepository.save(question);

    return question.getId();
}
```

Controller에는 요청 처리만 배치  
실제 비즈니스 로직은 Service에서 처리  
트랜잭션 경계도 Service 계층에 배치

---

## 2. 질문 목록 조회

```java
@Transactional(readOnly = true)
public List<QuestionResponse> findQuestions() {
    return questionRepository.findAll().stream()
            .map(QuestionResponse::from)
            .toList();
}
```

조회 로직에는 `readOnly = true` 적용  
Entity를 그대로 반환하지 않고 Response DTO로 변환  
외부 응답 구조와 도메인 모델 분리

---

## 3. 답변 등록

```text
답변 등록 요청
  ↓
질문 조회
  ↓
답변 Entity 생성
  ↓
질문과 답변 연관관계 설정
  ↓
답변 저장
```

예시 코드

```java
@Transactional
public Long createAnswer(Long questionId, AnswerCreateRequest request) {
    Question question = questionRepository.findById(questionId)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 질문입니다."));

    Answer answer = Answer.create(
            question,
            request.getContent(),
            request.getWriter()
    );

    answerRepository.save(answer);

    return answer.getId();
}
```

답변은 반드시 질문에 속해야 하므로 질문을 먼저 조회한 뒤 답변 생성  
존재하지 않는 질문에 답변을 작성할 수 없도록 예외 처리

---

## 4. QueryDSL 조회

```java
public List<Question> searchQuestions(String keyword) {
    return queryFactory
            .selectFrom(question)
            .where(
                    titleContains(keyword)
                            .or(contentContains(keyword))
            )
            .orderBy(question.createdAt.desc())
            .fetch();
}
```

검색 조건이 늘어날 경우 Repository 메서드가 과도하게 많아질 수 있음  
QueryDSL을 사용해 조회 조건을 메서드로 분리  
동적 조건 처리를 명확하게 표현

---

## 화면 흐름 예시

| Method | URL | Description |
|---|---|---|
| GET | /questions | 질문 목록 조회 |
| GET | /questions/{id} | 질문 상세 조회 |
| GET | /questions/new | 질문 작성 화면 |
| POST | /questions | 질문 등록 |
| GET | /questions/{id}/edit | 질문 수정 화면 |
| POST | /questions/{id}/edit | 질문 수정 |
| POST | /questions/{id}/delete | 질문 삭제 |
| POST | /questions/{id}/answers | 답변 등록 |


---

## 학습한 내용

- Spring Boot 기반 웹 애플리케이션 구조
- Controller, Service, Repository 계층 분리
- JPA Entity 설계
- DTO를 활용한 요청과 응답 분리
- Validation을 통한 입력값 검증
- QueryDSL 설정과 사용
- Thymeleaf 기반 화면 렌더링
- H2 Database 기반 로컬 개발 환경 구성

---

## 기술 선택 이유

## JPA

질문과 답변처럼 관계가 있는 도메인을 객체 중심으로 다루기 위해 JPA 사용  
반복적인 SQL 작성 부담을 줄이고 Repository를 통해 데이터 접근 계층 분리

---

## QueryDSL

검색 조건이 늘어나면 Repository 메서드 이름이 길어지고 관리가 어려워질 수 있음  
QueryDSL을 사용해 조회 조건을 코드로 명확하게 표현  
동적 쿼리도 비교적 깔끔하게 처리 가능

---

## Thymeleaf

백엔드 학습 과정에서 서버 사이드 렌더링 흐름도 함께 익히기 위해 Thymeleaf 사용  
Controller에서 View로 데이터를 전달하고 화면에서 사용자 입력을 받아 다시 서버로 요청하는 전체 흐름 학습

---

## 회고

Spring Boot 웹 애플리케이션의 기본 흐름을 익히기 위한 토이 프로젝트

질문과 답변이라는 단순한 도메인을 통해 계층 분리, JPA Entity 설계, DTO 분리, Validation, QueryDSL, Thymeleaf 사용 흐름 학습

단순 기능 구현보다 Entity를 외부로 직접 노출하지 않고 계층별 책임을 나누는 구조에 집중

---

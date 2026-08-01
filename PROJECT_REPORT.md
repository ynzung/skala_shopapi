# SKALA Shop API

고객이 포인트로 상품을 주문하는 Spring Boot 기반 쇼핑 API입니다. JPA + H2로 데이터를 관리하고, Spring Security + JWT로 로그인 인증을 처리합니다.

- **기본 기능** (과제 필수 요구사항): 고객 CRUD, 상품 CRUD, 주문/취소
- **차별화 기능** (추가 구현): 회원가입 축하 포인트, 출석 체크 포인트, 상품 찜, `index.html` 웹 화면
- **선택 과제**: JWT 인증/보안

---

## 목차

- [기능 구분 요약](#기능-구분-요약)
- [기술 스택](#기술-스택)
- [폴더 구조](#폴더-구조)
- [JWT 인증 흐름](#jwt-인증-흐름)
- [오류 코드](#오류-코드)
- [API 목록](#api-목록)
- [트랜잭션 적용 기능](#트랜잭션-적용-기능)
- [실행 방법](#실행-방법)

---

## 기능 구분 요약

| 구분      | 기능        | 설명                                                          |
| --------- | ----------- | ------------------------------------------------------------- |
| 기본      | 고객 CRUD   | 회원가입, 조회, 로그인, 비밀번호 수정, 삭제                   |
| 기본      | 상품 CRUD   | 등록, 조회, 수정, 삭제                                        |
| 기본      | 주문/취소   | 포인트로 주문, 취소 시 포인트 환급                            |
| 차별화    | 포인트 지급 | 가입 시 3,000P, 출석 시 1,000P                                |
| 차별화    | 상품 찜     | 찜 토글 및 찜 목록 조회                                       |
| 차별화    | index.html  | API 연동 단일 페이지 웹 화면                                  |
| 선택 과제 | JWT 인증    | Spring Security + JWT, 비밀번호 암호화, 본인 데이터 접근 제어 |

---

## 기술 스택

| 구분                 | 기술                                          |
| -------------------- | --------------------------------------------- |
| Language / Framework | Java 21, Spring Boot 4.1.0                    |
| DB / ORM             | H2 (in-memory), Spring Data JPA               |
| 인증                 | Spring Security, JJWT 0.13.0                  |
| 문서화               | springdoc-openapi (Swagger, Bearer 인증 지원) |
| 기타                 | Gradle, Lombok                                |
| Frontend             | HTML/CSS/JS (`index.html`)                    |

---

## 폴더 구조

```text
skala_shopapi/
├── build.gradle
├── settings.gradle
└── src/
    └── main/
        ├── java/com/sk/skala/shopapi/
        │   ├── ShopapiApplication.java
        │   ├── config/
        │   │   ├── SecurityConfig.java          # 인증 정책, 공개/보호 경로 설정
        │   │   └── OpenApiConfig.java             # Swagger Bearer 인증 설정
        │   ├── security/
        │   │   ├── JwtAuthenticationFilter.java    # 요청별 JWT 검증 필터
        │   │   ├── JwtTokenProvider.java             # 토큰 발급/검증
        │   │   └── RestAuthenticationEntryPoint.java # 인증 실패 응답 처리
        │   ├── controller/
        │   │   ├── CustomerController.java        # 회원가입/로그인/고객 정보
        │   │   ├── ProductController.java           # 상품 CRUD
        │   │   ├── OrderController.java              # 주문/취소
        │   │   ├── RewardController.java              # 출석 체크
        │   │   └── FavoriteController.java             # 상품 찜
        │   ├── service/
        │   │   ├── AuthService.java                 # 로그인 인증 및 JWT 발급
        │   │   ├── CustomerService.java
        │   │   ├── ProductService.java
        │   │   ├── OrderService.java
        │   │   ├── RewardService.java
        │   │   └── FavoriteService.java
        │   ├── repository/
        │   │   ├── CustomerRepository.java
        │   │   ├── ProductRepository.java
        │   │   ├── OrderItemRepository.java
        │   │   ├── DailyCheckInRepository.java
        │   │   └── FavoriteRepository.java
        │   ├── entity/
        │   │   ├── Customer.java
        │   │   ├── Product.java
        │   │   ├── OrderItem.java
        │   │   ├── DailyCheckIn.java
        │   │   └── Favorite.java
        │   ├── dto/
        │   │   ├── customer/
        │   │   │   ├── SignupRequest.java
        │   │   │   ├── SignupResponse.java
        │   │   │   ├── LoginRequest.java
        │   │   │   ├── LoginResponse.java
        │   │   │   ├── UpdateCustomerRequest.java
        │   │   │   └── CustomerResponse.java
        │   │   ├── product/
        │   │   │   ├── ProductRequest.java
        │   │   │   └── ProductResponse.java
        │   │   ├── order/
        │   │   │   ├── OrderRequest.java
        │   │   │   ├── OrderItemResponse.java
        │   │   │   └── OrderListResponse.java
        │   │   ├── reward/
        │   │   │   └── CheckInResponse.java
        │   │   └── favorite/
        │   │       └── FavoriteResponse.java
        │   └── exception/
        │       ├── ErrorCode.java
        │       ├── ErrorResponse.java
        │       ├── GlobalExceptionHandler.java
        │       ├── DataNotFoundException.java
        │       ├── DuplicateCustomerException.java
        │       ├── InsufficientFundsException.java
        │       ├── ParameterException.java
        │       └── RewardAlreadyReceivedException.java
        └── resources/
            ├── application.yml
            ├── data.sql
            └── static/
                └── index.html          # 차별화 기능: 웹 화면
```

---

## JWT 인증 흐름

```text
로그인 성공 → JWT 발급(고객ID를 subject로 저장, 만료 60분)
   → 이후 요청에 Authorization: Bearer {token} 헤더 첨부
   → JwtAuthenticationFilter가 서명·만료 검증 → SecurityContext에 인증 정보 저장
   → Controller가 Principal.getName()으로 로그인 고객 식별
```

- 비밀번호는 `PasswordEncoder`(BCrypt)로 암호화하여 저장합니다.
- 개인 API는 요청의 고객 ID가 아닌 **토큰 소유자 ID**로 처리하여 타인 정보 조작을 방지합니다.
- 공개 API: 회원가입, 로그인, 상품 조회, 전체 고객 목록
- 그 외 개인/상품 변경 API는 토큰이 필요합니다.

---

## 오류 코드

| 코드                      | HTTP | 상황                          |
| ------------------------- | ---: | ----------------------------- |
| `DUPLICATE_CUSTOMER_ID`   |  409 | 회원가입 아이디 중복          |
| `AUTHENTICATION_FAILED`   |  401 | 로그인 아이디/비밀번호 불일치 |
| `INSUFFICIENT_FUNDS`      |  400 | 포인트 부족                   |
| `PARAMETER_ERROR`         |  400 | 필수값/수량 오류              |
| `REWARD_ALREADY_RECEIVED` |  409 | 당일 출석 중복                |
| `DATA_NOT_FOUND`          |  404 | 고객/상품/주문 없음           |

---

## API 목록

| Method              | URL                                   | 인증 | 설명                |
| ------------------- | ------------------------------------- | ---- | ------------------- |
| POST                | `/api/customers`                      | 공개 | 회원가입 (+3,000P)  |
| POST                | `/api/customers/login`                | 공개 | 로그인, JWT 발급    |
| GET                 | `/api/customers/list`                 | 공개 | 전체 고객 조회      |
| PUT / DELETE        | `/api/customers/me`                   | 필요 | 본인 정보 수정/삭제 |
| GET                 | `/api/products`, `/api/products/{id}` | 공개 | 상품 조회           |
| POST / PUT / DELETE | `/api/products(/{id})`                | 필요 | 상품 등록/수정/삭제 |
| GET                 | `/api/customers/me`, `/me/products`   | 필요 | 내 포인트·주문 조회 |
| POST                | `/api/customers/order`                | 필요 | 상품 주문           |
| POST                | `/api/customers/cancel`               | 필요 | 주문 취소           |
| POST                | `/api/customers/me/check-in`          | 필요 | 출석 체크 (+1,000P) |
| POST / GET          | `/api/customers/favorites(/{id})`     | 필요 | 찜 토글/조회        |

---

## 트랜잭션 적용 기능

| 기능      | 함께 처리되는 데이터                      |
| --------- | ----------------------------------------- |
| 회원가입  | 고객 저장 + 포인트 설정 + 비밀번호 암호화 |
| 상품 주문 | 주문 수량 저장 + 포인트 차감              |
| 주문 취소 | 수량 변경/삭제 + 포인트 환급              |
| 출석 체크 | 출석 기록 + 포인트 지급                   |
| 고객 삭제 | 주문·출석·찜 데이터 삭제 + 고객 삭제      |

---

## 실행 방법

```bash
./gradlew bootRun
```

| 항목         | URL                                         |
| ------------ | ------------------------------------------- |
| 웹 화면      | http://localhost:8080/                      |
| Swagger UI   | http://localhost:8080/swagger-ui/index.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs           |

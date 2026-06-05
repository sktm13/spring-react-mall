## 프로젝트 소개
Spring Boot API ,React+Vite 를 활용한 쇼핑몰 서비스

---

## 주요 기능

### 인증 / 회원
- JWT 인증 (Access / Refresh Token)
- Redux Toolkit + 쿠키 기반 로그인 상태 유지
- Kakao 로그인 연동 및 회원 자동 생성

### 상품
- 이미지 업로드 및 썸네일 생성
- 페이징 처리

### 장바구니
- 사용자별 장바구니 생성
- 장바구니 상태 관리

### 문의하기
- ADMIN 권한 사용자 답변 기능

### 추가 구현
- AWS 기반 배포 환경 구축 (S3, CloudFront, EC2, ELB, RDS)
  
## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Querydsl
- MariaDB

### Frontend
- React + Vite
- TypeScript
- Redux Toolkit

## AWS 인프라 구성

```
User
├─ https://yujin-mall.com
│ ↓
│ CloudFront
│ ↓
│ S3
│
└─ https://api.yujin-mall.com
↓
ELB
↓
EC2
↓
RDS

Image Upload
EC2 ↔ S3
```
---

## 실행 방법

Docker Compose 기준으로 실행할 수 있습니다.

민감정보는 GitHub에 포함하지 않으며, 실행 전 루트 경로에 `.env` 파일을 직접 생성해야 합니다.

### 1. 환경변수 파일 생성

프로젝트 루트에서 `.env.example` 파일을 복사해 `.env` 파일을 생성합니다.

```cp .env.example .env```

생성한 `.env` 파일에서 본인의 환경에 맞게 값을 수정합니다.

특히 Kakao 로그인을 사용하려면 아래 값을 입력해야 합니다.

```env
KAKAO_REST_API_KEY=your-kakao-rest-api-key
KAKAO_CLIENT_SECRET=your-kakao-client-secret
KAKAO_REDIRECT_URI=http://localhost:5173/member/kakao

VITE_KAKAO_REST_API_KEY=your-kakao-rest-api-key
VITE_KAKAO_REDIRECT_URI=http://localhost:5173/member/kakao
```

Kakao Developers에도 Redirect URI를 등록해야 합니다.

```text
http://localhost:5173/member/kakao
```
---

### 2. Docker 실행

프로젝트 루트에서 아래 명령어를 실행합니다.

```bash
docker compose up --build
```
---

### 3. 접속 주소

```text
Frontend: http://localhost:5173
Backend:  http://localhost:8080
MariaDB:  localhost:3307
```
---

## 초기 데이터

Docker 실행 시 DB에 데이터가 없으면 초기 데이터가 자동 생성됩니다.

초기 데이터는 서버 시작 시 `DataInitializer`에서 생성되며, 이미 데이터가 존재하는 경우 중복 생성하지 않습니다.

---

### 테스트 계정

#### USER

```text
email: user@aaa.com
password: 1111
role: USER
```

#### ADMIN

```text
email: admin@aaa.com
password: 1111
role: USER, ADMIN
```

#### MANAGER

```text
email: manager@aaa.com
password: 1111
role: USER, MANAGER
```

---

### 초기 상품 데이터

초기 상품 20개가 자동 생성됩니다.

| 번호 | 상품명          |       가격 |
| -: | ------------ | -------: |
|  1 | 베이직 반팔 티셔츠   |  19,900원 |
|  2 | 오버핏 후드티      |  45,900원 |
|  3 | 슬림 데님 팬츠     |  39,900원 |
|  4 | 코튼 와이드 팬츠    |  42,900원 |
|  5 | 라이트 윈드브레이커   |  69,900원 |
|  6 | 미니멀 셔츠       |  34,900원 |
|  7 | 스웨트 조거 팬츠    |  37,900원 |
|  8 | 니트 가디건       |  55,900원 |
|  9 | 레귤러 치노 팬츠    |  36,900원 |
| 10 | 데일리 맨투맨      |  32,900원 |
| 11 | 트레이닝 셋업 상의   |  48,900원 |
| 12 | 트레이닝 셋업 하의   |  42,900원 |
| 13 | 베이직 볼캡       |  19,900원 |
| 14 | 캔버스 토트백      |  25,900원 |
| 15 | 러닝 스니커즈      |  79,900원 |
| 16 | 클래식 로퍼       |  89,900원 |
| 17 | 데일리 백팩       |  59,900원 |
| 18 | 울 블렌드 코트     | 129,000원 |
| 19 | 패딩 베스트       |  89,000원 |
| 20 | 스트라이프 긴팔 티셔츠 |  29,900원 |

상품별 샘플 이미지는 서버 실행 시 자동 생성됩니다.

```text
sample_01.jpg ~ sample_20.jpg
s_sample_01.jpg ~ s_sample_20.jpg
default.jpeg
s_default.jpeg
```

---

### 초기 문의 데이터

초기 문의 8개가 자동 생성됩니다.

| 번호 | 제목             | 작성자                                       | 상태   |
| -: | -------------- | ----------------------------------------- | ---- |
|  1 | 배송은 얼마나 걸리나요?  | [user@aaa.com](mailto:user@aaa.com)       | WAIT |
|  2 | 상품 사이즈 문의      | [user@aaa.com](mailto:user@aaa.com)       | DONE |
|  3 | 이미지 업로드 테스트 문의 | [manager@aaa.com](mailto:manager@aaa.com) | WAIT |
|  4 | 반품 가능 여부 문의    | [user@aaa.com](mailto:user@aaa.com)       | DONE |
|  5 | 관리자 답변 기능 확인   | [manager@aaa.com](mailto:manager@aaa.com) | WAIT |
|  6 | 장바구니 수량 변경 문의  | [user@aaa.com](mailto:user@aaa.com)       | WAIT |
|  7 | 카카오 로그인 문의     | [user@aaa.com](mailto:user@aaa.com)       | DONE |
|  8 | 상품 검색 기능 문의    | [manager@aaa.com](mailto:manager@aaa.com) | WAIT |

`DONE` 상태의 문의는 관리자 답변 데이터도 함께 생성됩니다.

---

### 초기 데이터 재생성 방법

이미 생성된 DB 데이터가 있으면 초기 데이터는 다시 생성되지 않습니다.

초기 데이터를 다시 생성하려면 Docker volume까지 삭제한 뒤 재실행합니다.

```bash
docker compose down -v
docker compose up --build
```

`-v` 옵션은 MariaDB 데이터와 업로드 파일 volume을 삭제하므로 주의해야 합니다.

---

### 5. Docker 종료

컨테이너를 종료합니다.

```bash
docker compose down
```

DB 데이터와 업로드 파일 볼륨까지 초기화하려면 아래 명령어를 사용합니다.

```bash
docker compose down -v
```
`-v` 옵션을 사용하면 다음 실행 시 초기 회원, 상품, 문의 데이터가 다시 생성됩니다.

---
## 실행 방법 (배포 환경)

~~URL 접속 : https://yujin-mall.com~~
※ 현재 배포 종료 
---

## 실행 화면
(1) 초기 구현
![ezgif com-animated-gif-maker](https://github.com/user-attachments/assets/5af5ee33-fbc6-4b6d-9879-daaabca17195)

(2) 기능 확장 및 UI/UX 리팩토링
![리팩시나](https://github.com/user-attachments/assets/1851d258-867d-4014-8725-a0b21436c3b1)



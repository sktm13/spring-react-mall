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

### 4. 테스트 계정

Docker 실행 시 초기 데이터가 자동 생성됩니다.

#### USER

```text
email: user@aaa.com
password: 1111
```

#### ADMIN

```text
email: admin@aaa.com
password: 1111
```

#### MANAGER

```text
email: manager@aaa.com
password: 1111
```

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



# mallapi

## 프로젝트 소개
이 프로젝트는 강의를 기반으로 구현한 쇼핑몰 API 서비스입니다.  
기본 구조는 강의를 참고했으며, 로그인 실패 이후, default 이미지 처리 등 일부 사용자 시나리오를 직접 구현했습니다.

---

## 참고 강의
- 강의명: 코드로 배우는 React 19 with 스프링부트 API서버
- 플랫폼: 인프런

---

## 주요 기능

### 인증 / 회원
- JWT 기반 인증 (Access/Refresh Token)
- Spring Security 기반 권한 관리
- 로그인 처리 (성공/실패 핸들러)
- 로그인 상태 관리 (Redux Toolkit, 커스텀 훅 useCustomLogin 활용, 쿠키 유지)
- Kakao 로그인 연동 및 회원 객체 자동 생성

### 상품
- 상품 CRUD 및 이미지 업로드/썸네일 처리
- 페이징 처리 (목록 조회, useCustomMove 활용)

### 장바구니
- 사용자별 장바구니 생성
- 상품 추가 / 수량 변경 / 삭제
- 장바구니 목록 조회
- 장바구니 상태 관리 (useCustomCart 활용)

---

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- Querydsl
- MariaDB
- Gradle

### Frontend
- React
- TypeScript
- Redux Toolkit
- Axios (JWT 인터셉터 적용)
- Vite
- Node.js

---

## 프로젝트 구조


mallapi/
├── backend/
└── frontend/


---

## 실행 방법

### Backend

- application.yml에 DB 설정 필요
- ./gradlew bootRun


### Frontend

- npm install
- npm run dev


---

## 실행 화면
(여기에 이미지/GIF 추가)

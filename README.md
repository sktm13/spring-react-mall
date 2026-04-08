## 프로젝트 소개
Spring Boot API 서버와 React를 활용해 구현한 쇼핑몰 서비스입니다.  

강의를 기반으로 시작했으며,  
문의 기능 추가, 사용자 시나리오 개선, 전체 UI/UX 리팩토링을 통해 기능을 확장했습니다.
---

## 참고 강의
- 강의명: 코드로 배우는 React 19 with 스프링부트 API서버
- 플랫폼: 인프런

---

## 주요 기능

### 인증 / 회원
- JWT 기반 인증 (Access / Refresh Token)
- Spring Security 권한 관리
- 로그인 처리 (성공/실패 핸들러 커스터마이징)
- Redux Toolkit + 쿠키 기반 로그인 상태 유지
- Kakao 로그인 연동 및 회원 자동 생성

### 상품
- 상품 CRUD
- 이미지 업로드 및 썸네일 생성
- 페이징 처리 (커스텀 훅 useCustomMove 활용)

### 장바구니
- 사용자별 장바구니 생성
- 상품 추가 / 수량 변경 / 삭제
- 장바구니 상태 관리 (커스텀 훅 useCustomCart 활용)

### 문의하기 (추가 구현)
- Inquiry 기능 구현
- ADMIN 권한 사용자 답변 기능
---

## 추가 구현 및 개선 사항
- 로그인 실패 시나리오 처리
- 기본 이미지 fallback 처리
- 전체 UI/UX 리팩토링

  
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

```
project/
 ├── backend/ 
 │ ├── src/ 
 │ ├── build.gradle 
 │ ├── settings.gradle 
 │ └── gradlew 
 ├── frontend/ 
 │ ├── src/ 
 │ ├── public/ 
 │ ├── package.json 
 │ ├── vite.config.ts 
 │ └── tsconfig.json 
```
---

## 실행 방법

### Backend
- application.yml에 DB 설정 필요
- cd backend
- ./gradlew bootRun



### Frontend
- cd frontend
- npm install
- npm run dev


---

## 실행 화면
(1) 초기 구현
![ezgif com-animated-gif-maker](https://github.com/user-attachments/assets/5af5ee33-fbc6-4b6d-9879-daaabca17195)

(2) 기능 확장 및 UI/UX 리팩토링
![리팩시나](https://github.com/user-attachments/assets/1851d258-867d-4014-8725-a0b21436c3b1)



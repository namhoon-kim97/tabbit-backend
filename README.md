# Tabbit Backend

**Tabbit**: "Tab! Eat!"  
NFC 태그를 활용한 간편한 식당 웨이팅 관리 솔루션.

## 프로젝트 개요
Tabbit은 NFC 기술을 활용하여 식당 웨이팅을 간편하게 관리하고, 실시간 데이터 동기화 및 스탬프/칭호 시스템을 통해 사용자 경험을 향상시키는 프로젝트입니다.

<img src="https://github.com/user-attachments/assets/d58708f1-7d5f-4550-924b-6da6a65c56d7" alt="Tabbit Poster" width="150">

## 주요 기능
1. **NFC 기반 웨이팅 관리**
   - NFC 태그로 웨이팅 번호를 간단하게 발급.
   - 매일 오전 5시에 웨이팅 번호 자동 초기화.

2. **실시간 데이터 동기화**
   - 고객과 점주 간 상태를 실시간으로 업데이트.
   - WebSocket과 Firebase Cloud Messaging(FCM)을 활용.

3. **스탬프 및 칭호 수집**
   - 사용자의 활동에 따라 스탬프를 수집하고, 조건에 따라 칭호를 부여.

4. **지도 기반 맛집 검색 및 예약**
   - 지도에서 주변 맛집 검색 가능.
   - 예약 및 주문을 통해 시간 절약.

## 기술적 도전과 해결
1. **NFC 태그 통신**

2. **실시간 데이터 송수신**
   - 초기 구현에서 소켓 연결 문제로 데이터 유실 발생.
   - 개선된 WebSocket 및 FCM 구조로 문제 해결.

3. **과도한 서버 부하**
   - FCM 메시징 최적화를 통해 필요할 때만 데이터 렌더링.


## 백엔드 아키텍처
- **기술 스택**: Java Spring Boot, JPA, MySQL, AWS.
- **주요 모듈**:
  - **웨이팅 관리**: `ConcurrentHashMap`과 `AtomicLong`을 활용하여 구현.
  - **실시간 이벤트**: 서버 부하를 줄이기 위해 FCM 메시징 최적화.
  - **배지 부여**: 사용자 상태를 평가하여 배지 자동 부여.
 
![Tabbit포스터찐최종본](https://github.com/user-attachments/assets/43ec341b-539b-4e3a-a3ce-aba20f0e9f55)

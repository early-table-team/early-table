# 얼리테이블 : 실시간 음식점 웨이팅&예약 어플

<img src = "https://github.com/user-attachments/assets/374b47bd-1017-458d-89f6-4ebf05833575" width = "500">  


### "줄서기 스트레스는 그만! 효율적인 웨이팅의 시작"

외식을 자주 즐기는 요즘 인기 맛집에서 길게 줄 서는 시대는 끝! 얼리 테이블로 미리 예약하고, 웨이팅을 잡아 스마트하게 식사 준비하세요. 시간도 아끼고, 더 맛있게!!

## ❤️ 서비스/프로젝트 소개

### 서비스 개요

<aside>
본 서비스는 음식점 예약 및 웨이팅 기능을 제공하여 사용자 경험을 개선한 프로젝트입니다. 기존 테이블링 앱의 낮은 사용성을 보완하고, 보다 편리한 예약 및 웨이팅 시스템을 제공합니다.

</aside>

### 기획 배경


#### 현재 많은 음식점이 테이블링 앱을 활용하고 있지만, 사용자들은 여러 문제점을 겪고 있습니다.

- **부정확한 대기 시간 예측**: 기존 서비스에서는 대기 시간이 미제공 또는 부정확하게 제공되어 사용자 불편이 발생
- **개인화된 서비스 부족**: 사용자의 식습관(예: 알러지 등)에 대한 필터링 기능이 미비
- **일행 관리 서비스 부족**: 일행이 예약에 대한 정보를 알고싶을 때 예약자를 통해 확인하는 번거로움 발생


### 개선 사항

#### 본 서비스는 기존의 문제를 해결하고, 보다 편리한 예약 및 웨이팅 환경을 제공하기 위해 다음과 같은 기능을 제공합니다.

1. **실시간 예상 대기 시간 제공**
    - 정확한 대기 시간 정보 제공
    - 실시간 대기열 업데이트로 사용자의 대기 불편 최소화
2. **맞춤형 필터링 기능**
    - 알러지, 특정 식재료 제외 등 사용자의 조건을 고려한 음식점 필터링 제공
3. **향상된 사용자 경험 제공**
    - 손쉬운 예약 및 웨이팅 기능 제공
    - 알림 기능을 활용한 예약 및 웨이팅 상태 실시간 업데이트
    - 일행을 초대하고 관리할 수 있는 서비스 제공
    - 실시간 특정 가게 조회수 조회 기능 제공


## 📊 주요 기능

<details>
<summary>실시간 웨이팅</summary>
<div markdown="1">

- 실시간 남은 웨이팅 순서 정보 + 실시간 예상 대기 시간 제공

<img src = "https://github.com/user-attachments/assets/46615295-da6f-4312-b68c-3b4431e21d7a" width = "180">  

</div>
</details>

<details>
<summary>실시간 예약 및 결제 API 연동</summary>
<div markdown="1">

- 동시성 제어 - Redis 분산락 적용
- 카카오 페이의 API를 연동하여 결제 정보처리 및 결과 DB에 반영

<img src = "https://github.com/user-attachments/assets/ac77ae79-095e-4c3f-9b58-39a4f7923ffc" width = "520">  


</div>
</details>

<details>
<summary>실시간 가게 조회수</summary>
<div markdown="1">

- SSE와 Redis Pub/Sub 기능 이용

<img src = "https://github.com/user-attachments/assets/71d2ae6a-5791-4ffe-a50d-08173128329d" width = "180">  

</div>
</details>

<details>
<summary>알림 서비스</summary>
<div markdown="1">

- FCM과 SSE, Redis Pub/Sub 기능 이용

<img src = "https://github.com/user-attachments/assets/524a9604-1df0-450c-8b52-4c3a74c18b2e" width = "480">  

</div>
</details>

<details>
<summary>맞춤형 조건 검색</summary>
<div markdown="1">

- 지역, 금액, 음식 카테고리, 알러지 및 검색어로 맞춤 검색 기능 제공

<img src = "https://github.com/user-attachments/assets/ed79f86e-bb8d-4257-b2ec-395c498deb23" width = "680">  


</div>
</details>

## 🛠️ 적용 기술

<img src = "https://github.com/user-attachments/assets/2c066087-101a-41b6-b756-d581c28b387b" width = "500"> 


## 🛣️ 인프라 설계도

<img src = "https://github.com/user-attachments/assets/055a7208-eb87-43be-b6e9-21ce1723ab19" width = "700"> 

## ERD

<details>
<summary>ERD</summary>
<div markdown="1">

<img src = "https://github.com/user-attachments/assets/4da306f5-644a-42d5-b484-3e0af8d981ff" width = "900"> 

</div>
</details>

## API 명세서

[API 명세서 노션 페이지](https://quick-air-c33.notion.site/1a146aaef6e480f59d9af98bf17a13f9?v=1a146aaef6e48186959f000c46da3f8b)

 
## 🛰️ 기술적 의사 결정

<details>
<summary>🚦예약 동시성 제어 : Redisson 분산락</summary>
<div markdown="2">

### 🔒예약 순서를 세울 때 분산 락을 사용하는 이유
    
#### 1️⃣ 중복 예약 방지🛑
    
  - 여러 사용자가 동시에 같은 시간대 예약을 시도할 수 있음.
  - 락을 사용하면 한 번에 한 명만 해당 예약을 점유하도록 제어가 가능.
    
#### 2️⃣ 경쟁 조건(Race Condition)해결⚔️
    
  - 락이 없으면 같은 시간대 예약 요청이 거의 동시에 들어올 경우 예약이 중복 생성될 수 있음.
  - 락을 사용하면 첫 번째 요청이 완료된 후 다음 요청이 진행됨.
    
#### 3️⃣ 데이터 정합성 유지🔄
    
  - 예약 시스템에서는 예약 순서가 중요한 경우가 많음.
- 락을 사용하면 하나의 트랜잭션이 완료된 후 다음 트랜잭션이 실행되도록 보장 가능.
    
### 📊Redis에서 직접 분산락 구현 vs. Redisson의 분산락 사용 비교
    
| 비교 항목 | Redis 직접 구현 | Redisson의 분산락(RLock) |
| --- | --- | --- |
| 구현 난이도 | 직접 Lua 스크립트 또는 SETNX를 활용하여 구현해야 함 | API를 활용하여 손쉽게 락을 적용 가능 |
| 재진입성
(Reentrant) | 기본적으로 지원되지 않음 (직접 구현해야 함) | 기본적으로 재진입 가능 |
| TTL 설정 | 직접 만료 시간을 설정해야 하며, 스크립트를 통해 갱신해야 함 | 자동 갱신 지원 (watchdog) |
| 노드 장애 대응 | 노드 장애 시 락이 사라질 수 있음 (TTL로 보완 가능) | Redisson의 Watchdog 기능으로 자동 연장 |
| 성능 | 경량 구현 가능하지만 추가적인 보완 로직이 필요 | 추가적인 기능 제공으로 인해 약간의 성능 오버헤드 가능 |
| 클러스터 지원 | Redis 클러스터 환경에서는 추가적인 고려 필요 | Redisson이 자체적으로 클러스터 지원 |
| 락 해제 안전성 | 클라이언트 장애 시 락이 남아 있을 가능성 있음 (TTL 설정 필수) | Redisson이 자동으로 해제 관리 |
    
### 🔐Redisson을 선택한 이유
    
**1️⃣ 자동 연장 기능 지원 ⏳**
    
Redisson의 **RLock**은 락을 획득한 스레드가 살아있는 동안 **자동으로 만료 시간을 연장**해줍니다. 따라서, **예기치 않은 딜레이나 작업 시간이 길어지는 경우에도 락이 갑자기 해제되지 않도록 보장**할 수 있습니다. 반면 Redis의 SET NX EX 방식은 설정한 만료시간이 지나게 되면 자동으로 해제되므로 추가적인 유지 로직이 필요합니다.
    
2️⃣ **분산 환경에서의 안정성 🔄🌍**
    
**Redisson은 분산 환경에서도 안정적으로 동작**하도록 설계되었습니다. 여러 서버에서 동시에 락을 요청하더라도 **Redisson이 Redis를 활용해 분산 환경에서 자동으로 락을 동기화하며, 충돌을 방지**합니다. 직접 Redis를 사용한 락 구현에서는 네트워크 장애나 서버 장애시 데이터 정합성을 보장하는 추가적인 로직이 필요하지만, Redisson은 이를 자동으로 처리해 줍니다.
    
3️⃣ **빠른 적용 및 유지보수 용이 📖⚡**
    
이번 프로젝트를 진행하면서 **Redis를 직접 활용한 경험이 부족한 상태**였기 때문에 분산락을 처음부터 구현하며 발생할 수 있는 **복잡한 동기화 이슈를 직접 제어하는데 상당한 시간과 리소스를 요구할 가능성이 있다고 생각**을 했습니다. 하지만 **Redisson은 이미 검증된 분산락 기능을 제공**하며, **직관적인 API**를 통해 쉽게 적용할 수 있도록 설계되어 있습니다. 이를 활용하면 별도의 락 관리 로직을 직접 구현할 필요 없이 **신뢰성 높은 분산락을 손쉽게 적용**할 수 있으며, 향후 **유지보수 또한 간결하게 진행**할 수 있다는 점에서 더욱 효율적인 선택이라 판단했습니다.
    

</div>
</details>

<details>
<summary>💰결제 API : KG 이니시스 vs  KakaoPay  vs  TossPay</summary>
<div markdown="1">

### KG 이니시스 🏦💳
    
- **🟩장점**
  - **다양한 결제 수단 지원💳** : 신용카드, 가상계좌, 계좌이체, 휴대폰 결제 등 다양한 결제 옵션을 제공
  - **API 안정성 높음🔒** : 오랜 기간 운영된 PG사인만큼 장애 발생 가능성이 적음
  - **정기 결제 & B2B 기능 제공📅🔄** : 정기 결제, 자동 결제 같은 고급 기능 지원
  - **대량 거래 처리 가능📊** : 트래픽이 많거나 B2B 환경에서도 안정적
- **🟥단점**
  - **JSON기반이 아닌 key=value방식 사용📝** : RESTful API를 제공하지만, 최신 표준인 JSON이 아닌 key = value 형태로 데이터를 주고 받음
    - **불편한 테스트 환경⚠️** : API를 테스트 하려면 실제 가맹점 가입 & 승인 절차가 필요
      - **UI/UX 개선 필요🖥️** : 다른 결제 서비스 대비 사용자 경험이 다소 불편할 수 있음
    
### KakaoPay💛💬💰
- **🟩장점**
  - **높은 사용자 접근성🌍** : 카카오톡 기반으로 가입자가 많아 결제 전환율이 높음
  - **모바일 친화적인 UX/UI📱** : 카카오톡 기반 결제로 사용자에게 익숙한 환경
  - **편의성🔑** : 결제 연동이 간편하고 문서화가 잘 되어 있음
  - **손쉬운 테스트 환경🧪** : 별도의 심사 없이 테스트 가능
- **🟥단점**
  - **카카오톡 중심📲** : 카카오톡을 사용하지 않는 사용자에게는 접근성이 낮음, 카카오톡 장애 발생시 결제 불가
  - **일부 결제 수단 제한 🌍💳** : 해외 결제 및 일부 신용카드 결제 제한
  - **제한된 UI커스텀🛠️** : 카카오 페이에서 제공하는 방식으로만 창을 띄울 수 있고 PC환경에서는 결제를 위해 QR코드 스캔 또는 전화번호 입력의 번거로움이 있음
    
### TossPay 💙⚡💰
- **🟩장점**
  - **손쉬운 테스트 환경🧪** : 별도의 심사 없이 테스트 가능
  - **결제창 UI 커스텀 가능🎨** : Toss Paymemts SDK를 활용하여 자체 UI구성이 가능
  - **광범위한 결제 수단 지원 💳📱** : 신용카드, 계좌이체, 가상계좌, 휴대폰결제 등 다양한 결제 수단을 제공
- **🟥단점**
  - **제한적인 사용자층👥** : 토스 사용자가 빠르게 증가하였지만, 카카오페이에 비해 인지도나 이용자가 적을 수 있음 (4050세대 이상)
  - **PC환경에서의 결제 경험 제한💻** : 모바일 중심 서비스라 PC웹에서 UX가 최적화 되지 않았을 수도 있음
    
### **왜 KakaoPay를 선택했는가?** 💛
  이번 프로젝트에서 KakaoPay를 선택한 이유는 주로 사용자 접근성과 편의성에 큰 장점이 있기 때문입니다. 여러 PG사들이 각각의 장단점이 있지만, KakaoPay는 다음과 같은 이유로 더 적합한 선택이었습니다.

  #### 1. 높은 사용자 접근성 🌍
  KakaoPay는 **카카오톡**과 깊게 통합되어 있기 때문에, 이미 **많은 사용자들이 익숙**하게 사용하고 있는 환경입니다. 결제 과정에서 사용자가 별도의 앱을 설치할 필요 없이 **카카오톡만 있으면 바로 결제**가 가능합니다. 이는 사용자 경험을 크게 향상시키며, **결제 전환율**을 높이는 데 유리한 요소입니다. 토스도 마찬가지로 많은 사용자와 모바일에 친숙하지만 토스 이용자 수 보다 카카오톡을 이용하는 이용자 수가 더 높을 것으로 예상되었습니다.
    
  #### 2. 모바일 친화적인 UX/UI📱
  KakaoPay는 **모바일 환경에서 최적화된 UI/UX**를 제공하여, 사용자가 결제 과정을 더욱 직관적으로 진행할 수 있습니다. 특히 카카오톡 사용자들에게 매우 익숙한 UI를 제공하여 **사용자의 불편함을 최소화** 할 수 있습니다. 이런 점에서 **모바일 중심의 비즈니스**에 특히 유리합니다. 완성까지의 시간을 고려하여 앱이 아닌 웹으로 유저의 UI를 제작하게 되었지만 기존에 앱을 생각하며 구상하였기 때문에 더욱 적합하다 생각하였습니다.
    
  ### 3. 편리한 테스트 환경 🧪
  다른 PG사들과 달리 KakaoPay는 **심사 없이 테스트가 가능**하다는 점이 큰 장점으로 와닿았습니다.  개발 및 테스트가 용이하고, 별도의 절차 없이 바로 테스트를 시작할 수 있어 **개발 속도와 효율성**을 크게 개선할 수 있었습니다.
    
### **선택의 아쉬운 점** 😮‍💨
  #### **1. 결제 수단의 제한🚫💳**
  KakaoPay는 카카오톡 기반이라 편리하지만, **카카오톡을 사용하지 않거나 카카오페이 결제에 익숙하지 않은 사용자에게는 접근성이 떨어질 수 있습니다**. 또한, 특정 해외 결제 및 일부 신용카드 사용이 제한되는 점도 아쉬운 부분입니다.
    
  #### 2. UI 커스터마이징의 한계🎨🔒
  KakaoPay의 결제 화면은 **카카오가 제공하는 방식 그대로 사용**해야 하므로, 서비스에 맞게 자유롭게 **커스터마이징하기 어렵습니다**. 특히 PC 환경에서는 **QR코드 스캔 또는 전화번호 입력 과정이 다소 번거롭게 느껴졌습니다**.
    
  #### 3. 여러 PG사 연동의 어려움🔄🛠️
  이번 프로젝트에서는 KakaoPay만을 고려하여 개발을 진행했기 때문에 당장은 문제가 없지만, 추후 다른 결제 서비스를 추가하려면 **각각 개별로 연동해야 하는 번거로움**이 발생할 수 있습니다.
  이러한 경험을 바탕으로, 다음 프로젝트에서는 "포트원(PortOne)"을 활용하여 여러 PG사를 **하나의 API로 통합하여 연동하는 방식**을 시도할 계획입니다. 이를 통해 다양한 **결제 수단을 유연하게 추가하고, 유지보수의 부담을 줄일 수 있을 것**으로 기대됩니다.
    

</div>
</details>

<details>
<summary>🕧실시간 웨이팅 정보 관리 : Redis</summary>
<div markdown="1">

### 웨이팅 번호 및 웨이팅 소요시간 통계에 따른 실시간 대기 시간 예상 제공

| **특징** | **MySQL** | **Redis** |
| --- | --- | --- |
| **유형** | 관계형 데이터베이스 (RDBMS) | 키-값 저장소 (In-memory NoSQL DB) |
| **데이터 저장 방식** | 디스크 기반 영속성 | 메모리 기반 (옵션으로 디스크 영속성 가능) |
| **쿼리 언어** | SQL (Structured Query Language) | 자체 명령어 (SQL 비슷한 명령어 사용 가능) |
| **트랜잭션 지원** | ACID 트랜잭션 지원 | 트랜잭션 미지원 (Atomic 명령어는 지원) |
| **속도** | 상대적으로 느림 (디스크 I/O가 필요) | 매우 빠름 (메모리 기반으로 높은 성능) |
| **데이터 구조** | 테이블 기반 (정형 데이터) | 다양한 데이터 구조 (문자열, 리스트, 세트 등) |
| **영속성** | 데이터를 디스크에 영속적으로 저장 | 메모리에 저장 (영속성 옵션 있음) |
| **사용 사례** | 복잡한 관계형 데이터 모델, 트랜잭션 처리 | 캐시, 세션 저장소, 실시간 데이터 처리, 큐 시스템 |
| **확장성** | 수평 확장 어려움 (주로 수직 확장) | 수평 확장 용이 (클러스터링 지원) |
| **지원하는 기능** | 복잡한 쿼리, 조인, 인덱스, 데이터 무결성 | 빠른 데이터 접근, Pub/Sub, 리스트/셋 등 다양한 구조 |
    
### **Redis 사용 장점**
    
1. **빠른 읽기, 쓰기 속도**
  1. RScoredSortedSet, RMap을 활용해 메모리에 저장된 데이터를 조회할 수 있음
  2. MySQL을 사용했을 때보다 훨씬 빠른 데이터 접근이 가능함
  3. 자주 조회하는 현재 웨이팅 순서 및 예상 대기시간 등에서 활용
2. **TTL 설정으로 자동 데이터 정리**
  1. 시간 설정을 통해 시간이 지나면 자동 삭제 되므로 불필요한 데이터가 쌓이는 것을 방지함
  2. 메모리 사용량에 대한 부담을 줄일 수 있음
3. **실시간 정렬된 대기열 관리**
  1. 점수로 정렬되기 때문에 별도로 정렬 로직을 구현하지 않아도 됨
  2. 현재 남아있는 순번을 빠르게 조회 가능
4. **예상 대기 시간**
  1. 입장이 완료되면 소요시간을 저장
  2. 저장된 데이터를 기반으로 실시간으로 예상 대기 시간을 계산해 제공할 수 있음
5. **데이터베이스 부하 감소**
  1. MySQL에 계속해서 접근 할 필요성이 없어 데이터베이스에 대한 부하 및 쿼리 비용이 감소
    상세하고 영구적으로 저장되어야하는 상세 웨이팅 정보는 MySQL에 저장하고, 자주 조회되고 데이터에 영속성이 요구되지 않는 현재 순번 및 대기 시간 저장에 Redis 캐싱을 활용하여 각각의 장점을 살릴 수 있습니다
    
</div>
</details>

<details>
<summary>🏪실시간 가게 조회수 기능 :  SSE, Redis Pub/Sub</summary>
<div markdown="1">

서비스에서는 사용자가 특정 가게 상세 조회 페이지에 진입하면 해당 가게의 조회수가 증가하고, 페이지를 벗어나면 감소하는 동적 기능이 필요합니다. 이를 위해 여러 통신 기술(SSE, WebSocket, RabbitMQ, Kafka)을 고려하였으며, 각 기술의 장단점을 아래와 같이 비교했습니다. 또한 이 조회수를 다른 사용자에게 전달하기 위해 메시징 이벤트가 필요합니다. 이 메시징 이벤트를 구현하기 위해 Redis의 Pub/Sub 기능을 활용했습니다.
    
<img src = "https://github.com/user-attachments/assets/407487c5-a63e-45ad-bea5-cbcd1fbfadbf" width = "240"> 

    
#### 1. 기술 비교
    
  | 항목 | SSE | WebSocket | RabbitMQ | Kafka |
  | --- | --- | --- | --- | --- |
  | **장점** | - 서버에서 클라이언트로의 단방향 실시간 알림 전송에 최적화됨- HTTP 기반으로 브라우저에서 기본 지원되어 구현이 단순함- 연결 유지 오버헤드가 낮아 조회수 업데이트와 같이 빈번한 이벤트 전송에 적합함 | - 양방향 통신 지원으로 서버와 클라이언트가 자유롭게 데이터 교환 가능- 실시간성이 매우 뛰어나며 지속적인 데이터 교환이 가능함 | - 메시지 큐 기반으로 신뢰성 있는 비동기 메시징 제공- 복잡한 메시지 라우팅 및 보증 기능 우수 | - 대규모 데이터 스트림 처리에 최적화됨- 확장성과 처리량 면에서 강점을 가짐 |
  | **단점** | - 클라이언트에서 서버로의 양방향 통신이 불가능하여 양방향 데이터 교환이 필요한 경우 부적합함 | - 단순 조회수 업데이트와 같이 서버 → 클라이언트 단방향 알림만 필요한 경우, 구현 복잡성과 리소스 관리 부담이 큼 | - 단순 실시간 알림 전송보다는 비동기 작업 처리에 적합하여 설정 및 운영이 복잡하고 오버헤드가 큼 | - 설치 및 운영 부담이 크며, 단순 실시간 이벤트 전송에는 과도한 리소스 소모가 발생함 |
    
#### 2. SSE 선택 이유
    
이 서비스에서는 실시간 단방향 알림 기능만 필요하므로, SSE(Server-Sent Events)를 선택했습니다.
조회수는 단순히 증감하는 이벤트이므로 양방향 통신이 필요 없습니다.
서비스에서 조회수 기능은 “사용자가 페이지에 들어갔을 때 증가, 나갔을 때 감소”하는 이벤트로, 클라이언트로 실시간 알림을 보내면 되므로 복잡한 양방향 통신이 필요하지 않습니다.
SSE는 HTTP 기반으로 브라우저에서 기본적으로 지원되며, 추가적인 라이브러리 없이 쉽게 구현 가능합니다.
지속적인 연결 유지가 필요하지만, WebSocket처럼 복잡한 핸드셰이크 및 연결 유지 관리가 필요 없습니다.
서버에서 클라이언트로만 데이터 전송이 가능하므로, 실시간 조회수 갱신에 적합합니다.
    
#### 3. Redis Pub/Sub 활용
    
사용자가 특정 가게의 상세 조회 페이지에 들어가면 Redis의 Pub 이벤트가 발생합니다. 
해당 페이지에 이미 접속해 있는 사용자들은 Sub 이벤트를 수신하여 실시간으로 조회수를 업데이트 받습니다.
**실시간 반영**: 사용자가 페이지에 들어가거나 나갈 때마다 즉각적으로 조회수가 업데이트되어 사용자 경험을 향상시킵니다.
**경량화**: 별도의 무거운 메시지 큐 시스템 없이, Redis의 경량 Pub/Sub 기능을 통해 빠른 메시지 전달이 가능해집니다.
**확장성**: 여러 서버 인스턴스 간에도 이벤트 전달이 원활하여, 트래픽이 증가하더라도 안정적인 성능을 유지할 수 있습니다.
    
#### 4. 결론
    
실시간 가게 조회수 기능은 사용자가 현재 보고있는 가게의 인기와 경쟁도를 확인하고 예약을 촉진시킬 수 있는 기능입니다.
이 기능을 구현하기 위해서는 서버와 클라이언트가 지속적으로 연결이 유지되는 통신 기술이 필요하고 조회 수 변경 감지에 따른 조회 수 업데이트가 필요하기 때문에 메세지 이벤트 시스템이 필요했습니다.
이 요구사항에 맞게 여러 기술을 비교해보고, SSE와 Redis의 Pub/Sub 기술을 이 서비스에 적합하다고 판단하여 적용하게 되었습니다.
    

</div>
</details>

<details>
<summary>🔔 FCM : 외부 알림 기능</summary>
<div markdown="1">

### 1. 실시간 알림이 필수적인 서비스 특성
테이블링(예약) 애플리케이션은 고객과 매장 간의 원활한 소통을 위해 실시간 알림이 필수적인 서비스입니다. 예약 확정, 대기 번호 호출, 예약 취소, 방문 리마인더 등 다양한 알림을 즉시 사용자에게 전달해야 합니다. 이때, FCM(Firebase Cloud Messaging)은 안정적이고 빠른 푸시 알림 전송을 지원하여, 예약 시스템이 실시간으로 동작할 수 있도록 돕습니다.
    
### 2. 모바일 디바이스에서의 강력한 지원
테이블링 앱의 주요 사용자는 스마트폰을 사용하는 고객과 매장 관리자입니다. FCM은 Android와 iOS 플랫폼 모두에서 네이티브 푸시 알림을 지원하며, 백그라운드 및 종료된 상태에서도 알림을 전송할 수 있습니다. 이는 사용자가 앱을 실행하지 않아도 예약 상태를 실시간으로 확인할 수 있도록 도와줍니다.
    
### 3. 비용 효율성과 유지보수 편의성
FCM은 Google이 제공하는 무료 서비스로, 일정한 트래픽 내에서는 별도의 비용 없이 알림을 전송할 수 있습니다. 자체 푸시 서버를 구축하는 경우 인프라 구축 비용과 유지보수 부담이 크지만, FCM을 활용하면 이러한 부담을 줄이고 빠르게 서비스를 개발할 수 있습니다. 또한, Google이 직접 서버를 관리하므로 높은 안정성과 확장성을 보장합니다.
    
### 4. 다양한 기능 지원
FCM은 단순한 푸시 알림뿐만 아니라 다양한 기능을 제공합니다. 예를 들어:
  - **주제 기반 메시징(Topic Messaging)**: 특정 매장 또는 특정 지역 고객에게 맞춤형 알림을 보낼 수 있습니다.
  - **우선순위 설정**: 긴급한 예약 변경 알림을 즉시 전달하고, 일반적인 마케팅 알림은 배터리 절약을 고려해 예약 전송할 수 있습니다.
  - **데이터 메시지 지원**: 알림과 함께 추가 데이터를 전송하여, 앱 내에서 더욱 다양한 사용자 경험을 제공할 수 있습니다.
    
### 5. 백그라운드 알림 기능
FCM은 앱이 백그라운드 상태이거나 종료된 상태에서도 푸시 알림을 전송할 수 있습니다. 이는 사용자가 직접 앱을 실행하지 않아도 예약 상태나 대기 번호 호출 등의 중요한 정보를 받을 수 있도록 해줍니다. 특히, 예약 확정 및 변경과 같은 중요한 이벤트를 놓치지 않도록 보장하는 데 유용합니다. 이를 통해 사용자 경험을 향상시키고, 앱 재방문율을 높이는 효과도 기대할 수 있습니다.
    
### 6. 글로벌 확장성과 신뢰성
테이블링 서비스가 국내뿐만 아니라 해외 시장으로 확장할 경우, 안정적인 글로벌 인프라가 필요합니다. FCM은 Google의 데이터 센터를 기반으로 전 세계 어디서나 빠르고 안정적인 푸시 알림 전송을 지원합니다. 또한, Google Play 서비스와 연동하여 Android 기기에서 높은 전달율을 보장합니다.
    
### 7. 대체 기술과의 비교
    
| **기술명** | **플랫폼 지원** | **실시간 알림** | **백그라운드 알림** | **비용** | **주요 단점** |
| --- | --- | --- | --- | --- | --- |
| **FCM** | Android, iOS, Web | ✅ 가능 | ✅ 가능 | 무료 | Google 서비스에 의존 |
| **APNs (Apple Push Notification Service)** | iOS | ✅ 가능 | ✅ 가능 | 무료 | Android 미지원 |
| **WebSocket** | Android, iOS, Web | ✅ 가능 | ❌ 불가능 (앱이 종료되면 연결 끊김) | 서버 비용 발생 | 연결 유지 시 배터리 소모 많음 |
| **SSE (Server-Sent Events)** | Web | ✅ 가능 | ❌ 불가능 | 서버 비용 발생 | 단방향 통신만 가능 |
| **SMS** | 모든 기기 | ❌ 불가능 | ✅ 가능 | 유료 (메시지당 비용) | 비용 부담 큼 |
    
FCM과 비교할 수 있는 다른 기술로는 APNs(Apple Push Notification Service), 자체 WebSocket 서버, SMS 등이 있습니다. 하지만 APNs는 iOS 전용이며, WebSocket 기반의 실시간 알림은 지속적인 연결 유지가 필요해 배터리 소모가 크고, SMS는 비용이 많이 듭니다. 반면, FCM은 **Android와 iOS를 모두 지원하고, 배터리와 데이터 사용량을 효율적으로 관리할 수 있으며, 비용 부담이 적다는 장점**이 있습니다.
    
### 결론
테이블링 앱에서 FCM을 선택한 이유는 **실시간 푸시 알림이 필수적인 서비스 특성**, **모바일 디바이스에 대한 강력한 지원**, **비용 효율성**, **다양한 기능 제공**, **글로벌 확장성 및 신뢰성**, 그리고 **다른 기술 대비 우수한 성능** 때문입니다. FCM을 통해 사용자 경험을 극대화하고, 효율적으로 알림 시스템을 운영할 수 있습니다.

</div>
</details>

<details>
<summary>🔊 SSE : 내부 알림 기능</summary>
<div markdown="1">

우리 서비스에서는 친구 초대나 일행 초대 등 여러 알림이 발생합니다. 서버에서 클라이언트에게 해당 이벤트에 대한 메세지를 전달하는 기능이 필요합니다. 이미 FCM을 통해 디바이스로 알림을 전달하는 기능이 있지만 서비스 내부에서도 알림을 보낼 필요성을 느꼈습니다.  
그래서 가게 조회수 기능처럼 통신 기술을 비교하여 가장 적절한 기술인 SSE을 선택했습니다.
    
<img src = "https://github.com/user-attachments/assets/f5147dc0-d7ca-4914-976f-5a46e6594204" width = "240"> 

    
#### 1. 기술 비교
    
| 항목 | SSE | WebSocket | RabbitMQ | Kafka |
| --- | --- | --- | --- | --- |
| **장점** | - 서버에서 클라이언트로의 단방향 실시간 알림 전송에 최적화됨- HTTP 기반으로 브라우저에서 기본 지원되어 구현이 단순함- 연결 유지 오버헤드가 낮아 조회수 업데이트와 같이 빈번한 이벤트 전송에 적합함 | - 양방향 통신 지원으로 서버와 클라이언트가 자유롭게 데이터 교환 가능- 실시간성이 매우 뛰어나며 지속적인 데이터 교환이 가능함 | - 메시지 큐 기반으로 신뢰성 있는 비동기 메시징 제공- 복잡한 메시지 라우팅 및 보증 기능 우수 | - 대규모 데이터 스트림 처리에 최적화됨- 확장성과 처리량 면에서 강점을 가짐 |
| **단점** | - 클라이언트에서 서버로의 양방향 통신이 불가능하여 양방향 데이터 교환이 필요한 경우 부적합함 | - 단순 조회수 업데이트와 같이 서버 → 클라이언트 단방향 알림만 필요한 경우, 구현 복잡성과 리소스 관리 부담이 큼 | - 단순 실시간 알림 전송보다는 비동기 작업 처리에 적합하여 설정 및 운영이 복잡하고 오버헤드가 큼 | - 설치 및 운영 부담이 크며, 단순 실시간 이벤트 전송에는 과도한 리소스 소모가 발생함 |
    
#### 2. SSE 선택 이유
이 서비스에서는 실시간 단방향 알림 기능만 필요하므로, SSE(Server-Sent Events)을 선택했습니다.
단순히 서버에서 클라이언트에게 알림 메세지만 전달하는 기능이 필요합니다.
또한, 이미 실시간 가게 조회수 기능에서 SSE을 이용하고 있기 때문에 따로 설치 및 구현할 필요 없이
SSE을 전역적으로 설정하는 방법으로 접근이 쉬웠습니다.
    
#### 3. Redis Pub/Sub 활용
친구 초대, 일행 초대, 리뷰 등록 등과 같은 이벤트가 발생하면 메세지 처리를 할 메세지 브로커가 필요했습니다. 그래서 기존에 Redis Pub/Sub을 활용해서 특정 이벤트가 발생하면 메세지를 보낼 유저에게 메세지를 전달하게 됩니다.
    
#### 4. 결론
이미 사용하고 있는 기술을 확장하여 추가적인 기능이 필요할 때 적용한 시도가 적절했다고 생각했습니다.
서비스 내부에서 알림 기능이 추가적으로 필요하다는 요구사항을 구현하기 위해 실시간 가게 조회수 기능에서 사용하는 SSE와 Redis의 Pub/Sub 기술을 가져다가 전역으로 설정하여 적용했습니다.
이를 통해 추가적인 기술이나 라이브러리를 설치할 필요없이 알림기능을 구현할 수 있었습니다.

</div>
</details>

<details>
<summary>🖥️AWS : 인프라 구성</summary>
<div markdown="1">

### 1. 인프라 구성 요소 비교 표
    
  | **AWS 서비스** | 다른 선택 사항 | **비교 및 고려 포인트** |
  | --- | --- | --- |
  | **EC2** | Google Compute Engine, Azure Virtual Machines, 온프레미스 서버 | - **AWS EC2:** 다양한 인스턴스 유형, 손쉬운 스케일링, 풍부한 모니터링/관리 도구 제공 <br>- **대체:** 다른 클라우드의 VM도 유사 기능 제공하지만, AWS 생태계와 통합성이 뛰어남 |
| **RDS (MySQL)** | Google Cloud SQL, Azure Database for MySQL, 자체 구축 MySQL 클러스터 |- **AWS RDS:** 관리형 서비스로 백업, 패치, 확장 등이 자동화되어 운영 부담 경감<br>- **대체:** 자체 구축은 관리 복잡성이 크며, 타 클라우드 서비스와 비교 시 AWS의 안정성과 성능이 강점 |
  | **S3** | Google Cloud Storage, Azure Blob Storage, DigitalOcean Spaces |- **AWS S3:** 높은 내구성, 확장성, 정적 웹 호스팅 및 객체 저장에 최적화<br>- **대체:** 다른 스토리지 서비스들도 유사 기능 제공하지만, S3는 API 지원과 생태계 통합에서 우위를 점함 |
  | **Route 53** | Cloudflare DNS, Google Domains, 기타 외부 DNS 서비스 |- **AWS Route 53:** 글로벌 분산 DNS, 헬스 체크 및 트래픽 라우팅 정책 제공<br>- **대체:** 다른 DNS 서비스도 우수하지만, AWS 서비스와의 원활한 연계 및 관리 편의성이 주요 장점 |
  | **CloudFront** | Cloudflare CDN, Akamai, Fastly|- **AWS CloudFront:** 전 세계 엣지 로케이션을 통한 빠른 콘텐츠 전달, AWS 서비스와의 긴밀한 통합<br>- **대체:** 다른 CDN 제공업체도 있지만, AWS와의 네이티브 통합성이 결정적 선택 요인 |
  | **AWS Load Balancer** | Google Cloud Load Balancing, NGINX/HAProxy 기반 오픈 소스 로드밸런서 |- **AWS Load Balancer:** 자동 확장, 장애 복구 및 CloudFront와의 트래픽 연계 최적화<br>- **대체:** 오픈 소스 솔루션은 초기 설정 비용은 낮으나, 관리와 유지보수에 추가 리소스가 요구됨 |
    
### 2. 각 인프라 구성 요소 선택 상세 이유
    
  #### EC2
  - **선택 이유:** 스프링 부트로 개발한 서버를 배포하기 위해 선택하였습니다.
    
  #### RDS (MySQL)
  - **선택 이유:** 관리형 데이터베이스 서비스로, 서버에 필요한 데이터를 관리하기 위해 선택하였습니다. 직접 데이터베이스를 관리하는 것보다는 RDS에서 제공하는 자동 백업, 패치 적용, 모니터링, 그리고 확장 기능을 사용하여 데이터베이스 운영에 소요되는 관리 비용과 복잡성을 줄이려 하였습니다.
    
  #### S3
  - **선택 이유:** 프론트 엔드(리액트)인 웹사이트 호스팅과 이미지 및 파일 저장을 위해 사용하였습니다. 서버에서 직접 파일 처리할 필요가 없기 때문에 서버 부하를 줄일 수 있습니다. 또한, 신뢰성이 높고 용량 제한이 거의 없는 장점이 있습니다.
    
  #### Route 53
  - **선택 이유:** 도메인 등록과 글로벌 분산 DNS 관리가 가능하며, 헬스 체크 및 트래픽 라우팅 정책을 통해 안정적인 서비스 운영을 할 수 있기 때문에 선택하였습니다.
    
  #### CloudFront
  - **선택 이유:** 프론트엔드의  정적 파일을 안정적으로 제공하기 위해 선택하였습니다. CloudFront는 세계 엣지 네트워크를 통해 사용자가 위치한 지역에 상관없이 빠르고 안정적으로 콘텐츠를 제공하며, 캐싱, 보안, 실시간 로그 분석 등의 기능을 갖추고 있습니다.
    
  #### AWS Load Balancer
  - **선택 이유:** CloudFront를 통해 전달된 트래픽을 효율적으로 EC2 인스턴스에 올라간 서버에 전달하기 위해 선택하였습니다. Load Balancer는 자동 확장 및 장애 복구 기능을 제공하여 서비스의 고가용성과 안정성을 보장합니다.

---
    
### 결론
이번 인프라 구성은 AWS의 다양한 관리형 서비스를 활용하여 다음과 같은 주요 이점을 제공합니다.

- **통합 관리 및 운영 효율성:**
  AWS 내 서비스 간의 원활한 통합으로 인프라 관리 및 모니터링이 용이하며, 자동화된 확장 및 장애 복구 기능을 통해 운영 안정성이 극대화됩니다.
        
- **비용 및 관리 부담 절감:**
  관리형 서비스(예: RDS, Load Balancer)를 통해 자체 구축 대비 관리 및 유지보수에 드는 비용과 인력을 줄일 수 있습니다.
        
- **서비스 안정성 및 성능:**
  글로벌 네트워크와 자동 스케일링 기능을 통해 사용자에게 일관된 성능과 높은 가용성을 제공합니다.
        
- **생태계 통합:**
  각 요소가 서로 긴밀하게 연계되어 있어, 개발 및 운영 측면에서 보다 효율적인 시스템 구성이 가능합니다.
  이와 같이, AWS 서비스를 선택한 이유는 단순한 기능 비교를 넘어서 전체 인프라의 통합 관리, 자동화된 운영, 그리고 안정성 및 확장성 측면에서 다른 대체 요소들보다 우수한 점이 있기 때문입니다.
    

</div>
</details>

<details>
<summary>🚀Cl/CD : GitHub Actions와 Docker</summary>
<div markdown="1">

### 1. GitHub Actions 선택 이유
> 프로젝트 배포 파이프라인을 구축할 때, Jenkins와 GitHub Actions 두 가지 옵션을 검토했습니다. 여러 측면에서 비교한 결과, GitHub Actions를 선택한 주요 이유는 다음과 같습니다.
> 
| **항목** | **GitHub Actions** | **Jenkins** |
| --- | --- | --- |
| **통합 및 사용 편의성** | - GitHub 리포지토리와 완벽하게 통합되어 별도의 CI/CD 서버 구성 불필요- YAML 기반 워크플로우 설정으로 직관적이고 빠르게 학습 및 적용 가능 | - 강력한 플러그인 에코시스템과 커스터마이징 가능- 별도의 설치, 관리, 유지보수가 필요하며, GitHub와 연동 시 플러그인 추가 등 초기 설정에 더 많은 노력이 요구됨 |
| **유지보수 및 비용 효율성** | - 클라우드 기반으로 운영되어 별도 서버 관리 부담이 없음- 사용량에 따라 무료 제공 범위 적용 가능, 소규모부터 중규모 프로젝트에 비용 효율적 적용 가능 | - 자체 호스팅이 일반적이라 서버 관리, 보안 업데이트, 백업 등 추가 유지보수 작업 필요- 인프라 비용 발생 |
| **확장성과 커뮤니티 지원** | - GitHub의 활발한 커뮤니티와 다양한 오픈소스 액션 제공- GitHub 인프라를 활용한 높은 확장성 확보 | - 오랜 기간 널리 사용되어 방대한 플러그인 보유- 플러그인 간 호환성 문제나 업데이트 관리 등에서 복잡성이 증가할 수 있음 |
    
### 2. Docker 및 Docker Hub를 활용
> 배포 프로세스의 효율성과 일관성을 높이기 위해, 애플리케이션을 Docker 이미지로 빌드하고 Docker Hub에 업로드한 후, EC2 서버에서 해당 이미지를 다운로드하여 컨테이너로 실행하는 방식을 채택했습니다.
> 
  #### Docker 사용 이유
  애플리케이션을 이미지 파일로 패키징하여 환경 간 일관성을 보장
  로컬, 스테이징, 프로덕션 등 다양한 배포 환경에서 동일한 이미지를 사용할 수 있어 배포 및 유지보수가 용이
    
  #### Docker Hub 사용 이유
  중앙화된 이미지 저장소를 통해 배포 파이프라인에서 손쉽게 이미지를 관리
  CI/CD 파이프라인과의 원활한 연계를 통해 빌드된 이미지를 자동으로 푸시하고, EC2 서버에서는 이를 풀(Pull) 받아 최신 버전으로 배포 가능
   
    
### 3. 결론
   GitHub Actions를 선택함으로써, GitHub와의 완벽한 통합과 간편한 YAML 설정을 통해 CI/CD 파이프라인을 효율적으로 구축할 수 있습니다. 또한, Docker와 Docker Hub를 활용한 배포 전략은 애플리케이션의 일관된 환경 제공 및 배포 자동화를 가능하게 하여, EC2 서버에서 최신 이미지를 손쉽게 배포할 수 있도록 합니다. 이러한 기술적 의사 결정은 초기 설정과 유지보수의 부담을 줄이고, 배포 과정에서의 효율성과 확장성을 극대화하는 데 기여합니다.
    
</div>
</details>



## 🚨 트러블슈팅

<details>
<summary>⛽자동 로그인의 필요성</summary>
<div markdown="1">

## 1. 문제

초기에는 엑세스토큰만을 사용하여 로그인을 관리했는데, 엑세스토큰의 만료 시간이 짧아 사용자가 자주 로그인을 다시 해야 하는 불편함이 발생했습니다.

즉, 사용자가 로그인한 후에도 짧은 시간마다 토큰 만료로 인해 인증이 끊기고, 다시 로그인을 해야 하는 상황이 문제였습니다.


## 2. 원인

- **엑세스토큰 만료 시간**
    
    엑세스토큰은 보통 보안상의 이유로 만료 시간이 짧게 설정됩니다. 이로 인해, 사용자가 서비스를 이용하는 도중에 토큰이 만료되면 인증 상태가 풀려 다시 로그인을 요구하게 됩니다.
    
- **엑세스토큰 단독 관리**
    
    기존에는 엑세스토큰만 관리하다 보니, 토큰 갱신 로직이 없어서 만료되면 새 토큰을 발급받지 못하고 로그인이 끊기는 문제가 발생했습니다.
    


## 3. 해결책

해결책으로는 **리프레쉬 토큰(Refresh Token)**을 도입하는 것입니다.

- **리프레쉬 토큰 발급:**
    
    사용자가 로그인할 때 서버는 엑세스토큰과 함께 리프레쉬 토큰을 발급합니다.
    
- **리프레쉬 토큰 저장:**
    
    리프레쉬 토큰은 보안상의 이유로 HttpOnly 쿠키로 저장합니다. 이 쿠키는 JavaScript에서 접근할 수 없으므로, XSS 공격에 안전합니다.
    
- **엑세스토큰 저장:**
    
    엑세스토큰은 브라우저의 로컬스토리지에 저장하여 클라이언트 측에서 사용합니다.
    
- **자동 토큰 갱신:**
    
    클라이언트에서는 엑세스토큰을 사용하다가 만료되면, HttpOnly 쿠키에 저장된 리프레쉬 토큰을 사용하여 서버에 새 엑세스토큰 발급을 요청합니다. 이 과정은 axios 인터셉터 등을 통해 자동으로 처리할 수 있습니다.
    


## 4. 적용 (코드 예시)

### 4.1. 서버에서 RefreshToken 추가 발급

```java
@PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> loginUser(@Valid @RequestBody UserLoginRequestDto requestDto,
                                                     HttpServletResponse response) {

        String accessToken = userService.loginUser(requestDto);

				// 쿠키에 refresh token 담기
        response.addCookie(userService.craeteCookie(requestDto.getEmail()));

        return ResponseEntity.status(HttpStatus.OK).body(new JwtAuthResponse(AuthenticationScheme.BEARER.getName(), accessToken));
    }
```

- refresh Token 을 생성해서 쿠키에 담는 메서드 (UserService)

```java
/**
     * refresh Token 을 쿠키에 담기
     *
     * @return Cookie
     */
    public Cookie craeteCookie(String email) {

        String cookieName = "refreshToken";
        String cookieValue = jwtProvider.generateRefreshToken(email); // 쿠키벨류엔 글자제한이 이써, 벨류로 만들어담아준다.

        // refreshToken db 저장
        refreshTokenService.saveRefreshToken(email, cookieValue);

        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        // cookie.setSecure(true); //https 옵션 설정
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 설정
        return cookie;

    }
```

- Redis에 refresh Token을 저장 (만료 기간 30일)

```java
@Service
public class RefreshTokenService {

    private final RedissonClient redissonClient;

    public RefreshTokenService(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // RefreshToken 저장
    public void saveRefreshToken(String username, String refreshToken) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        bucket.set(refreshToken, Duration.ofDays(30));
    }

    // RefreshToken 가져오기
    public String getRefreshToken(String username) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        return bucket.get();
    }

    // RefreshToken 삭제
    public void deleteRefreshToken(String username) {
        RBucket<String> bucket = redissonClient.getBucket("refreshToken:" + username);
        bucket.delete();
    }

    // RefreshToken 검증
    public boolean validateRefreshToken(String username, String refreshToken) {
        String storedToken = getRefreshToken(username);
        return storedToken != null && storedToken.equals(refreshToken);
    }
}

```

### 4.2. Axios 인터셉터를 활용한 자동 토큰 갱신

```jsx
import axios from "axios";

// 🔹 Axios 인스턴스 생성
const instance = axios.create({
  baseURL: "http://localhost:8080", // Spring Boot 서버 주소
  withCredentials: true, // 쿠키 포함 요청
});

// 🔹 액세스 토큰을 가져오는 함수
const getAccessToken = () => localStorage.getItem("accessToken");

// 🔹 요청 인터셉터: 헤더에 액세스 토큰 추가
instance.interceptors.request.use(
  (config) => {
    const accessToken = getAccessToken();
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// 🔹 리프레시 토큰을 사용해 새로운 액세스 토큰을 가져오는 함수
const refreshAccessToken = async () => {
  try {
    const response = await instance.post(
      "/users/refresh",
      {},
      {
        headers: { "Content-Type": "application/json" },
        withCredentials: true, // HttpOnly 쿠키 포함
      }
    );

    const newAccessToken = response.data.accessToken;
    if (!newAccessToken) throw new Error("새로운 액세스 토큰 없음");

    localStorage.setItem("accessToken", newAccessToken);
    return newAccessToken;
  } catch (error) {
    console.error("❌ 리프레시 토큰 만료: 로그인 페이지로 이동");
    localStorage.removeItem("accessToken");
    window.location.href = "/login"; // 로그인 페이지로 리디렉션
    return null;
  }
};

// 🔹 응답 인터셉터: 401 응답 처리 (액세스 토큰 갱신 후 요청 재시도)
instance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true; // 재시도 방지 플래그 설정

      const newAccessToken = await refreshAccessToken();
      if (newAccessToken) {
        originalRequest.headers.Authorization = `Bearer ${newAccessToken}`;
        return instance(originalRequest); // 요청 재시도
      }
    }

    return Promise.reject(error);
  }
);

export default instance;

```

### 4.3 서버에서 엑세스 토큰 재발급 (UserService)

```java
/**
     * refreshToken 확인 및 accessToken 재발급
     *
     * @param email
     * @return accessToken
     */
    public String refresh(String email, String refreshToken) {

        if(!refreshTokenService.validateRefreshToken(email, refreshToken)) {
            throw new UnauthorizedException(ErrorCode.UNAUTHORIZED);
        }

        return jwtProvider.generateAccessToken(email);
    }
```


## 5. 자동 로그인 플로우 설명

1. **로그인 시 토큰 발급**
    - 사용자가 로그인하면 서버는 엑세스토큰과 리프레쉬 토큰을 함께 발급합니다.
    - 엑세스토큰은 로컬스토리지에 저장되고, 리프레쉬 토큰은 HttpOnly 쿠키에 저장됩니다.
2. **요청 시 엑세스토큰 사용**
    - 클라이언트는 axios 인터셉터를 통해 로컬스토리지의 엑세스토큰을 HTTP 헤더에 포함하여 요청합니다.
3. **엑세스토큰 만료 시**
    - 서버로부터 401 Unauthorized 응답을 받으면 axios 응답 인터셉터가 동작합니다.
4. **토큰 갱신 요청**
    - 인터셉터에서 `/users/refresh` API를 호출하여 리프레쉬 토큰을 사용해 새 엑세스토큰을 발급받습니다.
5. **새 토큰으로 재시도**
    - 발급받은 새 엑세스토큰을 로컬스토리지에 저장하고, 원래의 요청을 재시도하여 자동 로그인을 유지합니다.
6. **갱신 실패 시**
    - 새 토큰 발급에 실패하면 사용자를 로그인 페이지로 리디렉션하여 다시 로그인하도록 합니다.

## **결론**

엑세스토큰의 짧은 만료 시간 문제를 해결하기 위해 리프레쉬 토큰을 도입하였습니다.

리프레쉬 토큰은 HttpOnly 쿠키에 안전하게 저장되고, 엑세스토큰은 로컬스토리지에 저장되어 사용됩니다.

axios 인터셉터를 활용해 401 에러 발생 시 자동으로 리프레쉬 토큰을 사용해 새 엑세스토큰을 발급받아 원래의 요청을 재시도하도록 구현하여, 사용자가 자주 로그인할 필요 없이 자동 로그인이 유지되도록 해결했습니다.

</div>
</details>

<details>
<summary>🌋SSE (Server-Sent Events) 연결 오류</summary>
<div markdown="1">

### **📌 React 프로젝트에서 SSE (Server-Sent Events) 트러블 슈팅**

SSE (Server-Sent Events)를 React 애플리케이션에 적용하면서 발생한 문제들과 해결 방법을 정리


## **🚨 1. 로그인/회원가입 페이지에서도 SSE가 실행되는 문제**

### **🔍 문제 원인**

- SSE를 알림기능에 사용하기 위해 전역 페이지에 적용함.
- `SSEProvider`가 `App.js`의 `Router` 전체를 감싸고 있어 **로그인/회원가입 페이지에서도 SSE가 실행됨**.
- 로그인하지 않은 사용자에게는 불필요한 SSE 연결이 발생함.

### **💡 해결 방법**

✅ **로그인 및 회원가입 페이지는 `SSEProvider`에서 제외하고, 인증된 페이지에만 적용**

```jsx
<Router>
  <Routes>
    {/* ✅ 로그인 & 회원가입에서는 SSEProvider 제외 */}
    <Route path="/register" element={<Register />} />
    <Route path="/login" element={<Login />} />

    {/* ✅ 나머지 페이지에서는 SSEProvider 적용 */}
    <Route
      path="/*"
      element={
        <SSEProvider>
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/store/:storeId" element={<StoreDetails />} />
            {/* ...생략 */}
          </Routes>
        </SSEProvider>
      }
    />
  </Routes>
</Router
```


## **🚨 2. 중복 SSE 연결이 발생하는 문제**

### **🔍 문제 원인**

- `SSEProvider`의 `useEffect`가 여러 번 실행되어 **기존 SSE 연결을 닫지 않은 채 새로운 연결이 계속 생성됨**.
- 페이지 이동 시 **기존 SSE 연결이 유지되지 않고 새로운 연결이 생성되어 중복 발생**.

### **💡 해결 방법**

✅ **`eventSourceRef`를 `useRef`로 관리하여 중복 연결 방지**

✅ **기존 SSE 연결이 있으면 새 연결을 생성하지 않도록 조건 추가**

```jsx
onst eventSourceRef = useRef(null);

const connectSSE = () =&gt; {
  if (eventSourceRef.current) {
    console.log("⚠️ 기존 SSE 연결 존재, 중복 연결 방지");
    return;
  }

  eventSourceRef.current = new EventSourcePolyfill(
    "http://localhost:8080/notifications/subscribe",
    {
      headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      withCredentials: true,
    }
  );

  eventSourceRef.current.onopen = () =&gt; {
    console.log("✅ SSE 연결 성공");
  };

  eventSourceRef.current.onerror = () =&gt; {
    console.error("❌ SSE 연결 오류 발생, 재연결 시도...");
    eventSourceRef.current?.close();
    eventSourceRef.current = null;
    setTimeout(connectSSE, 3000); // 3초 후 재연결
  };
};

useEffect(() =&gt; {
  connectSSE();

  return () =&gt; {
    console.log("🛑 SSE 연결 해제");
    eventSourceRef.current?.close();
    eventSourceRef.current = null;
  };
}, []);
```

✅ **이제 페이지 이동 시에도 기존 SSE가 유지되며, 중복 연결이 방지됨!**


## **🚨 3. SSE 연결 끊김 시 자동 재연결되지 않는 문제**

### **🔍 문제 원인**

- SSE 연결이 끊기면 새로운 연결을 시도하지 않고 종료됨.
- 브라우저의 자동 재연결 기능이 동작하지 않는 경우가 발생함.

### **💡 해결 방법**

✅ **SSE 에러 발생 시 자동 재연결 로직 구현**

✅ **최대 3회까지 재연결을 시도하고, 실패 시 토큰 갱신 후 재시도**

```jsx
const retryCountRef = useRef(0);

const connectSSE = () =&gt; {
  if (eventSourceRef.current) {
    console.log("⚠️ 기존 SSE 연결 존재, 중복 연결 방지");
    return;
  }

  eventSourceRef.current = new EventSourcePolyfill(
    "http://localhost:8080/notifications/subscribe",
    {
      headers: { Authorization: `Bearer ${localStorage.getItem("accessToken")}` },
      withCredentials: true,
    }
  );

  eventSourceRef.current.onopen = () =&gt; {
    console.log("✅ SSE 연결 성공");
    retryCountRef.current = 0; // 재연결 카운트 초기화
  };

  eventSourceRef.current.onerror = async (error) =&gt; {
    console.error("❌ SSE 연결 오류 발생:", error);
    eventSourceRef.current?.close();
    eventSourceRef.current = null;

    if (retryCountRef.current &gt;= 3) {
      console.warn("🚨 SSE 재연결 3회 실패, 토큰 갱신 후 재시도");
      const success = await getRefreshToken();
      if (success) connectSSE();
    } else {
      setTimeout(() =&gt; {
        retryCountRef.current += 1;
        console.log(`🔄 SSE 재연결 (${retryCountRef.current}번째 시도)`);
        connectSSE();
      }, 3000);
    }
  };
};
```

✅ **이제 SSE 연결이 끊겨도 자동으로 재연결됨!**


## **🚨 4. 액세스 토큰 만료 시 401 오류가 반복되는 문제**

### **🔍 문제 원인**

- 액세스 토큰 만료 시 새로운 토큰을 받아오지 못해 SSE 연결이 실패함.
- SSE 요청 시 **만료된 액세스 토큰을 계속 사용하여 401 에러가 반복됨**.

### **💡 해결 방법**

✅ **401 오류 발생 시 자동으로 리프레시 토큰을 요청하고, 성공하면 SSE 재연결**

✅ **토큰 갱신 요청의 중복 실행을 방지하도록 `isRefreshingRef` 활용**

```jsx
const isRefreshingRef = useRef(false);

const getRefreshToken = async () =&gt; {
  if (isRefreshingRef.current) return false;
  isRefreshingRef.current = true;

  try {
    console.log("🔄 토큰 갱신 시도...");
    const response = await instance.post("/users/refresh", {}, {
      headers: { "Content-Type": "application/json" },
      withCredentials: true,
    });

    const newAccessToken = response.data.accessToken;
    if (!newAccessToken) throw new Error("새로운 토큰 없음");

    localStorage.setItem("accessToken", newAccessToken);
    console.log("✅ 토큰 갱신 성공");
    return true;
  } catch (err) {
    console.error("❌ 토큰 갱신 실패, 로그인 페이지로 이동");
    navigate("/login");
    return false;
  } finally {
    isRefreshingRef.current = false;
  }
};

eventSourceRef.current.onerror = async (error) =&gt; {
  console.error("❌ SSE 연결 오류 발생:", error);
  eventSourceRef.current?.close();
  eventSourceRef.current = null;

  if (error.status === 401) {
    const success = await getRefreshToken();
    if (success) {
      console.log("🔄 SSE 재연결 시도...");
      connectSSE();
    }
  } else {
    setTimeout(() =&gt; {
      console.log("🔄 SSE 자동 재연결...");
      connectSSE();
    }, 3000);
  }
};
```

✅ **이제 토큰이 만료되어도 자동으로 갱신되고 SSE가 재연결됨!**


## **🎯 정리**

| **문제** | **해결 방법** |
| --- | --- |
| 로그인/회원가입에서도 SSE 실행됨 | 로그인/회원가입에서는`SSEProvider`제외 |
| 중복 SSE 연결 발생 | `useRef`를 사용해 중복 연결 방지 |
| SSE가 끊겨도 자동 재연결 안 됨 | 최대 3번 재연결 후 토큰 갱신 후 재시도 |
| 액세스 토큰 만료 시 401 오류 반복 | 401 발생 시 토큰 갱신 후 SSE 재연결 |

</div>
</details>


<details>
<summary>🔍조건 검색 성능 개선</summary>
<div markdown="1">

## 1. 배경

데이터 수가 증가함에 따라 현재 `Store` 엔티티와 연관된 검색 기능에서 성능 문제가 발생하고 있습니다.

## 2. 문제

`searchStoreQuery` 메서드에서 다수의 조인과 조건 필터링이 이루어지며 응답 속도가 저하됩니다. 이로 인해 사용자가 검색 결과를 확인하는 데 시간이 오래 걸리고, 서버의 리소스 사용량이 증가하는 문제가 발생합니다.

### 2.1 원인

- **조건 검색의 비효율성**: `BooleanExpression`을 이용한 필터링에서 인덱스 활용 부족
- **반복적인 DB 접근**: Redis 캐싱 미적용으로 인해 동일한 검색이 반복적으로 DB에 접근
- **Lazy Loading으로 인한 N+1 문제 가능성**
- **복잡한 검색 필터링 로직**: 최적화가 필요


## 3. 성능 개선 방법

### 3.0 데이터 준비

- store 더미 데이터 50만 개 생성
    - `net.datafaker.Faker`를 사용하여 랜덤 데이터를 생성하고, 이를 DB에 저장합니다.

```java
package com.gotcha.earlytable.domain.store;

import com.gotcha.earlytable.domain.file.FileService;
import com.gotcha.earlytable.domain.store.entity.Store;
import com.gotcha.earlytable.domain.store.enums.StoreCategory;
import com.gotcha.earlytable.domain.store.enums.StoreStatus;
import com.gotcha.earlytable.domain.user.UserRepository;
import com.gotcha.earlytable.domain.user.entity.User;
import com.gotcha.earlytable.global.enums.RegionBottom;
import com.gotcha.earlytable.global.enums.RegionTop;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class StoreDummyDataInitializer {

    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final FileService fileService;

    private final Random random = new Random();
    private final Faker faker = new Faker(Locale.KOREAN);

    private static final int STORE_COUNT = 500_000;

    @PostConstruct
    @Transactional
    public void init() {
        List<User> users = userRepository.findAll();

        if (users.isEmpty()) {
            throw new IllegalStateException("User가 존재하지 않습니다.");
        }

        List<Store> stores = new ArrayList<>();

        for (int i = 0; i < STORE_COUNT; i++) {
            stores.add(createRandomStore(users));

            if (i % 10_000 == 0) { // 10,000개씩 배치 저장
                storeRepository.saveAll(stores);
                stores.clear();
                System.out.println(i + " 개의 데이터를 저장 완료...");
            }
        }

        if (!stores.isEmpty()) {
            storeRepository.saveAll(stores);
        }

        System.out.println("총 " + STORE_COUNT + " 개의 더미 데이터 저장 완료!");
    }

    private Store createRandomStore(List<User> users) {
        return new Store(
                faker.company().name(), // 랜덤 상점명
                faker.phoneNumber().phoneNumber(), // 랜덤 전화번호
                faker.lorem().sentence(), // 랜덤 설명
                faker.address().fullAddress(), // 랜덤 주소
                randomEnum(StoreStatus.class), // 랜덤 StoreStatus
                randomEnum(StoreCategory.class), // 랜덤 StoreCategory
                randomEnum(RegionTop.class), // 랜덤 RegionTop
                randomEnum(RegionBottom.class), // 랜덤 RegionBottom
                getRandomElement(users), // 랜덤 User
                fileService.createFile() // New File
        );
    }

    private <T extends Enum<?>> T randomEnum(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[random.nextInt(enumConstants.length)];
    }

    private <T> T getRandomElement(List<T> list) {
        return list.get(random.nextInt(list.size()));
    }
}
```

- build.gradle에 의존성 추가

```java
implementation 'net.datafaker:datafaker:2.0.2'
```

- 더미 데이터 예시

![더미데이터.JPG](attachment:84a792e2-fce2-4414-b1ea-d155901ab213:%EB%8D%94%EB%AF%B8%EB%8D%B0%EC%9D%B4%ED%84%B0.jpg)

### 3.1 인덱스 최적화

현재 `storeName`, `regionTop`, `regionBottom`, `storeCategory` 등에 대해 적절한 인덱스가 없어 DB에서 검색하는 데 시간이 오래 걸립니다.

### 해결 방법

아래와 같이 인덱스를 추가하여 검색 성능을 개선할 수 있습니다

```java
@Entity
@Table(name = "store", indexes = {
@Index(name = "idx_store_name", columnList = "storeName"),
@Index(name = "idx_region_top", columnList = "regionTop"),
@Index(name = "idx_region_bottom", columnList = "regionBottom"),
@Index(name = "idx_store_category", columnList = "storeCategory")
})
public class Store extends BaseEntity {
		
	// ...이하 생략
	
}

```

- 기본 키와 외래 키는 이미 데이터베이스에서 자동으로 인덱스를 생성함

     - JPA에서는 @Id(storeId) 와 @manyToOne(UserId), @OneToOne(fileId) 관계의 필드를 인덱스로 자동 생성

![자동 인덱스.JPG](attachment:0719b6df-55ff-4e43-a8b9-7c46529e0bb8:%EC%9E%90%EB%8F%99_%EC%9D%B8%EB%8D%B1%EC%8A%A4.jpg)

- 조건 중 검색어는 가게 이름과 대표 메뉴 이름을 LIKE 조건으로 검색하기 때문에 “%검색어” 나 “%검색어%” 처럼 정렬이 무의미한 경우는 인덱스가 의미가 없음 → 실제로 시간이 더 오래 걸림(인덱스 수정)
- 조건 중 가격은 자주 변경될 가능성이 높고, BETWEEN 조건이라 성능 개선이 크게 이루어지지 않을 가능성이 높음

### 적용 전/후 성능 비교

- 인덱스 적용 전
    
    ![인덱스 적용 전.JPG](attachment:333cbdee-85b4-45a8-a44d-85b81fdf5477:%EC%9D%B8%EB%8D%B1%EC%8A%A4_%EC%A0%81%EC%9A%A9_%EC%A0%84.jpg)
    

- 인덱스 적용 후
    
    ![인덱스 적용 후.JPG](attachment:d98b91b7-cce8-4faf-a00d-4fbea9487032:%EC%9D%B8%EB%8D%B1%EC%8A%A4_%EC%A0%81%EC%9A%A9_%ED%9B%84.jpg)
    

### 결과

![표.JPG](attachment:3bca95cb-20a5-4d50-a1fc-8ae6bb78a7e5:%ED%91%9C.jpg)

전체적으로 성능이 개선되긴 했지만 큰 지역(31,813개)과 카테고리(85,001개)처럼 많은 양의 데이터를 가져오는 상황에서는 크게 유의미한 결과를 보여주진 못하고, **작은 지역과 작은 지역(2,259개) + 카테고리(5,303개) 10% 이하의 데이터를 가져올 때는 큰 성능 향상이 있었습니다.**

### 3.2 Redis 캐싱 적용

동일한 검색이 반복적으로 발생할 경우, 캐싱이 없으면 매번 DB에서 조회해야하는 문제가 있습니다.

### 해결 방법

동일한 검색이 반복적으로 발생할 경우, DB에서 매번 조회하는 대신 Redis를 이용해 캐싱함으로써 성능을 개선할 수 있습니다.

```java
/**
     * 가게 조건 검색 메서드
     *
     * @param requestDto
     * @return
     */
    public List<StoreSearchResponseDto> searchStore(StoreSearchRequestDto requestDto) {
        String cacheKey = "store_search:" + getCacheKey(requestDto);

        // RBucket을 JsonJacksonCodec과 함께 사용
        RBucket<String> cachedResult = redissonClient.getBucket(cacheKey, JsonJacksonCodec.INSTANCE);

        Instant start = Instant.now(); // 시작 시간 기록

        // 캐시된 결과가 있으면 반환
        String resultJson = cachedResult.get();
        List<StoreSearchResponseDto> result = null;
        if (resultJson != null && !resultJson.isEmpty()) {
            try {
                result = objectMapper.readValue(resultJson, objectMapper.getTypeFactory().constructCollectionType(List.class, StoreSearchResponseDto.class));
            } catch (Exception e) {
                log.error("Error deserializing cached result", e);
            }
        }

        if (result == null || result.isEmpty()) {
            // 캐시된 결과가 없으면 DB에서 조회 후 캐시에 저장
            result = storeRepository.searchStoreQuery(requestDto);

            try {
                String resultJsonToCache = objectMapper.writeValueAsString(result); // List to JSON
                cachedResult.set(resultJsonToCache, 10, TimeUnit.MINUTES); // 캐시 만료 시간은 10분으로 설정
            } catch (Exception e) {
                log.error("Error serializing result to cache", e);
            }
        }

        Instant end = Instant.now(); // 종료 시간 기록
        long elapsedTime = Duration.between(start, end).toMillis(); // 실행 시간(ms)

        log.info("searchStoreQuery 실행 시간: {} ms, 결과 개수: {}", elapsedTime, result.size());

        return result;
    }

    // 캐시 키를 위한 핵심 파라미터들만 조합하는 메소드
    private String getCacheKey(StoreSearchRequestDto requestDto) {
        return requestDto.getSearchWord() + ":" +
                requestDto.getRegionTop() + ":" +
                requestDto.getRegionBottom() + ":" +
                requestDto.getStoreCategory();
    }
```

- 적용 전
    - 테스트 1
        
        ![캐싱 적용 전url1.JPG](attachment:a65cfb9a-aae6-4569-a20b-c1aaaec3da0e:%EC%BA%90%EC%8B%B1_%EC%A0%81%EC%9A%A9_%EC%A0%84url1.jpg)
        
        ![Redis 적용 전1.JPG](attachment:3e40976f-1370-463a-a4c5-e69e10d64a3e:8f2abd95-ebfb-42f1-8892-e7cf285dc1ac.png)
        
    - 테스트 2
        
        ![캐싱 적용 전url2.JPG](attachment:e853e10b-02b4-493f-bf15-4c933d1f80b9:%EC%BA%90%EC%8B%B1_%EC%A0%81%EC%9A%A9_%EC%A0%84url2.jpg)
        
        ![Redis 적용 전2.JPG](attachment:5ba64b2f-5733-4f72-bc0e-d4aa23e0cd5a:Redis_%EC%A0%81%EC%9A%A9_%EC%A0%842.jpg)
        

- 적용 후
    - 테스트 1 - 캐싱 후
        
        ![테스트1 캐싱 적용 후.JPG](attachment:31ce8469-d731-496f-89bc-3728a778b54b:%ED%85%8C%EC%8A%A4%ED%8A%B81_%EC%BA%90%EC%8B%B1_%EC%A0%81%EC%9A%A9_%ED%9B%84.jpg)
        
    - 테스트 2 - 캐싱 후
        
        ![테스트2 캐싱 적용 후.JPG](attachment:a0c9a2bc-d0d9-4f4e-95ec-dbd7d7b33e30:%ED%85%8C%EC%8A%A4%ED%8A%B82_%EC%BA%90%EC%8B%B1_%EC%A0%81%EC%9A%A9_%ED%9B%84.jpg)
        
    

### 결과

![표2.JPG](attachment:78beec08-76a9-47b3-afc7-0d0178162c2d:%ED%91%9C2.jpg)

**데이터를 캐싱한 결과 평균 95% 이상의 성능 개선이 이루어졌습니다.** 자주 조회되는 조건들에 경우에는 아주 효과적인 개선 방법이 될 수 있습니다. 하지만 캐시 적중률이 높지 않다면 결국에는 DB 조회가 일어날 것입니다.

또한, 모든 조회들을 캐싱에 담아두고 긴 시간동안 보관하면 메모리 부하가 발생할 것입니다. 그렇기 때문에 적절한 만료시간과 자주 조회되는 컬럼을 적용시키면 아주 효과적인 성능 개선이 이루어질 것입니다.

### **주의사항**

   - **변경 빈도 고려**: 만약 가게 데이터가 자주 변경된다면, 캐시에 저장된 데이터와 DB의 데이터 간에 불일치(데이터 정합성 문제)가 발생할 수 있습니다. 이런 경우 캐시를 자주 무효화하거나 업데이트해야 하므로, 캐시 업데이트 작업이 빈번해져 오히려 Redis에 부하를 줄 수 있습니다.
   - **캐시 적중률 고려**: 캐시 적중률이 높다면 대부분의 요청이 Redis에서 처리되어 DB 부하가 줄어들지만, 캐시 적중률이 낮으면 DB 조회가 빈번하게 발생합니다. 특히 캐시 미스가 잦은 경우, Redis와 DB 간의 네트워크 트래픽이 증가할 수 있습니다.

### 3.3 N + 1 문제 해결

`@OneToMany(fetch = FetchType.LAZY)` 관계에서 N+1 문제가 발생할 가능성이 있습니다.

### 해결 방법 1(Hibernate Batch Size 조정)

`@BatchSize(size = 100)`를 사용하여 성능을 개선할 수 있습니다.

- store 엔티티

```java
@Entity
@Table(name = "store", indexes = {
@Index(name = "idx_store_name", columnList = "storeName"),
@Index(name = "idx_region_top", columnList = "regionTop"),
@Index(name = "idx_region_bottom", columnList = "regionBottom"),
@Index(name = "idx_store_category", columnList = "storeCategory")
})
public class Store extends BaseEntity {
		
	// ...
	
	@OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "file_id", nullable = false)
  private File file;
	
	@BatchSize(size = 100)
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<Menu> menuList = new ArrayList<>();
	
	@BatchSize(size = 100)
  @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private final List<Review> reviewList = new ArrayList<>();
}

```

- file 엔티티

```java
@Getter
@Entity
@Table(name = "file")
@BatchSize(size = 100)
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long fileId;

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FileDetail> fileDetailList = new ArrayList<>();

    public File() {

    }

}
```

### 결과

![batchsize2.JPG](attachment:de8bfe48-c829-4b32-b973-7a06fada37c1:batchsize2.jpg)

![batchsize.JPG](attachment:92d5a2ab-f591-435d-8355-ef11aeaba893:batchsize.jpg)

이처럼 수십 번 발생할 쿼리를 몇 번의 쿼리로 데이터를 가져오게 됩니다.

실제로  **76번의 쿼리가 4개의 쿼리로 줄어들었습니다.**

또한 실행 시간도 약간이지만 단축되는 효과가 있었습니다.

![asdasdasdada.JPG](attachment:5cf84b39-165e-4209-9669-587268df642f:asdasdasdada.jpg)

이것은 batchsize를 적용하기 전의 실행 시간이다. batchsize를 적용 후 3% 정도 단축되었습니다.

추가 조회 실행 후에도

- batchsize 적용 전 (2차)

![2번째..JPG](attachment:a1ebcf0d-be84-4819-9f70-70a89454d524:2%EB%B2%88%EC%A7%B8..jpg)

- batchsize 적용 후 (2차)

![2번째-적용후..JPG](attachment:baf8a2f3-49ec-4bb8-9bc2-a99d8bf51d8d:2%EB%B2%88%EC%A7%B8-%EC%A0%81%EC%9A%A9%ED%9B%84..jpg)

약 10% 정도 단축되는 것을 볼 수 있습니다. 

**이를 통해 batchsize를 알맞은 상황에 적용하면 쿼리도 최적화를 하고 성능도 개선할 수 있다는 것을 알 수 있습니다.**

### 해결 방법 2(fetchType 설정)

가게를 조회할 때 항상 사용되는 필드는 조인해서 하나의 쿼리에서 데이터를 가져오도록 설정합니다.

![lazy.JPG](attachment:04197784-1cd2-4102-8a43-b57ab510544e:lazy.jpg)

![eager.JPG](attachment:dbc15492-3454-4c5b-bd14-af8822a96273:eager.jpg)

### 결과

왼쪽은 fetchType 이 Lazy 일 때 쿼리가 3번 발생하는 상황이고, fetchType 을 Eager로 설정해서 1번의 쿼리만 발생하는 상황입니다. 항상 같이 조회되는 데이터의 경우는 조인을 통해 같이 가져오는 방법이 성능 개선이 도움을 줄 수 있습니다.


## 4. 결론

- **인덱스 최적화, 데이터 캐싱, N+1 문제 해결**을 통해 성능 개선을 이룰 수 있었습니다.
- 특히 **인덱스 최적화와 Redis 캐싱**이 성능 향상에 중요한 역할을 했습니다.
- 이러한 최적화 방법들은 각 상황에 맞게 사용하면, 시스템 성능을 크게 개선할 수 있습니다.

| 개선 방법 | 성능 향상 비율 | 결과 |
| --- | --- | --- |
| 인덱스 최적화 | 10% ~ 80% 개선 | 작은 데이터에서 큰 성능 향상 |
| Redis 캐싱 | 95% 개선 | 반복적인 조회에 매우 효과적 |
| N+1 문제 해결 | 3% ~ 10% 개선 | 불필요한 쿼리 수를 줄여 성능 개선 |
| FetchType 조정 | 20% 개선 | 하나의 쿼리로 데이터 조회 |

## 5. 추후 고려 사항

### 5.1  Elasticsearch 도입 고려

검색 성능을 더욱 향상하기 위해 **Elasticsearch** 도입을 고려.

- 빠른 텍스트 검색 가능
- 복합적인 필터링 및 랭킹 기능 제공

### 5.2 인덱스 추가

검색 성능을 위해 더 다양한 인덱스 추가 고려

- 알러지에 대한 인덱스 추가
- 복합 인덱스 추가

</div>
</details>

<details>
<summary>🧾웨이팅 번호 발급 성능 개선</summary>
<div markdown="1">

## 1. 문제 배경 및 기존 방식의 한계

### 기존 DB 기반 방식

초기 구현에서는 데이터베이스에서 직접 쿼리를 수행하여 "실시간 웨이팅 순서"를 계산했습니다.

예를 들어, 아래와 같이 특정 가게와 날짜, 그리고 본인의 웨이팅 번호보다 앞에 있는 대기 중인 팀의 수를 DB에서 직접 카운트하는 방식입니다.

```java
/**
 * 실시간 웨이팅 순서 조회 메서드
 *
 * @param waitingId
 * @return WaitingNowSeqNumberResponseDto
 */
public WaitingNumberResponseDto getNowSeqNumber(Long waitingId) {

    Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

    // 웨이팅 날짜만 가져오기
    LocalDate waitingDate = waiting.getCreatedAt().toLocalDate();

    // 앞에 대기중인 웨이팅 개수 가져오기
    int nowSeqNum = waitingRepository.countByStoreAndWaitingStatusAndCreatedAtBetweenAndWaitingNumberLessThanEqual(
            waiting.getStore(),
            WaitingStatus.PENDING,
            waitingDate.atTime(0, 0, 0), waitingDate.atTime(23, 59, 59),
            waiting.getWaitingNumber()
    );

    return new WaitingNumberResponseDto(nowSeqNum);
}

```

**문제점:**

- **DB 부하 증가:** 실시간으로 사용자의 순서를 확인할 때마다 복잡한 조건을 가진 쿼리를 실행하기 때문에, 사용자가 많아질 경우 DB에 큰 부하가 발생합니다.
- **성능 이슈:** 매번 DB를 조회함으로써 응답 속도가 느려질 수 있으며, 동시 요청이 많은 경우 트래픽 폭주 상황에서 병목 현상이 발생할 수 있습니다.


## 2. Redis 기반 개선 방식

### Redis와 Redisson을 활용한 설계

Redis를 활용하면 메모리 기반 데이터 저장소의 장점을 살려 실시간 조회 및 업데이트를 빠르게 처리할 수 있습니다. Redisson 라이브러리를 이용하여 Java 애플리케이션에서 Redis의 자료구조(특히, 정렬된 집합)를 효과적으로 사용할 수 있도록 구현했습니다.

### 주요 개선 포인트

- **빠른 읽기/쓰기:** Redis는 메모리 내 데이터 처리를 통해 낮은 지연 시간으로 실시간 순서 조회에 적합합니다.
- **TTL (Time-To-Live) 설정:** 대기열에 TTL을 설정하여 일정 시간이 지난 데이터는 자동으로 삭제되도록 함으로써, 메모리 낭비를 줄이고 데이터의 신선도를 유지합니다.
- **정렬된 집합 사용:** RScoredSortedSet을 이용해 대기열에 등록된 웨이팅 항목을 정렬된 형태로 관리하고, 이를 통해 순위(rank)를 효율적으로 계산할 수 있습니다.


## 3. 코드 상세 분석

### 3.1. 웨이팅 큐에 등록하는 메서드: `addToWaitingQueue`

```java
public void addToWaitingQueue(Waiting waiting) {
    String key = "waiting:store:" + waiting.getWaitingType().name() + ":" + waiting.getStore().getStoreId();
    RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

    // 웨이팅 번호를 score 로 추가 (혹시라도 있을 중복을 위해 날짜로 번호 구분)
    waitingQueue.add(LocalDate.now().getDayOfYear() * 1000000L + waiting.getWaitingNumber(), waiting.getWaitingId());

    // TTL 설정 (6시간 후 자동 삭제)
    waitingQueue.expire(Duration.ofHours(6));
}
```

**설명:**

- **Key 구성:** 대기열 키는 `waiting:store:[웨이팅타입]:[가게ID]` 형식으로 생성하여, 가게와 웨이팅 타입에 따라 분리된 저장소를 사용합니다.
- **Score 계산:** score는 오늘 날짜의 일자(예, `dayOfYear`)에 대기 번호를 곱하여 생성합니다. 이 방식은 동일한 번호가 중복 등록되는 상황을 방지하고, 정렬 기준으로 활용됩니다.
- **TTL 설정:** 해당 대기열은 6시간 후 자동으로 삭제되도록 설정하여 오래된 데이터가 남지 않도록 관리합니다.

### 3.2. 현재 순서를 조회하는 메서드: `getNowSeqNumber`

```java
public Integer getNowSeqNumber(Waiting waiting) {
    String key = "waiting:store:" + waiting.getWaitingType() + ":" + waiting.getStore().getStoreId();
    RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

    // 현재 웨이팅의 번호 조회
    Integer rank = waitingQueue.rank(waiting.getWaitingId());
    if (rank == null) {
        return 0;
    }
    // 0-indexed 이므로 1을 더해 실제 순서 계산
    return rank + 1;
}
```

**설명:**

- **순위 계산:** `rank()` 메서드는 해당 웨이팅 ID가 정렬된 집합 내에서 몇 번째에 위치하는지를 0부터 계산합니다. 따라서 사용자에게 보여줄 순서는 `rank + 1`이 됩니다.
- **결과 반환:** 만약 해당 대기 항목이 존재하지 않는다면 0을 반환하여 예외 상황을 처리합니다.

### 3.3. 대기열에서 제거하는 메서드: `removeFromWaitingQueue`

```java
public void removeFromWaitingQueue(Waiting waiting) {
    String key = "waiting:store:" + waiting.getWaitingType() + ":" + waiting.getStore().getStoreId();
    RScoredSortedSet<Long> waitingQueue = redissonClient.getScoredSortedSet(key);

    // Redis 의 대기열에서 해당 웨이팅 ID 제거
    waitingQueue.remove(waiting.getWaitingId());
}
```

**설명:**

- 해당 웨이팅 항목이 처리되었거나 취소되었을 경우, Redis 대기열에서 제거하여 실시간 순위 계산에서 제외합니다.

### 3.4. 남은 팀 수 저장 메서드: `saveWaitingLeft`

```java
public void saveWaitingLeft(Waiting waiting) {
    String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":left";
    RMap<Long, Integer> waitingLeftMap = redissonClient.getMap(key);

    Integer leftNow = getNowSeqNumber(waiting);
    waitingLeftMap.put(waiting.getWaitingNumber(), leftNow);
}
```

**설명:**

- **Map 사용:** 특정 가게와 웨이팅 타입에 대한 남은 대기 팀 수를 별도의 Redis Map에 저장합니다.
- **저장 방식:** 웨이팅 번호를 key로, 현재 순위를 value로 저장하여 나중에 통계나 모니터링 용도로 활용할 수 있습니다.

### 3.5. 대기 소요 시간 관련 메서드

### 3.5.1. 1팀 당 소요 시간 저장: `saveTakenTimeWaiting`

```java
public void saveTakenTimeWaiting(Long waitingId) {
    Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);
    Integer takenTime = (int) Duration.between(waiting.getCreatedAt(), waiting.getModifiedAt()).toMinutes(); // 등록 - 입장 소요시간

    String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";
    RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

    String leftKey = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":left";
    RMap<Long, Long> waitingLeftMap = redissonClient.getMap(leftKey);

    Long left = waitingLeftMap.get(waiting.getWaitingId());
    timeQueue.add((int) (takenTime / left), waiting.getWaitingId()); // 1팀 당 소요시간 저장

    // 데이터 수가 많아지면 오래된 데이터 제거
    if (timeQueue.size() > 150) {
        timeQueue.remove(0);
    }
}
```

**설명:**

- **소요 시간 계산:** 등록 시각과 입장 시각의 차이를 계산하여, 전체 소요 시간을 구합니다.
- **평균 계산:** 전체 소요 시간을 현재 대기 팀 수(저장된 left 값)로 나누어 1팀 당 소요 시간을 구합니다.
- **데이터 관리:** 일정 크기(150개)를 초과하면 오래된 데이터를 제거하여 메모리 사용량을 관리합니다.

### 3.5.2. 소요 시간 삭제: `deleteTakenTimeWaiting`

```java
public void deleteTakenTimeWaiting(Waiting waiting) {
    String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";
    RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

    timeQueue.remove(waiting.getWaitingId());
}
```

**설명:**

- 처리된 웨이팅 항목의 소요 시간 데이터를 정리하기 위한 메서드입니다.

### 3.5.3. 예상 대기 시간 조회: `getTakenTimeWaiting`

```java
public Integer getTakenTimeWaiting(Waiting waiting) {
    String key = "waiting:store:" + waiting.getStore().getStoreId() + ":" + waiting.getWaitingType() + ":time";
    RScoredSortedSet<Long> timeQueue = redissonClient.getScoredSortedSet(key);

    long leftNow = getNowSeqNumber(waiting);
    int sum = 0;
    int time;

    if (timeQueue.size() < 10) {
        time = 15; // 데이터가 충분하지 않을 경우 기본값 15분
    } else {
        for (Long num : timeQueue) {
            sum += num.intValue();
        }
        time = sum / timeQueue.size();
    }
    return time * (int) leftNow;
}
```

**설명:**

- **데이터 안정성:** 데이터 포인트가 충분하지 않은 경우 기본값(15분)을 적용합니다.
- **평균 소요 시간:** 저장된 각 웨이팅 항목의 1팀 당 소요 시간을 평균내어, 이를 현재 대기 팀 수와 곱해 전체 예상 대기 시간을 산출합니다.

### 3.6. 최종 결과 통합: `getNowSequenceAndTime`

```java
public WaitingSequenceDto getNowSequenceAndTime(Long waitingId) {
    Waiting waiting = waitingRepository.findByIdOrElseThrow(waitingId);

    // 현재 나의 순서 조회
    Integer nowSeqNumber = getNowSeqNumber(waiting);

    // 예상 대기 시간 조회
    Integer waitingTime = getTakenTimeWaiting(waiting);

    return new WaitingSequenceDto(nowSeqNumber, waitingTime);
}
```

**설명:**

- DB에서 웨이팅 정보를 불러온 후, Redis에 저장된 데이터(순서 및 소요 시간)를 활용하여 사용자에게 실시간 순서와 예상 대기 시간을 함께 제공합니다.
- 이로써 DB 부하를 최소화하면서도, 사용자에게 빠르고 정확한 정보를 제공할 수 있습니다.

---

## 4. 전체 코드 동작 흐름

1. **등록 시 처리:**
    - 사용자가 웨이팅에 등록하면 `addToWaitingQueue`를 호출하여 Redis의 정렬된 집합에 등록합니다.
    - 동시에 현재 남은 팀 수를 `saveWaitingLeft` 메서드로 Redis Map에 저장합니다.
2. **순서 및 예상 대기 시간 조회:**
    - 사용자가 자신의 순서와 예상 대기 시간을 조회할 때 `getNowSequenceAndTime` 메서드를 호출합니다.
    - 내부적으로 `getNowSeqNumber`로 현재 순위를, `getTakenTimeWaiting`으로 대기 시간을 계산합니다.
3. **완료/취소 시 처리:**
    - 웨이팅이 완료되거나 취소되면, `removeFromWaitingQueue`와 `deleteTakenTimeWaiting`을 통해 관련 데이터를 Redis에서 정리합니다.

---

## 5. 결론

Redis를 활용한 이번 개선 방식은 다음과 같은 장점을 제공합니다.

- **실시간 응답 속도 개선:** 메모리 기반의 정렬된 집합을 사용하여, 사용자에게 빠른 순서 계산 및 예상 대기 시간 제공이 가능해졌습니다.
- **DB 부하 완화:** DB 대신 Redis를 통해 많은 계산 및 조회 작업을 처리함으로써, DB 서버의 부하를 크게 줄일 수 있습니다.
- **데이터 관리:** TTL, 데이터 수 제한 등의 전략을 통해 Redis 메모리 사용을 효율적으로 관리합니다.

이와 같이 Redis 기반의 대기열 관리 방식을 활용하면, 대규모 사용자가 동시에 접근하는 상황에서도 안정적이고 빠른 실시간 정보 제공이 가능해집니다.

</div>
</details>

<details>
<summary>🚨예약 동시성 제어</summary>
<div markdown="1">

## 문제 정의

Redisson의 분산락을 이용하여 구현한 예약 파트를 여러 건의 유저가 동시에 예약을 한다는 가정을 두고 부하 테스트를 진행하던 중, 낮은 건수의 예약에는 요청의 누락 없이 예약이 생성되었으나, 1만 건의 예약을 생성하는 부분에서 예약 생성의 누락이 발생하는 현상을 발견하였습니다. 이는 동시 예약 요청 시 예약 생성이 일부 누락되는 문제로, 특히 높은 부하가 걸린 경우에만 발생했습니다.

## 발생 원인

- 예약 생성 시 Redisson의 분산락을 이용하여 여러 사용자들의 예약 요청을 동기화하려고 했으나, 예약 요청이 동시 다발적으로 몰렸을 때 락을 획득하지 못한 경우가 발생했습니다.
- Redisson의 분산락은 기본적으로 스핀락처럼 지속적으로 락을 획득할 때까지 기다리지 않습니다. 락을 획득하지 못한 경우 일정 시간 동안 대기한 후 타임아웃이 발생합니다. 기존 코드에서는 락을 획득하지 못하면 바로 예외가 발생하도록 설정되어 있었고, 이로 인해 락을 획득하지 못한 예약 요청이 실패 처리되었으며, 결과적으로 예약이 누락되는 현상이 발생했습니다.

## 해결 방안

- **락을 얻지 못한 경우 재시도 횟수와 재시도 요청까지의 대기시간 지정**: 락을 획득하지 못한 경우 재시도 횟수와 재시도 대기 시간을 점진적으로 늘려가며 처리할 수 있는 방식으로 개선하였습니다. 이를 통해 락을 획득할 수 있는 기회를 더 많이 제공하고, 동시 예약 요청의 부하를 효과적으로 처리할 수 있습니다.
    - **재시도 횟수**: 락을 획득할 때까지 최대 50번 재시도를 시도하고, 각 재시도마다 일정한 시간 간격을 두어 부하를 분산시킵니다. 재시도 횟수는 상황에 맞게 조절 가능합니다.
    
    - **지수 백오프 방식(Exponential Backoff)**: 초기 100ms의 대기 시간에서 시작하여, 락을 획득하지 못한 경우 점차적으로 대기 시간을 증가시킵니다. 이를 통해 시스템이 과도한 부하를 받지 않도록 방지할 수 있습니다. 최대 대기 시간은 1600ms로 설정하여 지나치게 긴 대기 시간을 방지하고, 재시도 횟수가 초과되었을 때는 예외 처리를 통해 빠르게 종료할 수 있게 합니다.
    
    - **타임아웃 설정**: 재시도 횟수와 대기 시간을 초과할 경우, `LOCK_TIMEOUT` 예외를 발생시켜, 사용자에게 명확한 실패 메시지를 제공하고, 예약 요청이 제대로 처리되지 않았음을 알려줍니다. 이 예외 처리는 시스템의 부하를 줄이고, 사용자가 시스템에 과도한 부하를 주지 않도록 도와줍니다.
    
    - **락을 얻지 못한 예약 요청의 예외 처리**: 락을 획득할 수 없는 상황에 대비하여, 예외를 발생시키고 이를 적절히 처리하는 로직을 추가하여 안정성을 높입니다. 락을 얻지 못한 요청은 즉시 실패 처리가 되어, 다른 예약이 영향받지 않도록 하여 시스템의 안정성을 유지할 수 있습니다.

## 결과

예약 시스템에 대한 재시도 로직 및 락 획득 방식 개선 후, 여러 건의 동시 예약 요청을 처리하는 과정에서 발생했던 예약 누락 문제는 해결되었습니다. 특히, **1만 건 이상의 예약**을 처리할 때 발생했던 성능 저하 및 누락 현상이 크게 개선되었습니다.

![image](https://github.com/user-attachments/assets/6e0101b4-c0c8-48c7-8001-ce9996766bc4)

예약 시스템에 대한 재시도 로직 및 락 획득 방식 개선 후, 여러 건의 동시 예약 요청을 처리하는 과정에서 발생했던 예약 누락 문제가 해결되었습니다. 

## 결론

- 개선 방안은 동시 예약 요청을 처리하는 과정에서 발생할 수 있는 락 충돌 및 예약 누락 문제를 해결하는 데 중요한 역할을 했습니다. 재시도 로직과 타임아웃 설정을 통해 시스템의 안정성을 높였고, 부하 테스트를 통해 해당 로직이 높은 부하에서도 효과적으로 작동하는지 확인했습니다. 앞으로는 추가적인 최적화 작업과 성능 분석을 통해 시스템의 성능을 더욱 개선할 수 있을 것입니다.

</div>
</details>

<details>
<summary>🔒DB락 vs Redisson 분산락</summary>
<div markdown="1">
  
## Lock의 사용을 고려하게 된 이유

### 동시 요청 처리

- **일어날 수 있는 문제** : 예약 시스템에서 여러 사용자가 동시에 요청을 할 수 있습니다. 이때 동시에 같은 시간대에 대한 예약을 여러명의 사용자가 요청을 한 경우 이를 처리하며 충돌이 일어나거나 예약 누락이 발생할 수 있습니다.

- **사용 이유** : Lock을 사용하면 동시에 여러 요청이 들어올 때, 첫 번째 요청이 처리되고 나서야 두번째 요청을 처리할 수 있또록 순차적으로 처리할 수 있습니다. 이를 통해 중복 예약을 방지하고, 예약 시스템이 일관된 상태를 유지할 수 있습니다.

## DB락 vs Redisson 분산락 비교

| 특성 | DB락(비관적 락) | Redisson 분산락 |
| --- | --- | --- |
| 목적 | 동시성 문제 해결 | 분산 환경에서의 동시성 문제 해결 |
| 작동방식 | SELECT FOR UPDATE 등으로 DB에서 락 | Redis를 이용하여 분산 락 관리 |
| 동작환경 | 단일 데이터베이스 시스템에서 주로 사용 | 분산 시스템에서 사용 |
| 성능 | DB 성능에 영향 가능 | 빠르고 효율적, 네트워크 의존적 |
| 락의범위 | DB 내 특정 레코드나 테이블 | Redis 서버에서 관리 |
| 타입 | 비관적 락(Pessimistic Locking) | 분산 락(Distributed Lock) |
| 내구성 | DB 내구성 보장 | 기본 메모리 기반, 영속성 설정 가능 |
| 사용예제 | 단일 DB환경에서 데이터 보호 | 분산 시스템에서 락 관리 |

## Lock을 이용한 예약 요청 처리 시간

![image](https://github.com/user-attachments/assets/6561b9d0-d806-40f1-9429-acf8099d15ee)


이 차트는 다양한 예약 건수에 대해 Redisson의 분산 락을 이용한 예약 요청 처리 시간을 나타냅니다. 각 회차의 시간은 서버가 락을 획득하고 예약을 처리하는 데 소요된 시간을 기준으로 측정되었습니다

낮은 건수의 요청에서는 큰 차이를 보이지 못하지만 당장 1만건의 예약을 처리하는데에만 해도 큰 차이를 보이고 있습니다. 
1만건의 예약 요청을 처리하는데 DB락 대신 Redisson의 분산락을 이용하여 31.2% 향상된 처리속도를 확인할 수 있었습니다.

</div>
</details>


## 👩‍👩‍👧‍👧 팀 소개

| **팀원명** | **포지션** | **담당(개인별 기여점)** | **깃허브 링크** |
| --- | --- | --- | --- |
| **손민석** | 리더 | - **프로젝트 기본 구성** (각종 Entity 및 연관관계 설정) <br> - **가게 예약** (예약 CRUD, Redisson을 활용한 중복 예약/결제 방지, RAtomicLong으로 인원 제한) <br> - **일행 관리** (CRUD, 초대 및 추방 기능) <br> - **관심 가게** (CRUD) <br> - **프론트 화면** (가게 예약 → 결제 → 완료 화면, 가게 조건 검색, 테블릿 웨이팅 화면) <br> - **결제** (카카오페이 API 연동) | [GitHub](https://github.com/MinSeok3796?tab=repositories) |
| **김세원** | 부리더 | - **AWS 인프라 구성** (CloudFront, Route53, VPC, ALB, S3 배포, EC2 서버 배포) <br> - **CI/CD** (GitHub Actions, Docker, AWS EC2 배포) <br> - **인증/인가** (Spring Security, JWT 인증) <br> - **자동 로그인** (AccessToken & RefreshToken) <br> - **실시간 조회수 알림** (SSE, Redis Pub/Sub 활용) <br> - **서비스 내부 알림** (SSE, Redis Pub/Sub) <br> - **가게 관리** (CRUD, 대기중 가게 관리, 영업시간/휴무 설정) <br> - **키워드 관리** (키워드 CRUD, 가게 등록) <br> - **이미지 관리** (AWS S3) <br> - **유저 프론트 화면** (로그인, 회원가입, 관심 가게, 예약/웨이팅 목록, 가게 리뷰 작성) <br> - **조건 검색 성능 개선** (인덱스, Redis 캐싱, N+1 문제 해결) | [GitHub](https://github.com/taketheking) |
| **주은수** | 팀원 | - **웨이팅 관리** (CRUD, Redis 기반 대기열 및 예상 대기시간) <br> - **가게 검색** (JPAQueryFactory 조건 검색) <br> - **유저 프론트 화면** (내 정보 조회/수정, 비밀번호 변경, 예약 화면, 마이페이지 CSS, 친구 관리 CSS, 리뷰 관리 CSS) <br> - **사장님 프론트 화면** (로그인, 회원가입, 가게 관리, 웨이팅 설정 및 실시간 관리) | [GitHub](https://github.com/dmsdmst) |
| **이유진** | 팀원 | - **메뉴 관리** (CRUD, 알러지 등록 및 관리) <br> - **리뷰 관리** (CRUD) <br> - **친구 관리** (친구 및 요청 관리) <br> - **FCM 알림 기능** <br> - **유저 프론트 화면** (마이페이지, 회원탈퇴, 내 리뷰 관리, 가게 리뷰 조회, 친구 관리, 일행 관리) <br> - **사장님 프론트 화면** (메뉴 및 알러지 관리, 리뷰 관리, 예약 설정, 실시간 예약 관리) | [GitHub](https://github.com/yujlee31) |

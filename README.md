# 🌗 RED & BLUE 스프링에 ChatGPT API 사용하기 
<img width="700" alt="RED & BLUE" src="https://github.com/user-attachments/assets/f05fd1d0-f838-4ce5-b53d-1eb53e977515">
<br/>

## 📮 관련 링크 주소
> 유튜브 : [프로젝트소개 바로가기](https://youtu.be/xuX7EdBltCo)<br>
> RED&BLUE  노션 : [노션 바로가기](https://buttered-uranium-6ec.notion.site/Red-Blue-4f5bc894cd674fce88a16989a82602cc?pvs=4)<br>

## 📽️ 프로젝트 간략소개

### 🂱 ChatGPT를 활용한 토론 프로그램

- 이 프로젝트는 찬성 vs 반대, 진보 vs 보수, 남성 vs 여성 등 다양한 갈등 상황에서 논리적인 의견 공유와 민주적 결론 도출을 목표로 하는 토론 프로그램입니다. 건강한 대화 문화를 조성하고, 갈등을 건설적인 토론으로 전환하기 위해 ChatGPT를 활용하여 개발되었습니다.


### 🂲 기존 댓글 시스템의 문제점과 OpenAI를 활용한 개선방안

- **문제점:** 온라인상의 어떤 주제나 컨텐츠에 대한 댓글에는 감정적 표현과 논리적 주장이 뒤섞여 사용자들이 주장하는 문체나 논리구조가 일관되지 않아 타인의 주장을 이해하기 어려운 경우가 많습니다.  
- **개선방향:** 토론주제 작성자는 사용자들에게 다양한 형식의 의견을 RED와 BLUE로 나누어 받은 후 ChatGPT에 정리를 요청하면 일정한 조건으로 글의 형태를 재구성하여 하나의 통일된 문단으로 정리합니다. 

### 🂳 기대효과

- 다양한 의견을 정리하고 통합함으로써 감정적 대립을 줄이고 논리적 주장을 강화하는 토론 환경을 제공합니다.
- 사용자의 의견이 명확하고 일관된 형식으로 표현되기 때문에, 건강한 의견 교환과 상호 존중을 기반으로 한 대화를 촉진합니다.


## 🖨️ 기술 스택
<h3 align="center"> 개발 / 테스트 </h3>

<p align="center">
<img src="https://img.shields.io/badge/Java 17-008FC7?style=flat-square&logo=Java&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Gradle-02303A?style=flat-square&logo=Gradle&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring-58CC02?style=flat-square&logo=Spring&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=flat-square&logo=Spring Boot&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Spring Data JPA-ECD53F?style=flat-square&logo=JPA&logoColor=white"/></img>
<img src="https://img.shields.io/badge/Query_DSL-0769AD?style=flat-square&logo=querydsl&logoColor=white"/></img>
<img src="https://img.shields.io/badge/JUnit5-25A162?style=flat-square&logo=JUnit5&logoColor=white"/></img>
</p>

<h3 align="center"> DB </h3>

<p align="center">
<img src="https://img.shields.io/badge/MySQL 8.0-4479A1?style=flat-square&logo=MySQL&logoColor=white"/></img>
<img src="https://img.shields.io/badge/H2-008FC7?style=flat-square&logo=Java&logoColor=white"/></img>
</p>


<h3 align="center"> 문서 / 협업 / 툴</h3>

<p align="center">
<img src="https://img.shields.io/badge/Swagger-85EA2D?style=flat-square&logo=Swagger&logoColor=white"/>
<img src="https://img.shields.io/badge/Notion-000000?style=flat-square&logo=Notion&logoColor=white"/>

<img src="https://img.shields.io/badge/Git-F05032.svg?style=flat-square&logo=Git&logoColor=white"/>
<img src="https://img.shields.io/badge/GitHub-181717.svg?style=flat-square&logo=GitHub&logoColor=white"/>
<img src="https://img.shields.io/badge/Postman-FF6C37.svg?style=flat-square&logo=Postman&logoColor=white"/>
<img src="https://img.shields.io/badge/Gather-0043CE.svg?style=flat-square&logo=Gather&logoColor=white"/>

</p>

<br/><br/>


## 💾 ERD
<img alt=".png" src="https://github.com/user-attachments/assets/6af381fd-0263-490e-91dc-826d224cab8a" width="700"/>
<br>

## 📋 API 명세서
<img alt=".png" src="https://github.com/user-attachments/assets/ffe119dd-a61d-49a2-b7d6-23124f686d2e" width="700"/>

## 🎯 트러블 슈팅
> - [Open AI 키 발급 문제]()<br>
> - [DTO사용]()<br>


## 📦 디렉토리 구조
<details>
<summary>디렉토리 구조</summary>
<div markdown="1"></div>
<pre>
com
└─ jinho
   └─ randb
      
</pre>
</details>


## 💬Convention
- 커밋 메시지
    1. 적절한 커밋 접두사 작성
    2. 커밋 메시지 내용 작성

| Tag Name | Description                                                    |
|----------|----------------------------------------------------------------|
| feat     | 새로운 기능                                                     |
| refactor | 프로덕션 코드 리팩토링                                          |
| docs     | 문서 수정                                                       |
| test     | 테스트 코드, 리펙토링 테스트 코드 추가, Production Code(실제로 사용하는 코드) 변경 없음     |
| chore    | 빌드 업무 수정, 패키지 매니저 수정, 패키지 관리자 구성 등 업데이트, Production Code 변경 없음 |
| remove   | 프로덕션 코드 삭제 및 디렉터리 삭제                            |
| rename   | 디렉터리 이름변경 및 폴더 이동                                 |
| fix      | 버그수정                                                        |

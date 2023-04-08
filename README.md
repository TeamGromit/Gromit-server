## 화면
![gromit 어플 사진](https://user-images.githubusercontent.com/77667212/230550958-e73d7db1-3104-4f4f-b279-1fad3630a0e6.png)



## 기획서 
https://docs.google.com/viewer?url=https://github.com/LHS-11/Gromit-server/files/10954918/Gromit.pm_.pdf?raw=True

## 시스템 구상도

![image](https://user-images.githubusercontent.com/77667212/224091705-41040fb6-2190-463a-88bb-4c32887c8080.png)

## ERD

![image](https://user-images.githubusercontent.com/77667212/224091864-381d033e-6280-4e0e-acfe-c443821e27e9.png)

## 기여한 부분
- Spring Security 와 JWT 토큰을 사용한 애플 로그인 및 회원가입 API 구현
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  처음으로 구현하는 Spring Security와 JWT 토큰 방식이었기 때문에, 공식 문서와 강의를 통해 Spring Security 필터의 내부 구조를 조사하고 Notion에 여러 시큐리티 필터의 역할과 인증 방식을 정리하고 그림을 그려 관계를 이해하는 데 많은 노력을 기울였습니다. 이를 통해 Spring Security에서 JWT 토큰을 검증하는 로직을 커스텀하게 구현할 수 있었습니다. 결과적으로 Spring Security와 JWT 토큰의 동작 원리를 이해할 수 있게 되어, 보안 기능을 개발할 때 보다 자신 있게 접근할 수 있게 되었습니다.

  </div>
  </details>
- @RestControllerAdvice 을 이용한 CustomException Handling 구현
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  애플리케이션 전역에서 예외 처리를 일관적으로 하기 위해 @RestControllerAdvice를 이용하여 CustomException을 구현하였습니다. 이를 통해 코드의 가독성을 높일 뿐만 아니라, 중복된 예외 처리 코드를 줄일 수 있었습니다. 또한, 임의의 ErrorCode를 생성하여 다양한 예외 상황에 대해 적절한 응답을 반환할 수 있어서 클라이언트와의 소통이 원활해졌습니다.

  </div>
  </details>
- 새로고침 로직에 @Async 을 적용해서 비동기 프로그래밍 구현
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  구현한 API가 어플리케이션 특정상 여러 사용자가 동시에 누를 경우에도 대비를 해야하는데, 많은 유저들이 새로고침을 동시에 누른다면 동기 프로그래밍으로는 처리하는데 부하가 심해져 반응이 느려질꺼 같다는 고민을 했습니다. 그래서 @Async 어노테이션을 이용해서 비동기 처리를 진행했고 Apache Jmeter 성능테스트를 통해 처리량이 2배 정도 증가하는 결과를 얻었습니다. 

  </div>
  </details>
- GitHub API와 통신해서 깃허브 닉네임과 프로필 이미지 조회 구현(HttpURLConnection → Feign Client 리팩토링 )
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  GitHub API 통신을 해서 해당 유저의 닉네임을 조회하고 프로필 이미지를 가져오는 API를 처음에는 HttpURLConnection을 사용해 구현했지만, 서버 통신과 Json 파싱 등 코드가 길어져 코드의 가독성이 좋지 않았습니다. 이에 대해 선언적 방식으로 인터페이스를 정의하고, 해당 인터페이스에 대한 구현체를 자동으로 생성하여 REST API 호출을 처리하는 Feign Client를 사용하여 리팩토링을 진행하였습니다. 이를 통해 코드가 간결해지고 가독성이 높아졌으며, API 호출과 응답 처리에 대한 코드 작성 시간을 줄일 수 있어서 개발 생산성도 향상되었습니다
  </div>
  </details>
- Commit 수 갱신 API 구현 ( 커밋 갱신을 위해 Jsoup 라이브러리를 이용한 GitHub 크롤링 )
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  Gromit 이라는 어플리케이션은 유저의 커밋 수를 이용해 캐릭터를 키우기 때문에, 매일 커밋 수를 가져와야 할 필요성이 있었습니다. 처음에는 깃허브 API를 조사하여 적용하려고 시도했지만, 구현 방법을 찾지 못해 막혀있던 상황이었습니다. 하지만, 크롤링 방법을 사용하여 구현에 성공했습니다.  https://github.com/users/{GitHubName}/contributions
 html을 분석하고 Jsoup 라이브러리를 이용해 커밋 수를 가져오는 API를 구현할 수 있었습니다. 이를 통해 매일 유저의 커밋 수를 정확하게 가져와서, Gromit 어플리케이션의 캐릭터 키우기 기능을 원활하게 동작시킬 수 있게 되었습니다.
  </div>
  </details>
     
    
- CI/CD 도구인 GitHub Action 을 이용한 자동화 배포 파이프라인 구축
  <details>
  <summary>자세한 내용</summary>
  <div markdown="1">
  
  코드를 변경할 때마다 수동으로 배포하는 데 불편함을 겪었고, 다른 팀원들이 서버 배포를 하지 못해 번거로움이 생겼습니다. 이에 저는 서버 배포를 원활하게 진행할 수 있는 방법을 찾기로 결정하였습니다. CI/CD 도구인 Jenkins와 GitHub Actions를 조사한 결과, 리소스가 적게 드는 GitHub Actions이 소규모 프로젝트에 적합하다고 판단하여 구축하기로  결정했습니다. GitHub Actions를 공부하여 배포 과정과 원리를 이해하고, 자동화 배포 파이프라인을 구축하였습니다. 이를 통해 코드 변경하고 특정 브랜치에 push시 EC2에 자동으로 배포가 이루어지며, 다른 팀원들도 편리하게 서버 배포를 함께 진행할 수 있게 되었습니다. 이를 통해 배포 과정에서 발생하는 인적 오류를 줄이고, 안정적인 서비스 제공이 가능해졌습니다.
  </div>
  </details>
  
## 느낀점    
<details>
  <summary>다양한 기술 경험 및 시도</summary>
  <div markdown="1">
  
  이번 프로젝트를 통해 Rest API, Spring Security와 JWT를 이용한 인증, Feign Client를 이용한 외부 API와 통신, 비동기 프로그래밍 등 다양한 기술을 경험하고 적용해볼 수 있어서 매우 유익한 경험이었습니다. 백엔드 서버 총괄을 맡았기 때문에 책임감이 생겨, 성능 개선과 리팩토링을 통해 더욱 완성도 있는 결과물을 만들고자 노력했습니다. 계속해서 어떻게 하면 성능을 개선하고, 리팩토링 하면 좋을지 계속 생각 했습니다. 이에 따라 새로운 기술들을 공부하고 프로젝트에 적용해보면서 프로젝트의 질을 높이는 과정에서 많은 것을 배웠습니다.
  </div>
 </details>    
 
 <details>
  <summary>첫 백엔드 개발 팀장을 하며 기른 책임감과 리더십</summary>
  <div markdown="1">
  
  협업을 진행하며 처음으로 서버 개발 팀장을 맡게 되어, 전체적인 리더쉽을 발휘하며 팀원들을 이끌어야 한다는 부담감이 컸지만, 책임감을 가지고 전체적인 프로젝트 초기 환경과 구조를 세팅하였습니다. 또한 이를 극복하기 위해 먼저 Notion을 통해 전체적인 개발 일정을 계획하고, 팀원들의 의견을 적극 수렴하여 업무 분담을 명확하게 하였습니다. 정기적인 화상 회의를 통해 프로젝트 진행 상황을 점검하고, 팀원들 간의 의사소통을 원활하게 하여 서로 도움이 될 수 있는 분위기를 조성하였습니다. . 이를 통해 팀원들이 목표를 달성하기 위한 방향성을 명확히 하였고, 협업에서 발생하는 문제를 효율적으로 해결할 수 있도록 팀을 이끌어나갔습니다. 이 경험을 통해 리더십과 책임감을 기르는데 많은 도움이 되었습니다.
  </div>
 </details>    

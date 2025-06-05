package com.helper.member.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.helper.chatbot.vo.ChatBotVO;
import com.helper.page.util.PageObject;
import com.helper.policy.service.PolicyService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/member")
@Log4j2
public class MemberRestController {
	
	private final RestTemplate restTemplate;
	
	public MemberRestController(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}
	
	@Autowired
	public PolicyService service;
	
	@GetMapping("/getId.do")
	// @AuthenticationPrincipal : Spring Security의 SecurityContextHolder에 저장되어있는 현재 인증된 사용자 정보를 주입
	// 다수의 클라이언트가 로그인하더라도 userDetails 매개변수에는 항상 현재 요청을 보낸 특정 클라이언트의 인증 정보만 주입됨
	public String getId(@AuthenticationPrincipal UserDetails userDetails) {
		if (userDetails != null) {
            String userId = userDetails.getUsername(); // UserDetails의 getUsername()은 사용자 ID 반환
            // 권한 정보는 getAuthorities()로 가져올 수 있지만, 문자열로 깔끔하게 조합하려면
            // 스트림 API를 사용할 수 있습니다.
            String roles = userDetails.getAuthorities().stream()
                                  .map(GrantedAuthority::getAuthority) // GrantedAuthority는 인터페이스, 구현체는 SimpleGrantedAuthority
                                  .collect(Collectors.joining(", ")); // 쉼표로 구분된 문자열로 결합

            log.info("인증된 사용자 ID: {}, 권한: {}", userId, roles);
            return userId;
        } else {
            // @AuthenticationPrincipal이 null 이라는 것은 인증이 되지 않은 상태를 의미합니다.
            // SecurityConfig에서 해당 경로가 permitAll()로 설정되어 있다면 이곳으로 들어올 수 있습니다.
            log.info("인증되지 않은 사용자입니다.");
            return "";
        }
	}
	
	@PostMapping("/chatbot/ask.do")
	public String ask(@RequestBody ChatBotVO vo, @AuthenticationPrincipal UserDetails userDetails) {
		log.info("----- MemberController ask() -----");
		if(userDetails == null)
			return null;
		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("member_id", userDetails.getUsername());
		requestBody.put("question", vo.getQuestion());
		
		String fastApiUrl = "http://localhost:8000/search";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
		
		try {
			ResponseEntity<ChatBotVO> response = restTemplate.postForEntity(fastApiUrl, requestEntity, ChatBotVO.class);
			if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
				String answer = response.getBody().getAnswer();
	            log.info("챗봇 응답 성공: 추출된 answer: {}", answer);
	            return answer;
			}
			else {
	            log.error("챗봇 응답 실패: 상태 코드 = {}, 본문 = {}", response.getStatusCode(), response.getBody());
	            return "챗봇 응답을 받지 못했습니다.";
			}
		}catch (Exception e) {
	         log.error("RestTemplate 호출 중 예외 발생: {}", e.getMessage());
	         return "챗봇 서비스와 통신 중 오류가 발생했습니다: " + e.getMessage();
	    }
	}

   @GetMapping("/policy/list.do")
   public ResponseEntity<Map<String, Object>> policyList(PageObject pageObject) {
	   
	   log.info("policy list mapping, PageObject: {}", pageObject);

	   Map<String, Object> map = new HashMap<>();
	   map.put("list", service.policyList(pageObject));
	   map.put("pageObject", pageObject);
	   	   
	   return new ResponseEntity<>(map, HttpStatus.OK);
   }
}
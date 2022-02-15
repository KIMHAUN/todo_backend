package com.ntels.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ntels.demo.dto.ResponseDTO;
import com.ntels.demo.dto.TestRequestBodyDTO;

@RestController
@RequestMapping("test") //리소스
public class TestController {
	
	@GetMapping
	public String testController() {
		return "Hello World!";
	}
	
	@GetMapping("/{id}")
	public String testControllerWithPathVariables(@PathVariable(required=false) int id) {
		return "Hello World! ID " + id;
	}
	
	@GetMapping("/testRequestParam")
	public String testControllerRequestParam(@RequestParam(required=false) int id) {
		return "Hello World! ID " + id;
	}
	
	//이 프로젝트의 모든 컨트롤러는 ResponseDTO를 반환한다.
	@GetMapping("/testRequestBody")
	public ResponseDTO<String> testControllerRequestBody(@RequestBody TestRequestBodyDTO testRequestBodyDTO) {
		//return "Hello World! ID" + testRequestBodyDTO.getId() + " Message : " + testRequestBodyDTO.getMessage();
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseDTO");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		return response;
	}
	
	//ResponseEntity는 HTTP응답의 바디 뿐만 아니라, status나 header를 조작하고 싶을 때 사용.
	@GetMapping("/testResponseEntity")
	public ResponseEntity<?> testControllerResponseEntity() {
		List<String> list = new ArrayList<>();
		list.add("Hello World! I'm ResponseEntity. And you got 400!");
		ResponseDTO<String> response = ResponseDTO.<String>builder().data(list).build();
		
		//return ResponseEntity.ok().body(response);
		//http status를 400으로 설정
		return ResponseEntity.badRequest().body(response);
	}

}

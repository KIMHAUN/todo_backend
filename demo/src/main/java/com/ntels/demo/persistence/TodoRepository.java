package com.ntels.demo.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ntels.demo.model.TodoEntity;

//@Repository또한 @Component의 특별 케이스
@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, String>{
	/**
	 * 스프링 데이터 JPA가 메서드 이름을 파싱해서 SELECT * FROM TodoRepository WHERE userId = '{userId}'
	 * ?1은 메소드의 매개변수의 순서 위치
	 * @param userId
	 * @return
	 */
	//@Query("select * from Todo t where t.userId = ?1")
	List<TodoEntity> findByUserId(String userId);
}

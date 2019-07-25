package com.liferon.usermgt.repository;

import com.liferon.usermgt.model.User;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

@Component
public interface UserRepository extends JpaRepository<User, String> {  
  
  User findByEmail(String email);     
  User getByUsername(String username);

  @Query(value="select * from user where username like %:keyword% or first_name like %:keyword% or last_name like %:keyword% "
  		+ "or email like %:keyword% or role like %:keyword%", nativeQuery=true)
  List<User> searchUsers(@Param("keyword") String keyword);
}

package com.example.interview.mapper;
import com.example.interview.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    int insertUser(User user);
    User getUserByUsername(@Param("username") String username);
}


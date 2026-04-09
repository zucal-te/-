package com.example.interview.entity;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String userId; // 业务上的唯一标识
    private String username;
    private String password;
    private String avatar;
    private LocalDateTime createTime;
}

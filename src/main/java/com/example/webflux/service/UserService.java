package com.example.webflux.service;

import com.example.webflux.model.User;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final Map<Long, User> userMap = new ConcurrentHashMap<>();

    public UserService() {
        // 添加一些示例数据
        userMap.put(1L, new User(1L, "张三", 20, "zhangsan@example.com"));
        userMap.put(2L, new User(2L, "李四", 25, "lisi@example.com"));
        userMap.put(3L, new User(3L, "王五", 30, "wangwu@example.com"));
    }

    public Flux<User> getAllUsers() {
        return Flux.fromIterable(userMap.values());
    }

    public Mono<User> getUserById(Long id) {
        return Mono.justOrEmpty(userMap.get(id));
    }

    public Mono<User> createUser(User user) {
        userMap.put(user.getId(), user);
        return Mono.just(user);
    }

    public Mono<User> updateUser(Long id, User user) {
        user.setId(id);
        userMap.put(id, user);
        return Mono.just(user);
    }

    public Mono<Void> deleteUser(Long id) {
        userMap.remove(id);
        return Mono.empty();
    }
} 
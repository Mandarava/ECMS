package com.finance.activiti.listener;

import com.finance.activiti.service.ActivitiProcessService;

import org.activiti.engine.identity.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@Scope("singleton")
public class GroupUser {

    private static final Map<String, List<User>> ROLE_USERS_MAP = new HashMap<>();

    @Autowired
    private ActivitiProcessService activitiProcessService;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        GroupUser groupUserCache = new GroupUser();
        List<String> users = new ArrayList<>();
        int threads = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threads);
        List<Future<List<String>>> futureList = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Future<List<String>> future = executorService.submit(() -> {
                List<String> tempUserIds = new ArrayList<>();
                for (int i1 = 0; i1 < 1000; i1++) {
                    User user = groupUserCache.getUserByRoleName("立案人员");
                    if (user == null) {
                        System.out.println("null");
                    }
                    tempUserIds.add(user.getId());
                }
                return tempUserIds;
            });
            futureList.add(future);
        }
        for (Future<List<String>> future : futureList) {
            users.addAll(future.get());
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                Collections.sort(users);
                Map<String, Integer> count = new HashMap<>();
                for (String user : users) {
                    if (count.containsKey(user)) {
                        count.put(user, count.get(user) + 1);
                    } else {
                        count.put(user, 1);
                    }
                }
                for (Map.Entry<String, Integer> entry : count.entrySet()) {
                    System.out.println(entry.getKey() + "  " + entry.getValue());
                }
                System.out.println("sum " + users.size());
                break;
            }
        }
    }

    public User getUserByRoleName(String roleName) {
        if (StringUtils.isEmpty(roleName)) {
            return null;
        }
        synchronized (ROLE_USERS_MAP) {
            List<User> userList = ROLE_USERS_MAP.computeIfAbsent(roleName, e -> {
                List<User> list = activitiProcessService.findGroupUsersByGroupName(roleName);
                if (CollectionUtils.isEmpty(list)) {
                    return null;
                }
                return list;
            });
            if (CollectionUtils.isEmpty(userList)) {
                return null;
            } else {
                User user = userList.get(0);
                userList.remove(0);
                if (CollectionUtils.isEmpty(userList)) {
                    ROLE_USERS_MAP.put(roleName, null);
                }
                return user;
            }
        }
    }

    public User allocateUserRandom(String roleName) {
        User user = null;
        List<User> userList = activitiProcessService.findGroupUsersByGroupName(roleName);
        if (CollectionUtils.isNotEmpty(userList)) {
            int random = new Random().nextInt(userList.size());
            user = userList.get(random);
        }
        return user;
    }

}

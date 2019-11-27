package com.project.util;

import lombok.Data;

@Data
public class RedisKey {
    public static final String interviewKey = "interviewKey";

    public static String getInterviewKey() {
        return interviewKey;
    }
}

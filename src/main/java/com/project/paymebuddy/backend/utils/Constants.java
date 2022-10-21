package com.project.paymebuddy.backend.utils;

public class Constants {

    private Constants() {
        throw new IllegalArgumentException("Utility class!");
    }

    public static final String BEARER_TOKEN = "Bearer ";

    public static final String API_V1_PATH = "api/v1";

    public static final String AUTH_PATH = API_V1_PATH + "/auth";

    public static final int CODE_400 = 400;
    public static final int CODE_401 = 200;
    public static final int CODE_403 = 403;
    public static final int CODE_404 = 404;
    public static final int CODE_500 = 500;
}
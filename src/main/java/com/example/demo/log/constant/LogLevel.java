package com.example.demo.log.constant;

public enum LogLevel {
    Error(4), Warn(5), Info(6);

    private int level;

    LogLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}

package com.example.demo.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.demo.log.constant.HeaderKey;
import com.example.demo.log.constant.LogLevel;

import java.util.Date;
import java.util.Map;


public class Logger {
    private Map<String, String> headers;

    public Logger(Map<String, String> headers) {
        this.headers = headers;
    }

    public void error(String var1, Object... var2) {
        String message = String.format(var1, var2);
        printLog(message, LogLevel.Error);
    }

    public void warn(String var1, Object... var2) {
        String message = String.format(var1, var2);
        printLog(message, LogLevel.Warn);
    }

    public void info(String var1, Object... var2) {
        String message = String.format(var1, var2);
        printLog(message, LogLevel.Info);
    }

    private void printLog(String message, LogLevel logLevel) {
        if (headers == null) {
            return;
        }

        String executeID = headers.get(HeaderKey.executeID);
        String logID = headers.get(HeaderKey.logID);
        String functionID = headers.get(HeaderKey.functionID);
        String tenantStr = headers.get(HeaderKey.tenant);
        Tenant tenant = JSON.parseObject(tenantStr, Tenant.class);
        if (tenant == null) {
            System.out.println(message);
            return;
        }

        FormatLog formatLog = new FormatLog();
        formatLog.Level = logLevel.getLevel();
        formatLog.EventID = executeID;
        formatLog.LogID = logID;
        formatLog.TenantID = tenant.ID;
        formatLog.Namespace = tenant.Namespace;
        formatLog.Message = message;
        formatLog.Timestamp = new Date().getTime();
        formatLog.TenantType = tenant.Type;
        formatLog.FunctionAPIID = functionID;

        String jsonStr = JSONObject.toJSONString(formatLog);

        String content = String.format("%s %s %s %s", "2024-01-01", HeaderKey.apaasLogPrefix, jsonStr, HeaderKey.apaasLogSuffix);
        System.out.println(content);
    }

    private static class Tenant {
        @JSONField(name = "id")
        public long ID;
        @JSONField(name = "name")
        public String Name;
        @JSONField(name = "type")
        public long Type;
        @JSONField(name = "namespace")
        public String Namespace;
        @JSONField(name = "domain")
        public String Domain;
    }


    private class FormatLog {
        @JSONField(name = "level")
        public int Level;         // 日志级别, 4-error,5-warn,6-info
        @JSONField(name = "event_id")
        public String EventID;       // 事件 ID，可观测需要
        @JSONField(name = "function_api_id")
        public String FunctionAPIID;// 函数 API ID
        @JSONField(name = "log_id")
        public String LogID;// 日志 ID，事件编号与日志编号有一一对应关系
        @JSONField(name = "timestamp")
        public long Timestamp;// 时间
        @JSONField(name = "message")
        public String Message;// 用户的日志内容，SDK 会对超长日志截断
        @JSONField(name = "tenant_id")
        public long TenantID;// 租户 ID
        @JSONField(name = "tenant_type")
        public long TenantType;// 租户 ID
        @JSONField(name = "namespace")
        public String Namespace;// 命名空间
    }
}



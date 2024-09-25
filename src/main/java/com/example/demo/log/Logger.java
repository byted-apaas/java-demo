package com.example.demo.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Map;


public class Logger {
    private String apaasLogPrefix = "apaas-log-prefix";
    private String apaasLogSuffix = "apaas-log-suffix";
    private Map<String, String> headers;

    public Logger(Map<String, String> headers) {
        this.headers = headers;
    }

    private org.slf4j.Logger logger = LoggerFactory.getLogger(org.slf4j.Logger.class);

    public void info(String var1) {
        logger.info(var1);
    }

    public void info(String var1, Object... var2) {
        if (headers == null) {
            return;
        }

        String executeID = headers.get("x-serverless-execute-id");
        String logID = headers.get("x-kunlun-logid");
        String functionID = headers.get("x-serverless-function-api-id");

        String tenantStr = headers.get("x-kunlun-tenant");
        logger.info("tenantStr: {}", tenantStr);
        Tenant tenant = JSON.parseObject(tenantStr, Tenant.class);
        if (tenant == null) {
            logger.info("null tenant, str: {}", tenantStr);
            return;
        }


        FormatLog formatLog = new FormatLog();
        formatLog.Level = 6; // info
        formatLog.EventID = executeID;
        formatLog.LogID = logID;
        formatLog.TenantID = tenant.ID;
        formatLog.Namespace = tenant.Namespace;
        formatLog.Message = String.format(var1, var2);
        formatLog.Timestamp = new Date().getTime();
        formatLog.TenantType = tenant.Type;
        formatLog.FunctionAPIID = functionID;

        String jsonStr = JSONObject.toJSONString(formatLog);

        String content = String.format("%s %s %s %s", "2024-01-01", apaasLogPrefix, jsonStr, apaasLogSuffix);
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



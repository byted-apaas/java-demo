package com.example.demo.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.example.demo.log.constant.Common;
import com.example.demo.log.constant.HeaderKey;
import com.example.demo.log.constant.LogLevel;
import org.slf4j.helpers.MessageFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

// 注意：修改这个文件的代码可能会导致应用在 aPaaS 平台上查看不到日志。
public class Logger {
    private Map<String, String> headers;
    private AtomicInteger logCount = new AtomicInteger(0);

    public Logger(Map<String, String> headers) {
        this.headers = headers;
    }

    public void error(String format, Object... args) {
        String message = getFormattedMessage(format, args);
        printLog(message, LogLevel.Error);
    }

    public void warn(String format, Object... args) {
        String message = getFormattedMessage(format, args);
        printLog(message, LogLevel.Warn);
    }

    public void info(String format, Object... args) {
        String message = getFormattedMessage(format, args);
        printLog(message, LogLevel.Info);
    }

    private String getFormattedMessage(String format, Object... args) {
        String message = format;
        if (args != null) {
            message = MessageFormatter.arrayFormat(format, args).getMessage();
        }
        return message;
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

        // 丢失超出条数的日志
        int nextCount = logCount.incrementAndGet();
        if (nextCount > Common.LogCountLimit) {
            return;
        }
        // 单条日志长度限制，超过则截断并在该日志后增加 Tip
        if (message.length() >= Common.LogLengthLimit) {
            message = message.substring(0, Common.LogLengthLimit) + Common.LogLengthLimitTip;
        }
        // 整体日志条数限制，超过则截断并在最后一条日志增加 Tip
        if (nextCount == Common.LogCountLimit) {
            message = message + Common.LogCountLimitTip;
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

        String content = String.format("%s %s %s %s", getFormatTime(), HeaderKey.apaasLogPrefix, jsonStr, HeaderKey.apaasLogSuffix);
        System.out.println(content);
    }

    private String getFormatTime() {
        // FaaS 平台通过时间分割日志
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        return sdf.format(now);
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



package cn.javayong.magic.module.ai.util;

import org.springframework.http.MediaType;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class ResponseUtils {

    public static void writeSSE(HttpServletResponse response, Flux<String> dataStream) throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        PrintWriter writer = response.getWriter();
        // 关键点：切换到当前线程执行
        dataStream
                .publishOn(Schedulers.immediate()) // 确保操作在当前线程
                .doOnNext(data -> {
                    writer.write("data: " + data + "\n\n"); // SSE格式
                    writer.flush();
                })
                .doOnError(e -> {
                    writer.write("event: error\ndata: " + e.getMessage() + "\n\n");
                    writer.flush();
                })
                .doOnComplete(() -> {
                    writer.write("event: done\ndata: [DONE]\n\n");
                    writer.flush();
                    writer.close();
                })
                .blockLast(); // 阻塞直到流结束
    }

    public static void writeJSON(HttpServletResponse response,String jsonStr) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(jsonStr);
        writer.flush();
        writer.close();
    }

}

package com.video.excption;

import com.video.utils.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

/**
 * 全局异常异常处理
 */
@RestControllerAdvice
public class HandleException {

    private Logger logger = LoggerFactory.getLogger(HandleException.class);

    /**
     * 捕获全局 exception 异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Response<String> handleException(Exception e){
        logger.error("【系统繁忙，服务器错误】，{}",e);
        return Response.error("系统繁忙");
    }

    /**
     * 捕获上传文件 MultipartException 异常
     * @return
     */
    @ExceptionHandler(MultipartException.class)
    public Response handleMultipartException(MultipartException e){
        logger.info("【上传失败！】，{}",e);
        return Response.error("上传失败！"+e.getMessage());
    }
}

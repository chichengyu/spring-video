package com.video.config;

import com.video.interceptor.MiniInterceptor;
import com.video.utils.IdWorker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 创建 bean 加入 spring 容器
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Bean
    public IdWorker idWorker(){
        return new IdWorker(1,1);
    }

    /**
     * api 拦截器
     * @return
     */
    @Bean
    public MiniInterceptor miniInterceptor(){
        return new MiniInterceptor();
    }

    /**
     * 前端 user 模块 api 拦截器
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(miniInterceptor())
                .excludePathPatterns("/user/queryPublisher")
                .addPathPatterns("/user/**")
                .addPathPatterns("/bgm/**")
                .addPathPatterns("/video/upload","/video/uploadCover","/video/userLike","/video/userUnLike","/video/saveComment");
        //super.addInterceptors(registry);
    }

    /**
     * 配置静态资源
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/META-INF/resources/") // 映射 swagger 发布目录
                .addResourceLocations("file:G:/video-upload/");// 映射上传图片后的目录
    }
}

package com.example.paysonix.network;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.ParameterizedType;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.example.paysonix.service.SomeService.KEY_VALUE_PAIR_SEPARATOR;
import static com.example.paysonix.service.SomeService.KEY_VALUE_SEPARATOR;

@RequiredArgsConstructor
@Component
public class StringMapRequestBodyHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver, InitializingBean {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Map.class) &&
                parameter.hasParameterAnnotation(RequestBody.class) &&
                isGenericParameterTypes(parameter);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Map<String, Map<String, Integer>> queryMap = new HashMap<>();
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request != null) {
            for (String query: request.getQueryString().split(KEY_VALUE_PAIR_SEPARATOR)) {
                String[] splittedQuery = query.split(KEY_VALUE_SEPARATOR);
                String value = splittedQuery.length == 2 ? splittedQuery[1] : "";
                queryMap.putIfAbsent(splittedQuery[0], new HashMap<>());
                queryMap.get(splittedQuery[0]).merge(value, 1, Integer::sum);
            }
        }

        Map<String, String> result = new HashMap<>();

        for (Map.Entry<String, String[]> entry: webRequest.getParameterMap().entrySet()) {
            String key = entry.getKey();
            String[] values = entry.getValue();
            if (queryMap.containsKey(key)) {
                for (String value: values) {
                    Map<String, Integer> valuesMap = queryMap.get(key);
                    if (valuesMap.containsKey(value)) {
                        if (valuesMap.compute(value, (k, oldValue) -> oldValue - 1) == 0) {
                            valuesMap.remove(value);
                        }
                    } else {
                        result.put(key, value);
                    }
                }
            } else {
                result.put(key, values[values.length - 1]);
            }
        }

        return result;
    }

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodArgumentResolver> argumentResolvers = requestMappingHandlerAdapter.getArgumentResolvers();
        List<HandlerMethodArgumentResolver> newArgumentResolvers = new LinkedList<>();
        newArgumentResolvers.add(this);
        newArgumentResolvers.addAll(argumentResolvers == null ? List.of() : argumentResolvers);
        requestMappingHandlerAdapter.setArgumentResolvers(Collections.unmodifiableList(newArgumentResolvers));
    }

    private boolean isGenericParameterTypes(MethodParameter parameter) {
        Class<?>[] classes = getGenericParameterTypes(parameter);
        return classes.length == 2 && classes[0].equals(String.class) && classes[1].equals(String.class);
    }

    private Class<?>[] getGenericParameterTypes(MethodParameter parameter) {
        if (parameter.getGenericParameterType() instanceof ParameterizedType parameterizedType) {
            int length = parameterizedType.getActualTypeArguments().length;
            Class<?>[] genericTypes = new Class[length];

            for (int i = 0; i < length; i++) {
                if (parameterizedType.getActualTypeArguments()[i] instanceof Class<?>) {
                    genericTypes[i] = (Class<?>) parameterizedType.getActualTypeArguments()[i];
                }
            }

            return genericTypes;
        }

        return new Class<?>[] {};
    }
}
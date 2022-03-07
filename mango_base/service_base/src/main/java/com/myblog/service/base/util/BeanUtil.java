package com.myblog.service.base.util;


import com.esotericsoftware.reflectasm.MethodAccess;
import com.myblog.service.base.common.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * RefectASM实现的属性复制工具类
 *
 * @author 李斯特
 * @date 2021/09/27
 */
public class BeanUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    // key:Class对象 value:MethodAccess对象
    private static Map<Class, MethodAccess> classMethodAccessMap = new ConcurrentHashMap<>();

    // key:Class对象名-get或者set方法名 value:方法下标
    private static Map<String, Integer> methodIndexMap = new ConcurrentHashMap<>();

    // key:Class对象 value:属性的get和set集合
    private static Map<Class, List<String>> fieldMap = new ConcurrentHashMap<>();

    /**
     * 复制属性
     * @param source 属性来源对象
     * @param target 要复制到的目标对象
     */
    public static void copyProperties(Object source, Object target) {
        if (Objects.equals(source, null) || Objects.equals(target, null)) {
            LOGGER.error("copyProperties failed, source:{}, target:{}", source, target);
            return;
        }
        MethodAccess sourceMethodAccess = getFromCache(source);
        MethodAccess targetMethodAccess = getFromCache(target);
        List<String> fieldList = fieldMap.get(source.getClass());
        for (String fieldName : fieldList) {
            String getKey = String.join(Constants.Symbol.COMMA2, source.getClass().getName(), "get" + fieldName);
            String setKey = String.join(Constants.Symbol.COMMA2, target.getClass().getName(), "set" + fieldName);

            Integer setIndex = methodIndexMap.get(setKey);
            if (setIndex != null) {
                int getIndex = methodIndexMap.get(getKey);
                targetMethodAccess.invoke(target, setIndex,
                        sourceMethodAccess.invoke(source, getIndex));
            }
        }
    }

    private static MethodAccess getFromCache(Object object) {
        MethodAccess methodAccess = classMethodAccessMap.get(object.getClass());
        if (methodAccess == null) {
            synchronized (object.getClass()) {
                methodAccess = classMethodAccessMap.get(object.getClass());
                if (methodAccess != null) {
                    return methodAccess;
                }
                Class<?> objClazz = object.getClass();
                methodAccess = MethodAccess.get(objClazz);
                if (methodAccess == null) {
                    throw new RuntimeException("getFromCache failed, MethodAccess is null");
                }
                // TODO 父类或者类型不同的无法复制,暂时手动复制
                Field[] fields = objClazz.getDeclaredFields();
                List<String> fieldNameList = new ArrayList<>();
                for (Field field : fields) {
                    // 非公共的私有变量
                    if (Modifier.isPrivate(field.getModifiers()) && !Modifier.isStatic(field.getModifiers())) {
                        // 获取属性名称
                        String fieldName = StringUtils.capitalize(field.getName());
                        String getFieldName = "get" + fieldName;
                        String setFieldName = "set" + fieldName;
                        int getIndex = methodAccess.getIndex("get" + fieldName);
                        int setIndex = methodAccess.getIndex("set" + fieldName);
                        methodIndexMap.putIfAbsent(String.join(Constants.Symbol.COMMA2, objClazz.getName(), getFieldName), getIndex);
                        methodIndexMap.putIfAbsent(String.join(Constants.Symbol.COMMA2, objClazz.getName(), setFieldName), setIndex);
                        fieldNameList.add(fieldName);
                    }
                }
                classMethodAccessMap.put(objClazz, methodAccess);
                fieldMap.put(objClazz, fieldNameList);
            }
        }
        return methodAccess;
    }
}

package com.lcs.library;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EventBus {
    private static volatile EventBus ourInstance;

    private Map<Object, List<SubscribleMethod>> cacheMap;

    public static EventBus getDefault() {
        if (ourInstance == null) {
            synchronized (EventBus.class) {
                if (ourInstance == null) {
                    ourInstance = new EventBus();
                }
            }
        }
        return ourInstance;
    }

    private EventBus() {
        cacheMap = new HashMap<>();
    }

    public void register(Object object) {
        // 寻找到object类带有subscrible注解方法
        List<SubscribleMethod> list = cacheMap.get(object);
        if (list == null) {
            list = findSubscribleMethods(object);
            cacheMap.put(object, list);
        }
    }

    private List<SubscribleMethod> findSubscribleMethods(Object object) {
        List<SubscribleMethod> list = new ArrayList<>();
        Class<?> clazz = object.getClass();
        while (clazz != null) {

            String name = clazz.getName();
            if (name.startsWith("java.")
                    || name.startsWith("javax.")
                    || name.startsWith("android.")) {
                break;
            }
            Method[] methods = clazz.getDeclaredMethods();
            for (Method method : methods) {
                Subscrible subscrible = method.getAnnotation(Subscrible.class);
                if (subscrible == null) {
                    continue;
                }
                Class<?>[] types = method.getParameterTypes();
                if (types.length != 1) {
                    Log.e("错误", "eventbus only one params");
                }
                ThreadMode threadMode = subscrible.threadMode();
                SubscribleMethod subscribleMethod =
                        new SubscribleMethod(method, threadMode, types[0]);
                list.add(subscribleMethod);
            }
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public void post(Object object) {
        Set<Object> set = cacheMap.keySet();
        Iterator<Object> iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            List<SubscribleMethod> list = cacheMap.get(obj);
            for (SubscribleMethod subscribleMethod : list) {
                if (subscribleMethod.getType().isAssignableFrom(object.getClass())) {
                    subscribleMethod.getThreadMode();

                    invoke(subscribleMethod, obj, object);
                }
            }
        }
    }

    private void invoke(SubscribleMethod subscribleMethod, Object obj, Object object) {
        Method method = subscribleMethod.getMethod();
        try {
            method.invoke(obj, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void unregister(Object object) {
        cacheMap.remove(object);
    }
}

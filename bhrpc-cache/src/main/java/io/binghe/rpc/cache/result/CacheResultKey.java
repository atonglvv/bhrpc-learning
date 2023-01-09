/**
 * Copyright 2022-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.binghe.rpc.cache.result;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 缓存结果数据的Key
 */
public class CacheResultKey implements Serializable {
    private static final long serialVersionUID = -6202259281732648573L;

    /**
     * 保存缓存时的时间戳
     */
    private long cacheTimeStamp;

    /**
     * 类名称
     */
    private String className;
    /**
     * 方法名称
     */
    private String methodName;
    /**
     * 参数类型数组
     */
    private Class<?>[] parameterTypes;
    /**
     * 参数数组
     */
    private Object[] parameters;
    /**
     * 版本号
     */
    private String version;
    /**
     * 服务分组
     */
    private String group;

    public CacheResultKey(String className, String methodName, Class<?>[] parameterTypes, Object[] parameters, String version, String group) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
        this.version = version;
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheResultKey cacheKey = (CacheResultKey) o;
        return  Objects.equals(className, cacheKey.className)
                && Objects.equals(methodName, cacheKey.methodName)
                && Arrays.equals(parameterTypes, cacheKey.parameterTypes)
                && Arrays.equals(parameters, cacheKey.parameters)
                && Objects.equals(version, cacheKey.version)
                && Objects.equals(group, cacheKey.group);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(className, methodName, version, group);
        result = 31 * result + Arrays.hashCode(parameterTypes);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    public long getCacheTimeStamp() {
        return cacheTimeStamp;
    }

    public void setCacheTimeStamp(long cacheTimeStamp) {
        this.cacheTimeStamp = cacheTimeStamp;
    }

}

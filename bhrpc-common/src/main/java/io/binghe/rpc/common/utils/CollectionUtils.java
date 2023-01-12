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
package io.binghe.rpc.common.utils;

import java.util.Collection;

/**
 * @author binghe(公众号 : 冰河技术)
 * @version 1.0.0
 * @description 集合工具类
 */
public class CollectionUtils {

    /**
     * 判断两个集合是否相等，如果T是对象类型，则需要重写对象的hashcode()和equals()方法
     */
    public static <T> boolean equals(Collection<T> c1, Collection<T> c2){
        if (isEmpty(c1) || isEmpty(c2)) return false;
        return c1.size() == c2.size() && c1.containsAll(c2) && c2.containsAll(c1);
    }

    public static <T> boolean isEmpty(Collection<T> c){
        return c == null || c.isEmpty();
    }
}

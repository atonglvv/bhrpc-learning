/**
 * Copyright 2020-9999 the original author or authors.
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
package io.binghe.rpc.test.scanner;

import io.binghe.rpc.common.scanner.reference.RpcReferenceScanner;
import io.binghe.rpc.common.scanner.ClassScanner;
import io.binghe.rpc.provider.common.scanner.RpcServiceScanner;
import org.junit.Test;

import java.util.List;

/**
 * @author binghe
 * @version 1.0.0
 * @description 扫描测试
 */
public class ScannerTest {

    /**
     * 扫描io.binghe.rpc.test.scanner包下所有的类
     */
    @Test
    public void testScannerClassNameList() throws Exception {
        List<String> classNameList = ClassScanner.getClassNameList("io.binghe.rpc.test.scanner", true);
        classNameList.forEach(System.out::println);
    }

    /**
     * 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcService注解的类
     */
    @Test
    public void testScannerClassNameListByRpcService() throws Exception {
       // RpcServiceScanner.doScannerWithRpcServiceAnnotationFilterAndRegistryService("io.binghe.rpc.test.scanner");
    }

    /**
     * 扫描io.binghe.rpc.test.scanner包下所有标注了@RpcReference注解的类
     */
    @Test
    public void testScannerClassNameListByRpcReference() throws Exception {
        RpcReferenceScanner.doScannerWithRpcReferenceAnnotationFilter("io.binghe.rpc.test.scanner");
    }
}

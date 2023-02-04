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
package io.binghe.rpc.common.scanner;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author binghe
 * @version 1.0.0
 * @description 类扫描器
 */
public class ClassScanner {

    /**
     * 文件
     */
    private static final String PROTOCOL_FILE = "file";
    /**
     * jar包
     */
    private static final String PROTOCOL_JAR = "jar";
    /**
     * class文件的后缀
     */
    private static final String CLASS_FILE_SUFFIX = ".class";


    /**
     * 扫描指定包下的所有类信息
     * @param packageName 指定的包名
     * @param recursive 是否递归扫描，true:是  false:否
     * @return 指定包下所有的完整类名的List集合
     * @throws Exception
     */
    public static List<String> getClassNameList(String packageName, boolean recursive) throws Exception{
        //第一个class类的集合
        List<String> classNameList = new ArrayList<String>();
        //获取包的名字 并进行替换
        String packageDirName = packageName.replace('.', '/');
        //定义一个枚举的集合 并进行循环来处理这个目录下的things
        Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        //循环迭代下去
        while (dirs.hasMoreElements()){
            //获取下一个元素
            URL url = dirs.nextElement();
            //得到协议的名称
            String protocol = url.getProtocol();
            //如果是以文件的形式保存在服务器上
            if (PROTOCOL_FILE.equals(protocol)) {
                //获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                //以文件的方式扫描整个包下的文件 并添加到集合中
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classNameList);
            } else if (PROTOCOL_JAR.equals(protocol)){
                packageName = findAndAddClassesInPackageByJar(packageName, classNameList, recursive, packageDirName, url);
            }
        }
        return classNameList;
    }


    /**
     * 扫描Jar文件中指定包下的所有类信息
     * @param packageName 扫描的包名
     * @param classNameList 完成类名存放的List集合
     * @param recursive 是否递归调用
     * @param packageDirName 当前包名的前面部分的名称
     * @param url 包的url地址
     * @return 处理后的包名，以供下次调用使用
     * @throws IOException
     */
    private static String findAndAddClassesInPackageByJar(String packageName, List<String> classNameList, boolean recursive, String packageDirName, URL url) throws IOException {
        //如果是jar包文件
        //定义一个JarFile
        JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
        //从此jar包 得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();
        //同样的进行循环迭代
        while (entries.hasMoreElements()) {
            //获取jar里的一个实体 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();
            String name = entry.getName();
            //如果是以/开头的
            if (name.charAt(0) == '/') {
                //获取后面的字符串
                name = name.substring(1);
            }
            //如果前半部分和定义的包名相同
            if (name.startsWith(packageDirName)) {
                int idx = name.lastIndexOf('/');
                String currentPackageDir = "";
                //如果以"/"结尾 是一个包
                if (idx != -1) {
                    //获取包名 把"/"替换成"."
                    currentPackageDir = name.substring(0, idx);
                    packageName = currentPackageDir.replace('/', '.');
                }
                //如果可以迭代下去 并且是一个包
                if ((idx != -1 && currentPackageDir.equals(packageDirName)) || recursive){
                    //如果是一个.class文件 而且不是目录
                    if (name.endsWith(CLASS_FILE_SUFFIX) && !entry.isDirectory()) {
                        //去掉后面的".class" 获取真正的类名
                        String className = name.substring(packageName.length() + 1, name.length() - CLASS_FILE_SUFFIX.length());
                        classNameList.add(packageName + '.' + className);
                    }
                }
            }
        }
        return packageName;
    }


    /**
     * 扫描当前工程中指定包下的所有类信息
     * @param packageName 扫描的包名
     * @param packagePath 包在磁盘上的完整路径
     * @param recursive 是否递归调用
     * @param classNameList 类名称的集合
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath, final boolean recursive, List<String> classNameList){
        //获取此包的目录 建立一个File
        File dir = new File(packagePath);
        //如果不存在或者 也不是目录就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }
        //如果存在 就获取包下的所有文件 包括目录
        File[] dirfiles = dir.listFiles(new FileFilter() {
            //自定义过滤规则 如果可以循环(包含子目录) 或则是以.class结尾的文件(编译好的java类文件)
            public boolean accept(File file) {
                return (recursive && file.isDirectory()) || (file.getName().endsWith(CLASS_FILE_SUFFIX));
            }
        });
        //循环所有文件
        for (File file : dirfiles) {
            //如果是目录 则继续扫描
            if (file.isDirectory()) {
                findAndAddClassesInPackageByFile(packageName + "." + file.getName(),
                        file.getAbsolutePath(),
                        recursive,
                        classNameList);
            }else {
                //如果是java类文件 去掉后面的.class 只留下类名
                String className = file.getName().substring(0, file.getName().length() - CLASS_FILE_SUFFIX.length());
                //添加到集合中去
                classNameList.add(packageName + '.' + className);
            }
        }
    }
}

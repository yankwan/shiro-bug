## shiro 1.2.4 反序列化漏洞

> [漏洞说明](https://issues.apache.org/jira/browse/SHIRO-550)

#### 1. 环境搭建

> 环境搭建参考: [Shiro RememberMe 1.2.4 反序列化导致的命令执行漏洞](https://www.seebug.org/vuldb/ssvid-92180)

* shiro 1.2.4 版本下载: https://github.com/apache/shiro/releases/tag/shiro-root-1.2.4

* commons-collections4-4.0.jar 额外增加该依赖

shiro 示例工程位于samples目录下的web目录, 需要对其pom文件进行如下修改

```xml
<!-- 修改为1.2版本 -->
<dependency>
    <groupId>javax.servlet</groupId>
    <artifactId>jstl</artifactId>
    <version>1.2</version>
    <scope>runtime</scope>
</dependency>

<!-- 增加该依赖 -->
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-collections4</artifactId>
    <version>4.0</version>
</dependency>

```

#### 2. 调试入口

部署运行成功后，在登录界面勾选Remember Me选项进行登录，在`AbstractRememberMeManager#onSuccessfulLogin(...)`方法打断点开始调试。

可以跟踪到如下代码:
```java
protected byte[] convertPrincipalsToBytes(PrincipalCollection principals) {
    // 序列化
    byte[] bytes = serialize(principals);
    // AES加密
    if (getCipherService() != null) {
        bytes = encrypt(bytes);
    }
    return bytes;
}
```

其中encrypt加密方法中，`getEncryptionCipherKey()`用到了`AbstractRememberMeManager`中硬编码的key: `Base64.decode("kPH+bIxk5D2deZiIxcaaaA==")`
```java
protected byte[] encrypt(byte[] serialized) {
    byte[] value = serialized;
    CipherService cipherService = getCipherService();
    if (cipherService != null) {
        ByteSource byteSource = cipherService.encrypt(serialized, getEncryptionCipherKey());
        value = byteSource.getBytes();
    }
    return value;
}
```

基于AES对称加密的特性，知道了AES的key就可以解密对应的密文了。


解密对应入口方法为: `AbstractRememberMeManager#getRememberedPrincipals(...)`
```java
public PrincipalCollection getRememberedPrincipals(SubjectContext subjectContext) {
    PrincipalCollection principals = null;
    try {
        // 进行Base64解码
        byte[] bytes = getRememberedSerializedIdentity(subjectContext);
        //SHIRO-138 - only call convertBytesToPrincipals if bytes exist:
        // 进行AES解密及反序列化
        if (bytes != null && bytes.length > 0) {
            principals = convertBytesToPrincipals(bytes, subjectContext);
        }
    } catch (RuntimeException re) {
        principals = onRememberedPrincipalFailure(re, subjectContext);
    }

    return principals;
}
```

#### 3. 利用ysoserial生成反序列化payload
# base-parent
基础的springboot项目封装，base springboot project

#### 启动参数

```text

-Dlog.file=D:\log\springboot\xx
-Ddomain=sms
-Dserver.port=8082
-Dspring.application.name=sms
-Dspring.profiles.active=dev

```

### 配置maven私服

```text
  <!-- 配置云效账号密码 -->
  <servers>
	  <server>
		<id>rdc-releases</id>
		<username>用户名</username>
		<password>密码</password>
	  </server>
	  <server>
		<id>rdc-snapshots</id>
		<username>用户名</username>
		<password>密码</password>
	  </server>
  </servers>
  
  
  <!-- 拉取jar配置 -->
  <repositories>
  <repository>
    <id>rdc-releases</id>
    <url>https://packages.aliyun.com/maven/repository/2422895-release-1qX72S/</url>
    <releases>
      <enabled>true</enabled>
    </releases>
    <snapshots>
      <enabled>false</enabled>
    </snapshots>
  </repository>
  <repository>
    <id>rdc-snapshots</id>
    <url>https://packages.aliyun.com/maven/repository/2422895-snapshot-SwD328/</url>
    <releases>
      <enabled>false</enabled>
      </releases>
    <snapshots>
      <enabled>true</enabled>
    </snapshots>
  </repository>
</repositories>
```


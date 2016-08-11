# AndroidFrameProject
安卓框架程序，用于快速开发

上传library到jCenter
步骤：1.执行gradle clean
     2.执行gradle install
     3.执行gradle bintrayUpload

#mvp项目
     是仿造google的mvp示例编写的

#删除重复依赖
      compile('com.alibaba:fastjson:1.2.15') {
          exclude module: 'fastjson', group: 'com.alibaba'
      }
      compile('org.springframework.boot:spring-boot-starter:1.4.0.RELEASE') {
          exclude module: 'org.springframework.boot:spring-boot-starter-logging'
      }

#


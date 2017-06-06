# MessageService
基于Android的轻量级消息发送管理和优化服务<br>
## 项目简介
本项目以Service的形式搭建起Android客户端与服务器之间的通信的消息中间件，在保留Android原有的网络通信框架Volley轻便高效的优点的基础上，加入消息持久化的功能，保证客户端与服务器之间的通信的完整性与正确性，与此同时，通过消息队列技术（Message Queue）实现对消息发送的管理和打包发送，有利于提高消息发送效率，降低消息发送的开销，最终达到降低能耗，延长Android终端待机的目的。<br>
## 下载安装
本项目基于Android 7.0及以上系统

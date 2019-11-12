# README
这是一个基于融云的即时通讯系统,实现了单聊和群聊。  
## 类介绍
### activity包
ConversationActivity:会话列表,显示会话。  
LoginActivity:登录界面。  
MainActivity:主界面,包括会话列表Fragment,FriendFragment和GroupFragment,侧滑菜单展示了登录用户信息以及登出操作。  
RegisterActivity:注册界面。  
SplashActivity:启动界面。根据上次是否登录来判断启动到LoginActivity还是MainActivity。  
### adapter包
ContactsAdapter:RecyclerView适配器,显示好友或者群组的单项并实现点击事件。  
### bean包
javabean类。  
### fragment包
分别表示好友和群组的界面。  
### info包
ConstValues:存储一些KEY或者Handler处理相关的内容(mysql数据库的IP也在这里)。  
Contacts:好友列表和群组列表,通过单例保存。  
Friend和Group:好友和群组,用于添加好友或者添加群组时使用。  
User:单例,登录者的用户信息。  
### util包
ConnectRongIM:连接融云的操作。  
ContactsOperation:发起好友请求,建群,加群操作。  
GetResult:Retrofit要用的接口。  
HttpUtil:获取融云Server所使用的以及创建Retrofit网络接口实例。  
MySqlDBHelper:MySql操作。  
PortraitUri:用户头像的随机选择以及获取群组头像的Uri。  
SystemMessageOperation:添加好友操作时,(比如id为a(名称为A)的用户需要加id为b(名称为B)的用户时,这时候需要系统通过发送请求给id为b,让id为b进行选择是否添加好友,然后再发送给a)。  

最后一个App是Application,初始化RongIM。



# RecycleViewFloorCountDown


本文介绍在RecyclerView中使用倒计时楼层，并且每秒刷新显示倒计时。
没有纠结于样式，主要介绍代码结构和设计模式。

先看一下效果：

 ![在这里插入图片描述](https://img-blog.csdnimg.cn/20190314162447643.gif)

我们采取的是观察者模式的方法，启动一个handler，每隔一秒去刷新所有注册过的item楼层。观察者模式的大概关系如下图：
![在这里插入图片描述](https://img-blog.csdnimg.cn/20190314165911923.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L0FuZHJvaWRNc2t5,size_16,color_FFFFFF,t_70)
我们并没有使用JAVA中的Observable，因为在释放Holder的时机比较难处理存在内存泄露的风险，所以我们采用WeakHashMap去保存所有Holder，但是在GC不频繁的情况下，弱引用也可以访问到无效的Holder对象，如果RecyclerView频繁更新，GC又不频繁进行，将会有通知到很多无效的Holder。
为此我们采用了均衡的两套方案，
1.在RecyclerView更新不频繁的场景下我们只在Adapter的OnCreate中注册Holder，并且除非页面销毁否则不去管理WeakHashMap中的内容，一切交给GC处理。
2在RecyclerView更新频繁的时候，我们会在onbind方法里判断是否注册了Holder，没有则加入，并且在notifyDataChange的时候解除所有Holder的注册，这样就算Holder没有被GC回收，也收不到我们的更新指令了。
选用哪种方案将在Observable初始化的时候得到确认





欢迎大家提出各种问题，让控件越来越好用谢谢。
	2019.3.14 Androidmsky
## License

    Copyright 2019 AndroidMsky

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


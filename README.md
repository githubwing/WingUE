# WowSplash
A Woooow Splash.. 一个让你的哇哇哇哇闪屏页。

![image](https://github.com/githubwing/WowSplash/raw/master/img/img.gif)

感谢[GAStudio](https://github.com/Ajian-studio)的SVG - Path工具类~
这个Splash分为两部分。
## Part I

第一部分是使用SVG动画实现的，你可以使用**Vector Magic**将一个普通图片转换为SVG图片,不过由于SVG大小的问题**没有进行适配**，思路最重要嘛哈哈哈。

然后配合PathMeasure来实现。

## Part II
采用Xfermode，将两个Bitmap混合。

![image](https://github.com/githubwing/WowSplash/raw/master/img/xfermode.png)

![image](https://github.com/githubwing/WowSplash/raw/master/img/zzz.png)

由于有一些坑，所以需要进行一些特殊处理~~ 详见文章[这交互炸了 ， 教你学会 App 闪屏页像云一样扩散到主页面](https://www.diycode.cc/topics/512)

如果你觉得还不错，欢迎Star. 欢迎加入我的Android酒馆来喝酒:425983695



# License

    Copyright 2016 androidwing1992

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
        http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

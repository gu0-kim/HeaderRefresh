# HeaderRefresh

## HeaderRefresh的demo

1. demo1 实现了某宝的首页功能

模仿某宝的demo**并没有**使用CoordinatorLayout+CollapsingToolbarLayout的结构，原因有二：
- CollapsingToolbarLayout折叠时动画有卡顿问题，体验并不好。
- 如果使用CollapsingToolbarLayout，当down事件发生在CollapsingToolbarLayout上，最大只能下拉到CollapsingToolbarLayout完全展开，不能连续进行下拉刷新。使用本demo的结构，从展开到拖拽刷新可以连续执行，用户体验很好。背景的异步滚动效果是通过监听HeaderRefreshRecyclerView的滚动位移处理的。感兴趣的可以看一下代码。

参见项目中的[ZhiFuBaoIndexView](https://github.com/gu0-kim/HeaderRefresh/blob/master/app/src/main/java/com/example/developergu/refreshmaster/mvp/view/indexpage/noappbar/ZhiFuBaoIndexView.java)

demo1截图

![image](https://github.com/gu0-kim/HeaderRefresh/blob/master/art/refresh.gif)        ![image](https://github.com/gu0-kim/HeaderRefresh/blob/master/art/4.gif)

2. demo2 实现了HeaderRefresh在使用appbar的情景

参见项目中的[AppBarDemoView](https://github.com/gu0-kim/HeaderRefresh/blob/master/app/src/main/java/com/example/developergu/refreshmaster/mvp/view/indexpage/appbar/AppBarDemoView.java)

![image](https://github.com/gu0-kim/HeaderRefresh/blob/master/art/appbar_refresh.gif)

## HeaderRefresh组件


- 用recyclerView实现的带customHeader的刷新组件。刷新的部分出现在customHeader下面。

- 支持nestscrolling机制。如果有appbar，则当appbar完全展开后，继续拖拽，才会触发HeaderRefresh的下拉刷新。

- 自带一种默认的刷新头部[DefaultRefreshLayout](https://github.com/gu0-kim/HeaderRefresh/blob/master/headerrefresh/src/main/java/com/developergu/headerrefresh/header/defaultype/DefaultRefreshLayout.java)。也可根据自己需求仿照DefaultRefreshLayout进行扩展。

## 组件结构

![image](https://github.com/gu0-kim/HeaderRefresh/blob/master/art/HeaderRefreshRecyclerView.png)

## 使用方法

### 完整使用

```
@BindView(R.id.header_rv)
HeaderRefreshRecyclerView mRecyclerView;


//in onCreateView

//由布局文件引入刷新头部
//RefreshLayout是HeaderRefreshRecyclerView处理刷新的部分
RefreshLayout refreshLayout =(RefreshLayout)getLayoutInflater().inflate(R.layout.default_refresh_layout, (ViewGroup) parent, false);
refreshLayout.setOffsetListener(this);
//加入自定义头部
View customHeader =LayoutInflater.from(getContext()).inflate(R.layout.simple_header_layout, mToolbar, false);
//设置自定义头部和刷新头部
mRecyclerView.setCustomHeaderView(customHeader);
mRecyclerView.setRefreshLayoutHeaderView(refreshLayout);
//设置回调
mRecyclerView.setRefreshListener(this);
//绑定appbar
mRecyclerView.setAppBarLayout(appBar);
```


### 刷新头部的引入

- 由布局文件配置刷新头部


```
<com.developergu.headerrefresh.header.defaultype.DefaultRefreshLayout 
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    app:header_refresh_rate="0.5"
    app:pull_distance_rate="0.5" />
```
- 或者由代码配置刷新头部


```
RefreshLayout refreshLayout =
        new DefaultRefreshLayout.DefaultHeaderBuilder()
            .setPullRate(0.5f)
            .setPullOverRate(0.5f)
            .setOffsetListener(this)
            .setAnimVelocity(300)
            .build(getContext());
```



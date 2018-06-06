# HeaderRefresh

## 项目demo截图

- demo1 实现了某宝的首页功能

参见项目中的[ZhiFuBaoIndexView](http://note.youdao.com/)
    
![image](http://note.youdao.com/favicon.ico)

- demo2 实现了HeaderRefresh在使用appbar的情景

参见项目中的[AppBarDemoView](http://note.youdao.com/)

![image](http://note.youdao.com/favicon.ico)

## 项目说明


- 用recyclerView实现的带customHeader的刷新组件。刷新的部分出现在customHeader下面。

- 支持nestscrolling机制。如果有appbar，则当appbar完全展开后，继续拖拽，才会触发HeaderRefresh的下拉刷新。

- 自带一种默认的刷新头部[DefaultRefreshLayout](http://note.youdao.com/)。也可根据自己需求仿照DefaultRefreshLayout进行扩展。

## 组件结构

![image](http://note.youdao.com/favicon.ico)

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



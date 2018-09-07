### 自定义view实现弹幕效果 ###

## 预览图 ##
![Sample Screenshot](https://github.com/xiedong11/BarrageView/blob/master/picture/GIF.gif)

## 1.使用gradle引入控件到你的项目 ##
#### 在你的project.gradle中 ####
```
allprojects {
		repositories {
			...
			maven { url 'https://www.jitpack.io' }
		}
	}
```
#### 在你的app.gradle中 ####
```
 implementation 'com.github.xiedong11:BarrageView:v1.0'
```
## 2.开始使用 ##
#### 在初始化控件处装载弹幕内容 ####
```
 barrageView = (BarrageView) findViewById(R.id.barrage_view);
        List<String> list = new ArrayList<>();
        list.add("打算发大水发大水分打算发生的范德萨范德萨");
        list.add("fsdafds");
        list.add("哈哈的伙食费和的方式发大水");
        list.add("安顿");
        list.add("打算发大水发大水分打算");
        list.add("fsdafds");
        list.add("111111111111111111111111");
        barrageView.setBarrageItemList(list);
```

#### 绑定弹幕内容点击监听事件 ####
```
 barrageView.setOnBarrageItemClickListener(new IBarrageItemClickListener() {
            @Override
            public void onItemClick(String content) {
                Toast.makeText(MainActivity.this, content, Toast.LENGTH_SHORT).show();
            }
        });
```

#### 开启弹幕 ####
```
barrageView.startBarrageView();
```
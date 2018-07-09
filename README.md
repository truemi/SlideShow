# SlideShowView
[![](https://jitpack.io/v/truemi/SlideShow.svg)](https://jitpack.io/#truemi/SlideShow)
  
自动轮播图控件,自动添加小圆点指示器,标题栏展示,修改滚动速度以及添加动画插值器.
### 使用:  


- 添加依赖  
 1.项目gradle添加一下配置:  

		allprojects {
			repositories {
			...
			maven { url 'https://jitpack.io' }
			}
		}

	2.module中的gradle添加依赖:

		dependencies {
	        implementation 'com.github.truemi:SlideShow:1.0'
		} 
  
- xml中添加view:  
		
		<com.truemi.slideshow.SlideShowView
       		android:id="@+id/slide_show"
        	android:layout_width="match_parent"
        	android:layout_height="200dp"
        	app:mDotRaduis="8px"//小圆点半径
        	app:mDotNormalColor="#999999"//小圆点默认颜色
        	app:mDotSelectColor="#FF0000"//小圆点选中的颜色
        	app:mDotlocation="bottom_right"//小圆点显示的位置
        	app:mAutoStandTime="5000"//每个界面停留的时间间隔
        	app:mBottomTextView="true"//是否显示底部标题栏
        	app:mDotNavigation="true"//是否显示小圆点
        	app:mTextColor="#FFF"//标题栏文字颜色
        	app:mTextSize="12sp"//标题栏文字大小
        	app:mTextBgColor ="#44000000"//标题栏背景颜色
        	app:mBottomTextViewHeight="40dp">//标题栏高度
    	</com.truemi.slideshow.SlideShowView>
- activity中设置数据:
   
		//图片集合
		ArrayList<String> urlLists = new ArrayList<>();
        urlLists.add("https://img03.sogoucdn.com/app/a/100520024/c25c07885f822d67c91256b3033749e7");
        urlLists.add("https://img04.sogoucdn.com/app/a/100520024/ee6b8a48e6322e18a85a62ddcb01f432");
        urlLists.add("https://img01.sogoucdn.com/app/a/100520024/ebb532d5da0e26e285ac2dc025bc99ec");
        urlLists.add("https://img01.sogoucdn.com/app/a/100520024/83922cd9e4aaf9b4c012f08629a5e160");
		//标题栏文字集合
        String titles[] ={" 足球 ","设计 时尚"," 风华绝代 一代巨星张国荣","发现时光的痕迹"};
        final SlideShowView slideShow = findViewById(R.id.slide_show);
		//设置adapter,构造方法还可以传入图片资源id数组
        slideShow.setAdapter(new SlideAdapter(this,urlLists,titles));
		图片点击事件
        slideShow.setOnItemClickListener(new SlideShowView.OnViewPagerItemClickListener() {
            @Override
            public void onViewPagerItemClick(int position) {
                Toast.makeText(MainActivity.this,"点击了第"+position+"张图片", Toast.LENGTH_SHORT).show();
            }
        }); 

### 自定义属性:  

|  属性        | 值 |  描述  |
| --------   | -----   | ---- |
| mDotNavigation        | true/false      |   是否显示小圆点(默认显示)   |
| mBottomTextView        |  true/false     |  是否显示底部标题栏(默认显示)    |
| mDotRaduis     | 2dp     |   小圆点半径    |
| mDotNormalColor     | #999999     |   小圆点默认颜色     |
| mDotSelectColor     | #FF0000     |   小圆点选中的颜色(当前显示的)     |
| mDotlocation     | bottom_center     |   小圆点显示的位置(默认bottom_right)     |
| mDuration     | 500     |   页面切换时间     |
| mAutoStandTime     | 5000     |   页面停留时间     |
| mBottomTextViewHeight     | 40dp     |   标题栏高度     |
| mTextSize     | 12sp     |   标题栏文字大小   |
| mTextColor     | #FFFFFF     |   标题栏文字颜色     |
| mTextBgColor     | #44000000     |   标题栏背景颜色     |

### 公开方法:  

- setAdapter(SlideAdapter adapter);//设置adapter
- setDuration(int mDuration, Interpolator interpolator);//设置界面切换时间和动画插值器
- setPageTransformer(boolean b, ViewPager.PageTransformer transformer);//设置切换动画
- setOnItemClickListener;//设置点击事件
- setBottomTextBg(int color);//设置标题栏背景颜色
- setBottomTextColor(int color);//设置标题栏文字颜色
- setBottomTextSize(int size);//设置标题栏文字大小
#### _注意:_  

- 库中使用glide作为图片加载框架,如果你的项目中已经使用glide,直接删除你项目中的依赖即可
- 使用前请在androidManifest.xml中添加网络权限

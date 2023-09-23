#### UI
Android UI框架目前支持如下功能  
数据库  
轮播图片  
权限工具  
缓存数据  
语言设置  
常用正则  
消息提示  
桌面圆点  
圆角按钮  
设置空视图  
日期选择器  
滚轮选择器  
网页加载器  
流式布局列表  
短信发送倒计时  
支持状态栏修改  
快速设置标题栏  
数字处理、价格处理    
刷新、加载、侧滑删除  
#### 资源
|名字|操作|
|-|-|
|AAR|[下载](https://github.com/RelinRan/UI/blob/main/aar)|
|GitHub |[查看](https://github.com/RelinRan/UI)|
|Gitee|[查看](https://gitee.com/relin/UI)|
#### Maven
1.build.grade
```
allprojects {
    repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2./app/build.grade
```
dependencies {
	implementation 'com.github.RelinRan:UI:2023.9.23.1'
}
```
#### AppActivity
Layout
```
 @Override
 public int getContentViewLayoutResId() {
     return R.layout.xxx;
 }
```
ActionBar
```
AppActionBar actionBar = getAppActionBar();
```
返回点击
```
@Override
public void onBackClick(View v) {
    finish();
}
```
标题点击
```
@Override
public void onTitleClick(View v) {

}
```
菜单点击
```
@Override
public void onMenuClick(View v) {

}
```
#### AppFragment
Layout
```
 @Override
 public int getContentViewLayoutResId() {
     return R.layout.xxx;
 }
```
Fragment
```
@Override
public int getContainerViewResId() {
    //fragment_container为Activity内容布局中的View
    //通常为FrameLayout,id=fragment_container
    return R.id.fragment_container;
}

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //方式一
    MainFragment fragment = new MainFragment();
    Bundle bundle = new Bundle();
    bundle.putString("key","value");
    fragment.setArguments(bundle);
    addFragment(fragment);
    //方式二
    Bundle options = new Bundle();
    options.putString("key","value");
    addFragment(MainFragment.class,options);
}
```
#### Fragment
```
@Override
public int getContainerViewResId() {
    //fragment_container为Activity内容布局中的View
    //通常为FrameLayout,id=fragment_container
    return R.id.fragment_container;
}

@Override
protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //方式一
    MainFragment fragment = new MainFragment();
    Bundle bundle = new Bundle();
    bundle.putString("key","value");
    fragment.setArguments(bundle);
    addFragment(fragment);
    //方式二
    Bundle options = new Bundle();
    options.putString("key","value");
    addFragment(MainFragment.class,options);
}
```
#### AppActionBar
```
页面标题栏,主要设置标题栏、返回图标、右上角图标文字
```
标题
```
setTitle("xxx");
```
侵入式
```
setImmersed(boolean statusBarTextDark);
```
背景
```
//状态栏同时修改
setBackgroundColor(@ColorInt int color);
```
返回图标
```
setBackIconResource(@DrawableRes int resId);
```
返回文字
```
#### setBackText(String text);
```
返回文字大小
```
setBackTextSize(int size);
```
返回文字颜色
```
setBackTextColor(@ColorInt int color);
```
返回文字内间距
```
setBackTextPadding(int position, int padding);
```
状态栏颜色
```
setStatusBarColor(int color);
```
状态栏文字颜色
```
setStatusBarTextColor(boolean dark);
```
#### AppStatusBar
颜色
```
AppStatusBar.setColor(this, Color.parseColor("#3C3F41"));
```
文字
```
AppStatusBar.setTextColor(this,false);
```
高度
```
int height = AppStatusBar.height(this);
```
显示
```
AppStatusBar.show(this);
```
隐藏
```
AppStatusBar.hide(this);
```
#### Permission
权限对象
```
AppPermission permission = getAppPermission();
```
权限请求
```
getPermission().requestPermissions(AppPermission.GROUP_CAMERA,AppPermission.REQUEST_CODE)
```
权限回调
```
@Override
public void onRequestPermissionsGranted(String[] permissions) {
    super.onRequestPermissionsGranted(permissions);
    //已同意
}

@Override
public void onRequestPermissionsDenied(String[] permissions) {
    super.onRequestPermissionsDenied(permissions);
    //已取消
}

@Override
public void onRequestPermissionRationale(String[] permissions) {
    super.onRequestPermissionRationale(permissions);
    //不再提示
}
```
#### Loading
网络加载
```
showLoading("登陆中...");
```
隐藏加载
```
dismissLoading();
```
#### Toast
显示提示
```
showToast(String msg);
```
#### Login
设置登录
```
setLogin(boolean login);
```
是否登录
```
boolean isLogin()
```
#### Token
设置token
```
setToken(String token);
```
获取token
```
String getToken();
```
#### UserInfo
设置用户信息JSON
```
setUserInfo(String json);
```
获取用户信息JSON
```
String getUserInfo();
```
#### Placeholder
设置占位图
```
setPlaceholder(AppPlaceholder.PLACEHOLDER_EMPTY,R.mipmap.xxx);;
```
显示占位图
```
showPlaceholder(AppPlaceholder.PLACEHOLDER_EMPTY);
```
隐藏占位图
```
hidePlaceholder();
```
#### ShapeLayout
支持圆角背景的Layout
```
<androidx.ui.widget.ShapeLayout
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:solidColor="#BC68E0"
    app:radius="5dp"/>
</androidx.ui.widget.ShapeLayout>
```
#### ShapeText
支持圆角背景的TextView
```
<androidx.ui.widget.ShapeText
    android:layout_width="match_parent"
    android:layout_height="45dp"
    app:solidColor="#BC68E0"
    app:radius="5dp"
    android:text="item"
    android:textSize="14sp"/>
```
#### ShapeButton
支持圆角背景的按钮
```
<androidx.ui.widget.ShapeButton
    android:layout_width="match_parent"
    android:layout_height="45dp"
    app:solidColor="#BC68E0"
    app:radius="5dp"
    android:text="item"
    android:textSize="14sp"/>
```
####  CarouselPager
轮播图
```
<androidx.ui.carousel.CarouselPager
    android:id="@+id/carousel"
    android:layout_width="match_parent"
    android:layout_height="180dp"
    app:duration="3000"
    app:autoPlay="true"
    app:itemCount="1" />
```
Adapter
```
public class ImageAdapter extends CarouselAdapter<Integer> {

    public ImageAdapter(Context context) {
        super(context);
    }

    @Override
    public void onItemBindViewHolder(ViewHolder holder, int position) {
        ImageView target = holder.find(CarouselPager.RES_IMAGE);
        target.setImageResource(getItem(position));
        holder.addItemClick(target);
    }

}
```
自定义Layout
```
@Override
public int getItemLayoutResId() {
    return R.layout.xxx;
}
```
Items
```
CarouselPager carousel = findViewById(R.id.carousel);
ImageAdapter adapter = new ImageAdapter(context);
carousel.setAdapter(imageAdapter);
adapter.setOnItemClickListener((adapter, view, position) -> {
     
});
List<Integer> list = new ArrayList<>();
list.add(R.mipmap.ic_img_0);
list.add(R.mipmap.ic_img_1);
list.add(R.mipmap.ic_img_2);
adapter.setItems(list);
```
####  SwipeRefreshLayout
下拉刷新
```
<androidx.ui.refrsh.SwipeRefreshLayout
    android:id="@+id/swipe_refresh"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/white">

    <androidx.ui.recycler.SwipeRecyclerView
        android:id="@+id/rv_content"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        tools:itemCount="5"
        tools:listitem="@layout/ui_item"/>

</androidx.ui.refrsh.SwipeRefreshLayout>
```
颜色
```
setColorSchemeColors(Color.parseColor("#26C9B7"));
```
监听
```
swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
     @Override
     public void onRefresh() {
                
     }
});
```
结束
```
setRefreshing(false);
```
开始
```
setRefreshing(true);
```
#### MessageDialog
消息确认
```
MessageDialog messageDialog = new MessageDialog(getContext());
messageDialog.setTitle("提示");
messageDialog.setTitleVisibility(View.VISIBLE);
messageDialog.setContent("是否确认删除此项？");
messageDialog.setCancel("取消");
messageDialog.setConfirm("确认");
messageDialog.setOnMessageDialogListener(new MessageDialogCallback() {
    @Override
    public void onMessageDialogConfirm(MessageDialog dialog) {
        super.onMessageDialogConfirm(dialog);
        dialog.dismiss();
    }
});
messageDialog.show();
```
#### WheelPicker
滚轮选择器
```
List<String> list = new ArrayList<>();
for (int i = 0; i < 10; i++) {
    list.add(i+"");
}
WheelPicker picker = new WheelPicker(getContext(),1);
picker.setTitle("请选择");
picker.setOnWheelPickerConfirmListener(picker1 -> {
   String value = picker1.getSelected(true);
});
picker.setWheelsDataSource(0,list);
picker.show();
```
#### DatePicker
日期选择器
```
DatePicker picker = new DatePicker(getContext());
//选择范围
picker.setBoundary("2022-2-20","2050-2-20","yyyy-MM-dd");
picker.setOnDatePickerConfirmListener((picker1, date) -> {
    
});
//选中
picker.setSelected("2022-02-20", "yyyy-MM-dd");
picker.show();
```
#### PeriodPicker
时段选择器
```
PeriodPicker picker = new PeriodPicker(getContext());
//选择范围
picker.setBoundary("2022-2-20","2050-2-20","yyyy-MM-dd");
picker.setOnPeriodPickerConfirmListener((picker12, start, end) -> {
    
});
//选中
picker.setSelected("2022-02-20", "yyyy-MM-dd");
picker.show();
```
#### DateSwitchPicker
日期切换选择器（选择年、月、日）
```
DateSwitchPicker datePicker = new DateSwitchPicker(this);
datePicker.setOnDatePickerConfirmListener((picker, date) -> {
    showToast(date);
});
datePicker.setSelected("2022-02-20", "yyyy-MM-dd");
datePicker.show();
```
#### ClearEditTextView
快速清除内容的EditTextView
```
<androidx.ui.widget.ClearEditText
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:drawableRight="@drawable/android_ic_clear_text">
</androidx.ui.widget.ClearEditText>
```
清除图标
```
android:drawableRight="@drawable/android_ic_clear_text"
```
#### RecyclerAdapter
普通RecyclerView Adapter封装类
```
public class ItemAdapter extends RecyclerAdapter<String> {
    
    public ItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutResId(int viewType) {
        return R.layout.xxx;
    }

    @Override
    protected void onItemBindViewHolder(ViewHolder holder, int position) {
        holder.find(TextView.class,R.id.tv_name).setText("xxx");
        holder.addItemClick(R.id.tv_name);
    }

}
```
#### [SwipeRecyclerView](https://github.com/RelinRan/SwipeRecyclerView)
侧滑菜单列表
```
<androidx.ui.recycler.SwipeRecyclerView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"/>
```
#### [SwipeRecyclerAdapter](https://github.com/RelinRan/SwipeRecyclerView)
侧滑菜单适配器
```
public class ItemAdapter extends SwipeRecyclerAdapter<String> {

    public ItemAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemSwipeMenuLayoutResId() {
        //菜单布局
        return R.layout.xxx;
    }

    @Override
    protected int getItemLayoutResId(int viewType) {
        //普通item布局
        return R.layout.xxx;
    }

    @Override
    protected void onSwipeBindViewHolder(ViewHolder holder, int position) {
        super.onSwipeBindViewHolder(holder, position);
        //侧滑绑定数据
    }

    @Override
    protected void onItemBindViewHolder(ViewHolder holder, int position) {

    }
    
}
```
#### Time
现在时间
```
String now = Time.now(Time.DATE_FORMAT_YYYY_MM_DD);
```
时间戳
```
String time = Time.parseFromTimestamp(timestamp);
String time = Time.parseFromTimestamp(timestamp,Time.DATE_FORMAT_YYYY_MM_DD);
```
时间对象
```
Date date = Time.parse("2019-09-10 09:00:10");
Date date = Time.parse("2019-09-10 09:00:10",Time.DATE_FORMAT_YYYY_MM_DD_BLANK_24H_MM_SS);
```
时间差
```
long diff = Time.diff("2022-2-10","2022-2-20","yyyy-MM-dd");
```
#### Decimal
保留2位小数
```
String value = Decimal.format(2.351F,2);
```
保留2位小数,五入
```
String value = Decimal.format("2.351",2,Decimal.ROUND_HALF_UP);
```
保留2位小数,四舍
```
String value = Decimal.format("2.351",2,Decimal.ROUND_HALF_DOWN);
```
#### EditText
限制输入2位数,最大输入10位
```
EditText etPrice = findViewById(R.id.et_price);
Decimal.format(etPrice,2,10);
```
#### Text
判空
```
boolean isEmpty = Text.isEmpty("xxx");
```
赋值
```
boolean isAssign = Text.isAssign("xxx");
```
文本
```
String value = from(TextView textView);
```
#### Numeric
转换数字
```
//为空自动转为0
int intValue = Numeric.parseInt("null");
//Float自动加".00"
float floatValue = Numeric.parseFloat("2");
//Double自动加".00"
double doubleValue = Numeric.parseDouble("34");
```
转换字符
```
//为空自动转为0
String intValue = Numeric.toInt("null");
//Float自动加".00"
String floatValue = Numeric.toFloat("2");
//Double自动加".00"
String doubleValue = Numeric.toDouble("34");
```
#### Language
系统语言
```
Locale systemLanguage = Language.getSystem();
```
应用语言
```
Locale applicationLanguage = Language.getApplication(context);
```
修改语言
```
Language.update(context,Locale.US);
```
是否相同
```
boolean isSame =  Language.compare(source,target);
```
是否中文
```
Locale locale = Language.getSystem();
boolean isChinese =  Language.isChinese(locale);
```
#### WebLoader
加载URL
```
WebLoader.Builder builder = new WebLoader.Builder(webView);
builder.url("https://xxxxxxx");
builder.build();
```
Html数据
```
WebLoader.Builder builder = new WebLoader.Builder(webView);
builder.data("Html data");
builder.build();
```
图片点击
```
WebLoader.Builder builder = new WebLoader.Builder(webView);
builder.data("Html data");
builder.imageClickListener(new WebLoader.OnWebImageClickListener() {
@Override
public void onWebImageClick(String url) {

}
});
builder.build();
```
图片适应
```
WebLoader.Builder builder = new WebLoader.Builder(webView);
builder.data("Html data");
builder.imageFit(true);
builder.build();
```

#### AppActivityManager
添加页面
```
AppActivityManager.getInstance().add(MainActivity.class);
```
清除所有
```
AppActivityManager.getInstance().clear();
```
清除单个
```
AppActivityManager.getInstance().remove(MainActivity.class);
```
退出程序
```
AppActivityManager.getInstance().exit(context);
```
#### Badge
```
APP桌面角标-红色圆点，主要是显示红色圆点，但是不支持所有手机类型，目前支持小米、华为、三星、索尼。Badge已经做了缓存处理，同时去区别了多个项目在一个手机的缓存区别。  
```
增加数量
```
Badge.add(cntext);
```
重置数量
```
Badge.reset(context);
```
设置数量
```
Badge.setNumber(context,number);
```
获取数量
```
int number = Badge.number(context);
```
#### SQLite
初始化
```
SQLite.initialize(getApplicationContext());
```
建表
```
//方式1
SQLite.administrator().create("table_name",new String[]{"column_name_a","column_name_b"});
//方式2
User user = new User();
SQLite.administrator().create(user);
```
新增
```
//方式1
User user = new User();
SQLite.administrator().insert(user);
//方式2
SQLite.administrator().insert("sql");
//方式3
ContentValues values = new ContentValues();
values.put("user_id","1");
values.put("user_name","name");
SQLite.administrator().insert("table_name",values);
```
删除
```
//方式1
SQLite.administrator().delete("sql");
//方式2
SQLite.administrator().delete(User.class,"user_id=?",new String[]{"1"});
//方式3
SQLite.administrator().delete("table_name","user_id=?",new String[]{"1"});
```
更新
```
//方式1
SQLite.administrator().update("sql");
//方式2
User user = new User();
user.setUserName("Name");
ContentValues values = new ContentValues();
values.put("user_id","1");
values.put("user_name","name");
SQLite.administrator().update(user,values,"user_id=?",new String[]{"1"});
```
查询
```
//方式1
List<Map<String, String>> list = SQLite.administrator().query("sql");
//方式2
List<User> list = SQLite.administrator().query(User.class,"sql");
```
删除表数据
```
//方式1
SQLite.administrator().deleteTable("table_name");
//方式2
SQLite.administrator().deleteTable(User.class);
//方式3
SQLite.administrator().dropTable("table_name");//删除表
```
#### Validator
正则验证
自定义
```
Validator.REGEX_PHONE = "xxx";
```
手机号
```
Validator.isPhone(String number);
```
身份证号
```
Validator.isIdCard(String number);//粗略的校验
```
微信号
```
Validator.isWeChat(String number);
```
密码
```
Validator.isPassword(String password);//默认正则，密码长度为8到20位,必须包含字母和数字，字母区分大小写
```
数字
```
Validator.isNumeric(String number);
```
邮箱
```
Validator.isEmail(String mail);
```
QQ
```
Validator.isQQ(String number);
```


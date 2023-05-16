## Navigation 簡介

Navigation 是一個強大的庫，提供了一種簡單的方式來處理 Android 應用程序中不同fragment之間的導航。以下圖例，是在一個專案中導入navigation，並設置跳轉的一個例子。可以看到當完成一個專案時，可以一目瞭然整個轉案的跳轉邏輯。

![01.png](/images/navigation/01.png)

## Navigation 導入開發

將 Navigation 庫添加到項目的 build.gradle 文件中，添加相關library至build.gradle內。

注意：「Navigation」元件必須使用 Android Studio 3.3 或以上版本。

![02.png](/images/navigation/02.png)

## Navigation 導入開發

### 0. 快速透過IDE建立

透過 Android Studio 右鍵單擊項目目錄中的 res 文件夾 > New > Android Resource File 來新增 Navigation xml。
另外如果想純手動加入，也可以在 res 下建立 navigation folder 裡面新增 nav_graph.xml。

![03.png](/images/navigation/03.png)
![04.png](/images/navigation/04.png)

### 1. 建立 fragment 與設定 startDestination

**實際 folder 結構**

![05.png](/images/navigation/05.png)

加入 fragment：
- id = 命名 
- name = fragment 的 path 
- label = 標示訊息或類似 tag 
- tools:layout = 要預覽顯示的 layout xml resource

加入 startDestination：
- 例如：`app:startDestination="@id/landingFragment"`

![06.png](/images/navigation/06.png)

### 2. 在Activity的FragmentContainerView加入以下三行

```
android:name="androidx.navigation.fragment.NavHostFragment"
app:navGraph="@navigation/navigation_main" 
app:defaultNavHost="true"
```
![07.png](/images/navigation/07.png)
### 3. 加入action 並設定目的地

在 fragment 內加入 action，
加入後設定 destination，
如：app:destination="@id/signInFragment"
(action 寫在外面也可全域跳轉)
![08.png](/images/navigation/08.png)
    
### 4. 程式碼添加跳轉目的地即可跳轉

直接在要跳轉的地方加入

`findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)`
    
![09.png](/images/navigation/09.png)

### 5. 要跳轉Activity則跟上面fragment類似，換成activity即可

![010.png](/images/navigation/010.png)

### 6. 使用多張nav graph，可使用嵌套方式或 include

直接再加入一張navigation的方式：
![011.png](/images/navigation/011.png)

新增一張 nav graph xml後使用include導入的方式：
![012.png](/images/navigation/012.png)
## Navigation 開發案例 - dialog fragment

#### 從dialog fragment進入

跟上面一樣在nav graph 直接加入 dialog tag
並導入你做好的DialogFragment就能用
(id = 命名 , name = fragment的path,  label = 標示訊息或類似tag, tools:layout = 要顯示的layout xml resource)

![013.png](/images/navigation/013.png)


## Navigation 開發案例 - 傳遞變數

### 加入argument

一樣在nav graph 直接加入argument，則可以預設帶入傳遞的變數 (argType = 變數型態, defaultValue = 預設值)

![014.png](/images/navigation/04.png)

### 程式碼傳遞變數

a. 不使用上面xml的方式預設值，可以下方兩組程式碼直接傳遞變數
b. 如果是有用xml方式帶入預設值的，則可以用下面收變數的程式碼直接接收

```
/*** 傳變數 ***/
val bundle = bundleOf("argName" to 123)
view.findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment, bundle)

/*** 收變數 ***/
arguments?.getInt("argName")
```
### navigation有支援的變數型態

![020.png](/images/navigation/020.png)

## Navigation 開發案例 - 動畫

### 支援直接設定跳轉動畫

帶入 `enterAnim`, `exitAnim`, `popEnterAnim`, `popExitAnim` 則可以輕鬆預設跳轉動畫

```
 <action
      android:id="@+id/confirmationAction"
      app:destination="@id/confirmationFragment"
      app:enterAnim="@anim/slide_in_right"
      app:exitAnim="@anim/slide_out_left"
      app:popEnterAnim="@anim/slide_in_left"
      app:popExitAnim="@anim/slide_out_right" />
```

### 支援Activity加上彈出動畫

```
override fun finish() {
    super.finish()
     ActivityNavigator.applyPopAnimationsToPendingTransition(this)
}
```

## Navigation 開發案例 - 支援 multiple back stacks

#### 支援處理多個back stack 的方式
透過 Navigation 提供的api讓相關view關聯  
便可處理多個back stack跳轉問題  
例如setupWithNavController 與bottom navigation view關聯  
setupActionBarWithNavController 與action bar關聯 

```Kotlin
val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) 
        as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById(R.id.bottom_nav)
        bottomNavigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
```

## Navigation 開發案例 - popUpTo 及 popUpToInclusive

### 透過 popUpTo 導向目的地頁面時，移除該頁面stack上的所有頁面
### 透過 popUpToInclusive，把最上層相同的頁面移除

此圖，闡述了一般跳轉邏輯

![015.png](/images/navigation/015.png))


fragment依照順序跳轉 1 -> 2 -> 3 最後再從 3回到1 
且再次跳轉 1 -> 2 -> 3 再回到1
此時眾fragment 在back stack內 順序為[1,2,3,1,2,3,1]

```
action
    android:id="@+id/action_third_to_first"
    app:destination="@id/FirstFragment"
    app:popUpTo="@+id/FirstFragment"
    app:popUpToInclusive="true"/>
```

若是想要跳轉回1的時候
stack內存放的fragment不再有目的地以上的instance 的話可以在xml的action內加入 
popUpTo 與 popUpToInclusive
這邊便可以讓下次跳轉回目的地fragment 上方有個stack instance給清除掉

#### 若以實際範例來看，加入前
[點擊查看加入前demo影片.mov](/images/navigation/016.mov)

#### 若以實際範例來看，加入後
[點擊查看加入後demo影片.mov](/images/navigation/017.mov)
        
        
## Navigation 開發案例 - NavOptions 
### 透過 NavOptions 來設置NavController

同樣navigation也提供對應builder讓你設置上面講到過的一些功能，NavOptions範例：
```
val options = NavOptions.Builder()
    .set....
    .build()
```

最後跳轉時帶入
```
findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment,null, options)
```

### 可進行的設置項目有：
![018.png](/images/navigation/018.png)


## Navigation 相關參考
### 官方文件：
https://developer.android.com/guide/navigation?hl=zh-tw

### Demo code ：
https://github.com/KuanChunChen/NavigationGraphDemo

### 投影片解說：
https://tome.app/kcchen/navigation-with-kotlin-clhh1vxwh02wk9w3ynag6cmat



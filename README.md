# 透明fragment,子类用于封装实现一些长逻辑

## 导包
```gradle
// app/build.gradle
// 需要kotlin支持

implementation 'org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.61'
implementation 'com.android.support:appcompat-v7:28.0.0'
implementation 'org.lynxz.transfragment:TransFragment:1.0.0'
```

## `BaseTransFragment`
 * 透明fragment基类,子类用于封装实现一些长逻辑,并对外暴露调用方法
 * 使用方法:
 * 1. 子类fragment不可见,实现各种业务逻辑,并对外暴露调用方法
 * 2. 在activity中通过调用 [getTransFragment] 来将子类fragment添加到activity中,若返回null表示注入失败;

 ## `PermissionFragment` 权限申请
 * 封装了权限申请长流程 Fragment
1. 通过 [isPermissionGranted] 来判断权限是否已被授权
2. 通过 [requestPermissions] 来批量申请权限
3. 通过 [requestPermission] 来申请某个权限的授权
4. 通过 [requestPermissionWithDialogIfNeeded] 来申请某个权限的授权,并按需弹出申请理由dialog
5. 通过 [startSettingActivity] 来跳转到设置页面
6. 使用示例:
```kotlin
 // 1. 在activity#onCreate()后,注入权限申请fragment
 val permissionFrag = BaseTransFragment.getTransFragment(hostActivity, "permission_tag", PermissionFragment())

 // 2. 设置回调接口
 val permissionCallback = object : IPermissionCallback {
     override fun onRequestResult(permission: PermissionResultInfo) {
         // 具体某个权限的授权结果
         Logger.d("授权结果\n权限名=${permission.name},是否授权=${permission.granted},是否可再弹出系统权限框=${permission.shouldShowRequestPermissionRationale}")
     }

     override fun onAllRequestResult(allGranted: Boolean) {
         // 所申请的权限是否全部都通过了
     }
 }

 // 3. [可选] 设置特定权限的申请方法,主要用于非dangerous权限申请,如电池优化白名单
 permissionFrag?.registerPermissionChecker(IPermissionChecker)

 // 4. 申请单个权限(步骤3有效)
 permissionFrag?.requestPermission(Manifest.permission.RECORD_AUDIO, permissionCallback)

 // 5. 申请单个权限,并按需弹出dialog跳转到设置页面
 permissionFrag?.requestPermissionWithDialogIfNeeded(Manifest.permission.RECORD_AUDIO, "缺少录音权限", "请点击确定按钮到设置页面开启权限", permissionCallback)

 // 6. 批量申请权限
 permissionFrag?.requestPermissions(arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA), permissionCallback)
 ```
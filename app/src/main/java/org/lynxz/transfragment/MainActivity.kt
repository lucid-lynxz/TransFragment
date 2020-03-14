package org.lynxz.transfragment

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.lynxz.transfragment_lib.BaseTransFragment
import org.lynxz.transfragment_lib.IPermissionCallback
import org.lynxz.transfragment_lib.PermissionFragment
import org.lynxz.transfragment_lib.PermissionResultInfo

class MainActivity : AppCompatActivity() {

    val permissionCallback = object : IPermissionCallback {
        override fun onRequestResult(permission: PermissionResultInfo) {
            // 具体某个权限的授权结果
            val msg =
                "授权结果\n权限名=${permission.name},是否授权=${permission.granted},是否可再弹出系统权限框=${permission.shouldShowRequestPermissionRationale}\n"
            LoggerUtil.d(msg)
            tv_info.append(msg)
        }

        override fun onAllRequestResult(allGranted: Boolean) {
            // 所申请的权限是否全部都通过了
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val permissionFrag =
            BaseTransFragment.getTransFragment(this, "permission_tag", PermissionFragment())

        // 申请单个权限
        btn_req_single.setOnClickListener {
            permissionFrag?.requestPermission(Manifest.permission.RECORD_AUDIO, permissionCallback)
        }

        // 同时申请多个权限
        btn_req_multi.setOnClickListener {
            permissionFrag?.requestPermissions(
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                ), permissionCallback
            )
        }

        btn_clear_log.setOnClickListener {
            tv_info.text = null
        }
    }
}

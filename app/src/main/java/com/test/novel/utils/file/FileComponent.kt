package com.test.novel.utils.file

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class FileComponent(private val fragment: Fragment) {

    private var openGalleryLauncher: ActivityResultLauncher<Unit?>? = null
    private var takePhotoLauncher: ActivityResultLauncher<Unit?>? = null
    private var openDocumentLauncher: ActivityResultLauncher<Unit?>? = null

    init {
        initializeLaunchers()
    }

    private fun initializeLaunchers() {
        // 注册图片选择
        openGalleryLauncher = fragment.registerForActivityResult(SelectPicture()) { result ->
            if (result.isSuccess) {
                handleGalleryResult(result.uri)
            } else {
                Log.d("FileComponentNativeV2", "Gallery selection failed")
            }
        }

        // 注册拍照
        takePhotoLauncher = fragment.registerForActivityResult(TakePhoto.instance) { result ->
            if (result.isSuccess) {
                handlePhotoResult(result.uri)
            } else {
                Log.d("FileComponentNativeV2", "Photo capture failed")
            }
        }

        // 注册文档选择
        openDocumentLauncher = fragment.registerForActivityResult(SelectDocument()) { result ->
            if (result.isSuccess) {
                handleDocumentResult(result.uri)
            } else {
                Log.d("FileComponentNativeV2", "Document selection failed")
            }
        }
    }

    // 处理相册选择结果
    private fun handleGalleryResult(uri: Uri?) {
        uri?.let {
            Log.d("FileComponentNativeV2", "Selected image URI: $uri")
            // 执行相应逻辑，比如上传图片或显示在 UI
        }
    }

    // 处理拍照结果
    private fun handlePhotoResult(uri: Uri?) {
        uri?.let {
            Log.d("FileComponentNativeV2", "Captured image URI: $uri")
            // 处理拍照逻辑，比如上传到服务器或显示
        }
    }

    // 处理文档选择结果
    private fun handleDocumentResult(uri: Uri?) {
        uri?.let {
            Log.d("FileComponentNativeV2", "Selected document URI: $uri")
            // 执行文档处理，比如解析或上传
        }
    }

    private val permissionLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openGalleryLauncher?.launch(null)
        } else {
            showPermissionDialog()
        }
    }

    private val cameraPermissionLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            takePhotoLauncher?.launch(null)
        } else {
            showPermissionDialog()
        }
    }

    private val documentPermissionLauncher = fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            openDocumentLauncher?.launch(null)
        } else {
            showPermissionDialog()
        }
    }

    private fun showPermissionDialog() {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("权限请求")
            .setMessage("需要权限以完成操作。请到设置中授予权限。")
            .setPositiveButton("前往设置") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                intent.data = Uri.fromParts("package", fragment.requireContext().packageName, null)
                fragment.requireContext().startActivity(intent)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    fun selectImage() {
        val readGalleryPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), readGalleryPermission) == PackageManager.PERMISSION_GRANTED) {
            openGalleryLauncher?.launch(null)
        } else {
            permissionLauncher.launch(readGalleryPermission)
        }
    }

    fun takePhoto() {
        val cameraPermission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), cameraPermission) == PackageManager.PERMISSION_GRANTED) {
            takePhotoLauncher?.launch(null)
        } else {
            cameraPermissionLauncher.launch(cameraPermission)
        }
    }

    fun selectDocument() {
        val readDocumentPermission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(fragment.requireContext(), readDocumentPermission) == PackageManager.PERMISSION_GRANTED) {
            openDocumentLauncher?.launch(null)
        } else {
            documentPermissionLauncher.launch(readDocumentPermission)
        }
    }
}




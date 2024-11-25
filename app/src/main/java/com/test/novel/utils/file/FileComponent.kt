package com.test.novel.utils.file

import android.util.Log
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
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

    // 调用相册选择功能
    fun selectImage() {
        openGalleryLauncher?.launch(null)
    }

    // 调用拍照功能
    fun takePhoto() {
        takePhotoLauncher?.launch(null)
    }

    // 调用文档选择功能
    fun selectDocument() {
        openDocumentLauncher?.launch(null)
    }
}



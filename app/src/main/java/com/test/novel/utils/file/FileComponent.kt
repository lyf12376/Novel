package com.test.novel.utils.file

import android.Manifest
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.test.novel.database.bookShelf.BookInShelf
import com.test.novel.database.bookShelf.BookShelfDao
import com.test.novel.database.chapter.Chapter
import com.test.novel.database.chapter.ChapterDao
import com.test.novel.utils.AppUtils
import com.test.novel.utils.Utils.arabicNumberToChinese
import dagger.hilt.EntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject
import kotlin.time.measureTime

class FileComponent(
    private val fragment: Fragment,
    private val bookShelfDao: BookShelfDao,
    private val chapterDao: ChapterDao,
) {

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
                Log.d("TAG", "initializeLaunchers: ${result.uri}")
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
            CoroutineScope(Dispatchers.IO).launch {
                val contentResolver = AppUtils.context.contentResolver
                val fileName = getFileName(contentResolver, uri).split(".")[0]
                var chapters:List<Chapter>
                val time = measureTime { chapters = extractChaptersAsync(contentResolver, uri) }
                //生成书本id(真随机数)
                val bookId = run {
                    var id: Int
                    while (true) {
                        id = (0..1000000).random()
                        if (bookShelfDao.getBookById(id) == null)
                            break
                    }
                    id
                }
                bookShelfDao.addBook(BookInShelf(bookId = bookId, isLocal = true, title = fileName))
                // 打印每章标题和内容
                val newChapters = chapters.map {
                    it.copy(bookId = bookId)
                }

                chapterDao.insert(
                    newChapters
                )
            }
        }
    }

    private suspend fun extractChaptersAsync(
        contentResolver: ContentResolver,
        uri: Uri
    ): List<Chapter> = withContext(Dispatchers.IO) {
        fun startsWithNumber(input: String): Boolean {
            val s = input.split(" ")[0]
            val regex = Regex("^\\d+")
            return regex.containsMatchIn(s)
        }
        println("开始解析文档: $uri")
        val chapters = mutableListOf<Chapter>()
        val contentBuilder = StringBuilder()
        var currentChapterTitle: String? = null
        var index = 1
        var isMainText = false
        var biref = ""
        contentResolver.openBufferedReader(uri).use { reader ->
            reader.lineSequence().forEachIndexed { index1, line ->
                if (Regex("^第.+章.*").find(line)?.range?.first == 0 || startsWithNumber(line)) {
                    // 保存上一章节
                    if (!isMainText) {
                        currentChapterTitle = line
                        isMainText = true
                        biref = contentBuilder.toString()
                        contentBuilder.clear()
                        return@forEachIndexed
                    }
                    currentChapterTitle?.let {
                        chapters.add(Chapter(chapterNumber = index ++ , title = it , content = contentBuilder.toString()))
                        contentBuilder.clear()
                    }
                    currentChapterTitle = line
                } else {
                    contentBuilder.appendLine(line)
                }
            }
        }

        // 保存最后一章
        currentChapterTitle?.let {
            chapters.add(Chapter(chapterNumber = index , title = it , content = contentBuilder.toString()))
        }
        chapters.forEach{
            println("章节标题: ${it.title}")
        }
        chapters
    }

    // 扩展函数：简化流操作
    private fun ContentResolver.openBufferedReader(uri: Uri): BufferedReader =
        openInputStream(uri)?.bufferedReader()
            ?: throw IllegalArgumentException("无法读取文件: $uri")


    // 辅助函数：从 URI 获取文件名
    private fun getFileName(contentResolver: ContentResolver, uri: Uri): String {
        var fileName = "unknown"
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            cursor.moveToFirst()
            fileName = cursor.getString(nameIndex)
        }
        return fileName
    }

    private val permissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openGalleryLauncher?.launch(null)
            } else {
                showPermissionDialog(0)
            }
        }

    private val cameraPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                takePhotoLauncher?.launch(null)
            } else {
                showPermissionDialog(0)
            }
        }

    private val documentPermissionLauncher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                openDocumentLauncher?.launch(null)
            } else {
                showPermissionDialog(1)
            }
        }

    private fun showPermissionDialog(mode: Int) {
        AlertDialog.Builder(fragment.requireContext())
            .setTitle("权限请求")
            .setMessage("需要权限以完成操作。请到设置中授予权限。")
            .setPositiveButton("前往设置") { _, _ ->
                val intent =
                    Intent(if (mode == 1) Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION else Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
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
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                readGalleryPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openGalleryLauncher?.launch(null)
        } else {
            permissionLauncher.launch(readGalleryPermission)
        }
    }

    fun takePhoto() {
        val cameraPermission = Manifest.permission.CAMERA
        if (ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                cameraPermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePhotoLauncher?.launch(null)
        } else {
            cameraPermissionLauncher.launch(cameraPermission)
        }
    }

    fun selectDocument() {
        val readDocumentPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Manifest.permission.MANAGE_EXTERNAL_STORAGE
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (isManageStorageGranted()) {
            openDocumentLauncher?.launch(null)
        } else {
            documentPermissionLauncher.launch(readDocumentPermission)
        }
    }

    private fun isManageStorageGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }


}




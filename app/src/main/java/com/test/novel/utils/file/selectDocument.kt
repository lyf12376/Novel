package com.test.novel.utils.file

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class SelectDocument : ActivityResultContract<Unit?, DocumentResult>() {
    private var context: Context? = null

    override fun createIntent(context: Context, input: Unit?): Intent {
        this.context = context
        return Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf(
                "text/plain",
                "application/json",
            ))
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): DocumentResult {
        return DocumentResult(intent?.data, resultCode == Activity.RESULT_OK)
    }
}

data class DocumentResult(val uri: Uri?, val isSuccess: Boolean)


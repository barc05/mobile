package com.example.mattapp_proyect.utils

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

fun uriToMultipart(context: Context, uri: Uri, partName: String = "archivo"): MultipartBody.Part {
    val resolver: ContentResolver = context.contentResolver
    val inputStream: InputStream? = resolver.openInputStream(uri)

    // Intentar obtener el nombre real del archivo
    var fileName = "upload_${System.currentTimeMillis()}.jpg"
    resolver.query(uri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1) fileName = cursor.getString(nameIndex)
        }
    }

    // Crear archivo temporal
    val tempFile = File(context.cacheDir, fileName)
    inputStream?.use { input ->
        FileOutputStream(tempFile).use { output ->
            input.copyTo(output)
        }
    }

    val reqFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), tempFile)
    return MultipartBody.Part.createFormData(partName, tempFile.name, reqFile)
}

fun String.toPlainTextRequestBody(): RequestBody =
    this.toRequestBody("text/plain".toMediaType())
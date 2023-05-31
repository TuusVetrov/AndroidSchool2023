package com.example.hxh_project.utils.extensions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.IOException

@Throws(IOException::class)
fun Context.getBitmapFromUri(uri: Uri, options: BitmapFactory.Options? = null): Bitmap? {
    val parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "r")
    val fileDescriptor = parcelFileDescriptor?.fileDescriptor
    val image: Bitmap? = if (options != null)
        BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
    else
        BitmapFactory.decodeFileDescriptor(fileDescriptor)
    parcelFileDescriptor?.close()
    return image
}

fun Context.getImageHgtWdt(uri: Uri): Pair<Int, Int> {
    val opt = BitmapFactory.Options()

    /* by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded.
    If you try the use the bitmap here, you will get null.*/
    opt.inJustDecodeBounds = true
    val bm = getBitmapFromUri(uri, opt)

    var actualHgt = (opt.outHeight).toFloat()
    var actualWdt = (opt.outWidth).toFloat()

    /*val maxHeight = 816.0f
    val maxWidth = 612.0f*/
    val maxHeight = 720f
    val maxWidth = 1280f
    var imgRatio = actualWdt / actualHgt
    val maxRatio = maxWidth / maxHeight

//    width and height values are set maintaining the aspect ratio of the image
    if (actualHgt > maxHeight || actualWdt > maxWidth) {
        when {
            imgRatio < maxRatio -> {
                imgRatio = maxHeight / actualHgt
                actualWdt = (imgRatio * actualWdt)
                actualHgt = maxHeight
            }
            imgRatio > maxRatio -> {
                imgRatio = maxWidth / actualWdt
                actualHgt = (imgRatio * actualHgt)
                actualWdt = maxWidth
            }
            else -> {
                actualHgt = maxHeight
                actualWdt = maxWidth
            }
        }
    }

    return Pair(actualHgt.toInt(), actualWdt.toInt())
}
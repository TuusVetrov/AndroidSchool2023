package com.example.hxh_project.utils.extensions

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.hxh_project.R
import com.example.hxh_project.presentation.ui.sign_in.SignInFragment
import com.example.hxh_project.utils.*
import com.example.hxh_project.utils.ScalingUtils.createScaledBitmap
import com.example.hxh_project.utils.ScalingUtils.decodeFile
import com.example.hxh_project.utils.ScalingUtils.getImageQualityPercent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream

inline fun <reified F : Fragment> Fragment.navigateTo(addToBackStack: Boolean) {
    parentFragmentManager.commit {
        replace<F>(R.id.main_activity_container)
        if(addToBackStack) addToBackStack(null)
    }
}

fun Fragment.navigateLogout() {
    parentFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    parentFragmentManager.commit {
        replace<SignInFragment>(R.id.main_activity_container)
    }
}

fun Fragment.setWindowTransparency(view: View, listener: OnSystemInsetsChangedListener = { _, _ -> }) {
    InsetUtil.removeSystemInsets(view, listener)
}

suspend fun Fragment.compressImageFile(
    path: String,
    shouldOverride: Boolean = true,
    uri: Uri
): String = withContext(Dispatchers.IO) {
    var scaledBitmap: Bitmap? = null

    try {
        val (hgt, wdt) = requireContext().getImageHgtWdt(uri)
        try {
            val bm = requireContext().getBitmapFromUri(uri)
            Log.d(tag, "original bitmap height${bm?.height} width${bm?.width}")
            Log.d(tag, "Dynamic height$hgt width$wdt")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        // Part 1: Decode image
        val unscaledBitmap = decodeFile(requireContext(), uri, wdt, hgt, ScalingUtils.ScalingLogic.FIT)
        if (unscaledBitmap != null) {
            if (!(unscaledBitmap.width <= 800 && unscaledBitmap.height <= 800)) {
                // Part 2: Scale image
                scaledBitmap = createScaledBitmap(unscaledBitmap, wdt, hgt, ScalingUtils.ScalingLogic.FIT)
            } else {
                scaledBitmap = unscaledBitmap
            }
        }

        // Store to tmp file
        val mFolder = File(requireContext().filesDir, "Images")
        if (!mFolder.exists()) {
            mFolder.mkdir()
        }

        val tmpFile = File(mFolder.absolutePath, "IMG_${FormatUtils.getTimestampString()}.png")

        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(tmpFile)
            scaledBitmap?.compress(Bitmap.CompressFormat.PNG, getImageQualityPercent(tmpFile), fos)
            fos.flush()
            fos.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var compressedPath = ""
        if (tmpFile.exists() && tmpFile.length() > 0) {
            compressedPath = tmpFile.absolutePath
            if (shouldOverride) {
                val srcFile = File(path)
                val result = tmpFile.copyTo(srcFile, true)
                Log.d(tag, "copied file ${result.absolutePath}")
                Log.d(tag, "Delete temp file ${tmpFile.delete()}")
            }
        }

        scaledBitmap?.recycle()

        return@withContext if (shouldOverride) path else compressedPath
    } catch (e: Throwable) {
        e.printStackTrace()
        return@withContext ""
    }
}
package com.example.hxh_project.utils

import android.content.Context
import android.graphics.*
import android.net.Uri
import com.example.hxh_project.utils.extensions.getBitmapFromUri
import java.io.File

object ScalingUtils {
    fun decodeFile(
        context: Context,
        uri: Uri,
        dstWidth: Int,
        dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap? {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        context.getBitmapFromUri(uri, options)
        options.inJustDecodeBounds = false

        options.inSampleSize = calculateSampleSize(
            options.outWidth,
            options.outHeight,
            dstWidth,
            dstHeight,
            scalingLogic
        )

        return context.getBitmapFromUri(uri, options)
    }

    /**
     * Utility function for creating a scaled version of an existing bitmap
     *
     * @param unscaledBitmap Bitmap to scale
     * @param dstWidth Wanted width of destination bitmap
     * @param dstHeight Wanted height of destination bitmap
     * @param scalingLogic Logic to use to avoid image stretching
     * @return New scaled bitmap object
     */
    fun createScaledBitmap(
        unscaledBitmap: Bitmap, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Bitmap {
        val srcRect = calculateSrcRect(
            unscaledBitmap.width, unscaledBitmap.height,
            dstWidth, dstHeight, scalingLogic
        )
        val dstRect = calculateDstRect(
            unscaledBitmap.width,
            unscaledBitmap.height,
            dstWidth,
            dstHeight,
            scalingLogic
        )
        val scaledBitmap =
            Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888)
        val canvas = Canvas(scaledBitmap)
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, Paint(Paint.FILTER_BITMAP_FLAG))

        return scaledBitmap
    }

    /**
     * ScalingLogic defines how scaling should be carried out if source and
     * destination image has different aspect ratio.
     *
     * CROP: Scales the image the minimum amount while making sure that at least
     * one of the two dimensions fit inside the requested destination area.
     * Parts of the source image will be cropped to realize this.
     *
     * FIT: Scales the image the minimum amount while making sure both
     * dimensions fit inside the requested destination area. The resulting
     * destination dimensions might be adjusted to a smaller size than
     * requested.
     */
    enum class ScalingLogic {
        CROP, FIT
    }

    /**
     * Calculate optimal down-sampling factor given the dimensions of a source
     * image, the dimensions of a destination area and a scaling logic.
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal down scaling sample size for decoding
     */
    fun calculateSampleSize(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Int {
        if (scalingLogic == ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcWidth / dstWidth
            } else {
                srcHeight / dstHeight
            }
        } else {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                srcHeight / dstHeight
            } else {
                srcWidth / dstWidth
            }
        }
    }

    /**
     * Calculates source rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal source rectangle
     */
    fun calculateSrcRect(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Rect {
        if (scalingLogic == ScalingLogic.CROP) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            return if (srcAspect > dstAspect) {
                val srcRectWidth = (srcHeight * dstAspect).toInt()
                val srcRectLeft = (srcWidth - srcRectWidth) / 2
                Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight)
            } else {
                val srcRectHeight = (srcWidth / dstAspect).toInt()
                val scrRectTop = (srcHeight - srcRectHeight) / 2
                Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight)
            }
        } else {
            return Rect(0, 0, srcWidth, srcHeight)
        }
    }

    /**
     * Calculates destination rectangle for scaling bitmap
     *
     * @param srcWidth Width of source image
     * @param srcHeight Height of source image
     * @param dstWidth Width of destination area
     * @param dstHeight Height of destination area
     * @param scalingLogic Logic to use to avoid image stretching
     * @return Optimal destination rectangle
     */
    fun calculateDstRect(
        srcWidth: Int, srcHeight: Int, dstWidth: Int, dstHeight: Int,
        scalingLogic: ScalingLogic
    ): Rect {
        return if (scalingLogic == ScalingLogic.FIT) {
            val srcAspect = srcWidth.toFloat() / srcHeight.toFloat()
            val dstAspect = dstWidth.toFloat() / dstHeight.toFloat()

            if (srcAspect > dstAspect) {
                Rect(0, 0, dstWidth, (dstWidth / srcAspect).toInt())
            } else {
                Rect(0, 0, (dstHeight * srcAspect).toInt(), dstHeight)
            }
        } else {
            Rect(0, 0, dstWidth, dstHeight)
        }
    }

    fun getImageQualityPercent(file: File): Int {
        val sizeInBytes = file.length()
        val sizeInKB = sizeInBytes / 1024
        val sizeInMB = sizeInKB / 1024

        return when {
            sizeInMB <= 1 -> 80
            sizeInMB <= 2 -> 60
            else -> 40
        }
    }
}
package com.aic.advancedimageconverter.ui

import android.graphics.Bitmap
import coil.request.ImageRequest
import coil.request.ImageResult
import com.aic.advancedimageconverter.ImageLoad
import kotlinx.coroutines.Deferred
import java.io.File

data class ImageConversion(
    val image: File,
    val targetFormat: Bitmap.CompressFormat,
    val targetPath: File,
    val quality: Int = 100
) {
    val size by lazy { image.length() }
    val convertedImage: File =
        File(targetPath, image.nameWithoutExtension + "." + targetFormat.name)

    val convertedSize: Long by lazy { convertedImage.length() }

    var status: Status = Waiting()
    val request = ImageRequest.Builder(ImageLoad.context!!)
        .data(image)
        .build()
    var conversionJob: Deferred<ImageResult>? = null
        private set

    fun setJob(job: Deferred<ImageResult>) {
        conversionJob = job
    }

    fun deleteIfBigger() {
        if (size <= convertedSize) {
            convertedImage.delete()
            val newFile = File(
                convertedImage.parentFile,
                convertedImage.nameWithoutExtension + "." + image.extension
            )
            if (!newFile.exists()) image.copyTo(newFile)
        }

    }


    sealed class Status()
    class Waiting() : Status()
    class Processing() : Status()
    class Done() : Status()

}

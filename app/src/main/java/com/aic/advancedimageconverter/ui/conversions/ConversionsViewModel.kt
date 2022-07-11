package com.aic.advancedimageconverter.ui.conversions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aic.advancedimageconverter.ImageLoad
import com.aic.advancedimageconverter.ui.ImageConversion
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import java.io.File

class ConversionsViewModel : ViewModel() {


    val imageLoad by lazy { ImageLoad.instance!! }

    val targetFormat by lazy { Bitmap.CompressFormat.WEBP }

    var tpath = File("")

    val quality = 100

    val total = MutableLiveData(0)

    val done = MutableLiveData(0)

    private var job: Job? = null

    fun convert(context: Context?) {
        val path = context?.getExternalFilesDir("images")!!
        tpath = context.getExternalFilesDir("converted")!!
        if (job == null || job?.isCancelled == true || job?.isCompleted == true)
            job = flow {
                path.mkdirs()
                tpath.mkdirs()
                total.value = path.listFiles()?.map { emit(it) }?.size ?: 0
            }.map { file -> ImageConversion(file, targetFormat, tpath, quality) }

                .flatMapMerge(8) { imageConversion ->
                    flow {
                        if (imageConversion.convertedImage.exists())
                            return@flow emit(imageConversion.apply { setSuccessful() })

                        imageLoad.execute(imageConversion.request).drawable
                            ?.let {
                                val bitmap = (it as BitmapDrawable).bitmap
                                imageConversion.convert(bitmap)
                            }
                        imageConversion.deleteIfBigger()
                        emit(imageConversion)
                    }.flowOn(Dispatchers.Default)
                }

                .onEach { if (it.isSuccessful) done.value = (done.value!! + 1) }
                .flowOn(Dispatchers.Main)
                .launchIn(this.viewModelScope)

    }

    fun stop() {
        job?.cancel()
        total.value = 0
        done.value = 0
    }

    companion object {
        var instance: ConversionsViewModel? = null
    }

    init {
        instance = this
    }

}
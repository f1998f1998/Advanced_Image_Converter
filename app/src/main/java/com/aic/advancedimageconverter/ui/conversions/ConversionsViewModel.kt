package com.aic.advancedimageconverter.ui.conversions

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.LiveData
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

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text

    val imageLoad by lazy { ImageLoad.instance!! }

    val targetFormat by lazy { Bitmap.CompressFormat.WEBP }

    var tpath = File("")

    val quality = 100

    val conversions = mutableSetOf<ImageConversion>()

    val total = MutableLiveData(0)

    val done = MutableLiveData(0)

    private var job: Job? = null

    fun add(image: File) {
        conversions.add(ImageConversion(image, targetFormat, tpath, quality))
        total.value = conversions.size
    }

    fun convert(context: Context?) {
        val path = context?.getExternalFilesDir("images")!!
        tpath = context.getExternalFilesDir("converted")!!
        path.mkdirs()
        tpath.mkdirs()
        path.listFiles()?.map { add(it) }
        if (job == null || job?.isCancelled == true || job?.isCompleted == true) job =
            conversions.asFlow()
                .flatMapMerge(8) { imageConversion->
                    flow {
                        if (imageConversion.convertedImage.exists()) return@flow emit(true to imageConversion)
                        imageConversion.status = ImageConversion.Processing()
                        imageLoad.execute(imageConversion.request).drawable
                            ?.let {

                                val bitmap =
                                    (it as BitmapDrawable).bitmap
                                imageConversion.convertedImage.outputStream().use { out ->
                                    bitmap.compress(
                                        imageConversion.targetFormat,
                                        imageConversion.quality,
                                        out
                                    )
                                }
                                imageConversion.status = ImageConversion.Done()
                                emit(true to imageConversion)

                            } ?: emit(false to imageConversion)
                    }
                        .map { (isSuccess, imageConv) ->
                            imageConv.deleteIfBigger()
                            isSuccess
                        }.flowOn(Dispatchers.Default)
                        .onEach { isSuccess ->
                            if (isSuccess) done.value = (done.value!! + 1)
                        }
                        .flowOn(Dispatchers.Main)
                }.launchIn(this.viewModelScope)

    }

    fun stop() {
        job?.cancel()
        total.value = 0
        done.value = 0
        conversions.clear()
    }

    companion object {
        var instance: ConversionsViewModel? = null
    }

    init {
        instance = this
    }

}
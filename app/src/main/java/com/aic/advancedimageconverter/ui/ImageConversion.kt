package com.aic.advancedimageconverter.ui

import android.graphics.Bitmap
import java.io.File

data class ImageConversion(
    val image:File,
    val targetFormat:Bitmap.CompressFormat
){
    val size by lazy { image.length() }
    //val imageWidth =
    val convertedImage:File? = null
    val convertedSize by lazy { convertedImage?.length() }














}

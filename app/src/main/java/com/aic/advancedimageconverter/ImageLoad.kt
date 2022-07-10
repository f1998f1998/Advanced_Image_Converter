package com.aic.advancedimageconverter

import android.content.Context
import coil.ImageLoader

class ImageLoad {





    companion object{
        var instance:ImageLoader? = null
        private set
        var context:Context? = null
        private set
        operator fun invoke(context: Context){
            instance = ImageLoader(context)
            this.context = context
        }
    }


}
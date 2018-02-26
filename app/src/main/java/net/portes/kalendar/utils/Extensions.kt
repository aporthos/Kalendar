package net.portes.kalendar.utils

import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup

/**
 * Created by portes on 25/02/18.
 */

fun ViewGroup.inflate (mResource: Int): View  = LayoutInflater.from(context).inflate(mResource, this, false)
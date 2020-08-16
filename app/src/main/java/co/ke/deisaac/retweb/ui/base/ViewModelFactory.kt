package co.ke.deisaac.retweb.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import co.ke.deisaac.retweb.data.repository.MainRepository
import co.ke.deisaac.retweb.ui.main.MainViewModel

class ViewModelFactory() : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) return MainViewModel() as T
        throw IllegalArgumentException("Unknown class name")
    }

}


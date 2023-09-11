package com.tomer.tasktick.viewmodals

import androidx.lifecycle.ViewModel
import com.tomer.tasktick.repo.RepoImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModal @Inject constructor(private  val repo:RepoImpl) : ViewModel (){

}
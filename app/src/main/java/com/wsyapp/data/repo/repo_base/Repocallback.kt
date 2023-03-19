package com.wsyapp.data.repo.repo_base

interface Repocallback<T> {

    fun onResult(result: Resource<T?, Resource.Status>)

}
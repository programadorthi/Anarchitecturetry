package br.com.programadorthi.anarchtecturetry.utils

import android.content.Intent
import br.com.programadorthi.anarchtecturetry.BuildConfig
import br.com.programadorthi.base.utils.AddressableActivity

fun createIntent(startableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW)
        .setClassName(BuildConfig.PACKAGE_NAME, startableActivity.className)
}
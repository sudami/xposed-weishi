/*
 * Copyright (c) 2018. The sky Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sky.xposed.weishi.hook.handler

import com.sky.xposed.weishi.hook.HookManager
import com.sky.xposed.weishi.hook.base.BaseHandler
import de.robv.android.xposed.XposedHelpers
import java.io.Serializable
import java.util.*

open class CommonHandler(hookManager: HookManager) : BaseHandler(hookManager) {

    val mVersionConfig = getConfigManager().getVersionConfig()!!

    fun getViewHolder(position: Int): Any? {

        if (position < 0) return null

        val viewPager = getObjectManager().getViewPager() ?: return null

        return XposedHelpers.callMethod(viewPager,
                mVersionConfig.methodViewPagerAdapterPosition, position)
    }

    fun getAdapter(): Any? {

        val viewPager = getObjectManager().getViewPager() ?: return null

        return XposedHelpers.callMethod(viewPager, mVersionConfig.methodViewPagerGetAdapter)
    }

    fun getAdapterItem(position: Int): Any? {

        if (position < 0) return null

        val adapter = getAdapter() ?: return null

        // com.tencent.oscar.module.feedlist.c.aa
        var fieldName = mVersionConfig.fieldItemModeList2

        if (adapter.javaClass.name == mVersionConfig.classItemModel) {
            fieldName = mVersionConfig.fieldItemModeList
        }

        val list = XposedHelpers.getObjectField(
                adapter, fieldName) as? ArrayList<Serializable>

        if (list == null || list.isEmpty()) return null

        return list[position]
    }

    fun getCurrentPosition(): Int {

        val viewPager = getObjectManager().getViewPager() ?: return -1

        return XposedHelpers.callMethod(viewPager,
                mVersionConfig.methodViewPagerGetCurrentPosition) as Int
    }
}
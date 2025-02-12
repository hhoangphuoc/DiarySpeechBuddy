package com.hhoangphuoc.diarybuddy.util

import android.view.accessibility.AccessibilityNodeInfo

fun extractTextFromNode(nodeInfo: AccessibilityNodeInfo?): String {
    if (nodeInfo == null) {
        return ""
    }

    val stringBuilder = StringBuilder()

    // 1. Check if the node itself has text.
    if (nodeInfo.text != null) {
        stringBuilder.append(nodeInfo.text)
        stringBuilder.append(" ") // Add a space for separation
    }

    // 2. Check for content description (used for images, buttons, etc.)
    if (nodeInfo.contentDescription != null) {
        stringBuilder.append(nodeInfo.contentDescription)
        stringBuilder.append(" ")
    }

    // 2.5 Check specifically for EditText nodes.
    if (nodeInfo.className == "android.widget.EditText" && nodeInfo.text != null){
        stringBuilder.append(nodeInfo.text)
        stringBuilder.append(" ")
    }


    // 3. Recursively traverse the children.
    for (i in 0 until nodeInfo.childCount) {
        val child = nodeInfo.getChild(i)
        stringBuilder.append(extractTextFromNode(child)) // Recursive call
        child?.recycle() // IMPORTANT: Recycle the child node to avoid memory leaks.
    }

    return stringBuilder.toString().trim() // Trim any extra spaces at the end.
}
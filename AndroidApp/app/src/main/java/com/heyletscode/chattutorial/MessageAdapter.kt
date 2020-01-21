package com.heyletscode.chattutorial

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import org.json.JSONException
import org.json.JSONObject

class MessageAdapter : RecyclerView.Adapter<ViewHolder>() {
    private val messages: MutableList<JSONObject> = ArrayList()

    private class SentMessageHolder(itemView: View) : ViewHolder(itemView) {
        var messageTxt: TextView = itemView.findViewById(R.id.sentTxt)
    }

    private class SentImageHolder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)

    }

    private class ReceivedMessageHolder(itemView: View) : ViewHolder(itemView) {
        var nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
        var messageTxt: TextView = itemView.findViewById(R.id.receivedTxt)
    }

    private class ReceivedImageHolder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.imageView)
        var nameTxt: TextView = itemView.findViewById(R.id.nameTxt)
    }

    override fun getItemViewType(position: Int): Int {
        val message = messages[position]
        try {
            return if (message.getBoolean("isSent")) {
                if (message.has("message")) TYPE_MESSAGE_SENT else TYPE_IMAGE_SENT
            } else {
                if (message.has("message")) TYPE_MESSAGE_RECEIVED else TYPE_IMAGE_RECEIVED
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when (viewType) {
            TYPE_MESSAGE_SENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_message, parent, false)
                SentMessageHolder(view)
            }
            TYPE_MESSAGE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received_message, parent, false)
                ReceivedMessageHolder(view)
            }
            TYPE_IMAGE_SENT -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sent_image, parent, false)
                SentImageHolder(view)
            }
            TYPE_IMAGE_RECEIVED -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received_photo, parent, false)
                ReceivedImageHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_received_photo, parent, false)
                ReceivedImageHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        try {
            if (message.getBoolean("isSent")) {
                if (message.has("message")) {
                    val messageHolder = holder as SentMessageHolder
                    messageHolder.messageTxt.text = message.getString("message")
                } else {
                    val imageHolder = holder as SentImageHolder
                    val bitmap = getBitmapFromString(message.getString("image"))
                    imageHolder.imageView.setImageBitmap(bitmap)
                }
            } else {
                if (message.has("message")) {
                    val messageHolder = holder as ReceivedMessageHolder
                    messageHolder.nameTxt.text = message.getString("name")
                    messageHolder.messageTxt.text = message.getString("message")
                } else {
                    val imageHolder = holder as ReceivedImageHolder
                    imageHolder.nameTxt.text = message.getString("name")
                    val bitmap = getBitmapFromString(message.getString("image"))
                    imageHolder.imageView.setImageBitmap(bitmap)
                }
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    private fun getBitmapFromString(image: String): Bitmap {
        val bytes = Base64.decode(image, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    fun addItem(jsonObject: JSONObject) {
        messages.add(jsonObject)
        notifyDataSetChanged()
    }

    companion object {
        private const val TYPE_MESSAGE_SENT = 0
        private const val TYPE_MESSAGE_RECEIVED = 1
        private const val TYPE_IMAGE_SENT = 2
        private const val TYPE_IMAGE_RECEIVED = 3
    }

}
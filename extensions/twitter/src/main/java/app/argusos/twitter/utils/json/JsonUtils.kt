package app.argusos.extension.twitter.utils.json

import app.argusos.extension.twitter.utils.stream.StreamUtils
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

object JsonUtils {
    @JvmStatic
    @Throws(IOException::class, JSONException::class)
    fun parseJson(jsonInputStream: InputStream) = JSONObject(StreamUtils.toString(jsonInputStream))
}

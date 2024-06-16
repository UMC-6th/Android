package com.example.flo.ui.main.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.flo.R

class SearchFragment : Fragment() {

    private lateinit var searchWebView: WebView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_search, container, false)

        searchWebView = rootView.findViewById(R.id.search_web_view)

        loadInitialPlaylist()

        return rootView
    }

    private fun loadInitialPlaylist() {
        val initialPlaylistUrl = "https://youtube.com/playlist?list=RDCLAK5uy_l7wbVbkC-dG5fyEQQsBfjm_z3dLAhYyvo&playnext=1&si=yHwvmcJiTUMxyrQN"

        searchWebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            loadUrl(initialPlaylistUrl)
        }
    }
}

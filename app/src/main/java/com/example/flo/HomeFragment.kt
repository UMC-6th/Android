package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.Album.Album
import com.example.flo.Album.AlbumFragment
import com.example.flo.Album.AlbumRVAdapter
import com.example.flo.Banner.BannerFragment
import com.example.flo.Banner.BannerVPAdapter
import com.example.flo.Indicator.IndicatorFragment
import com.example.flo.Indicator.IndicatorVPAdapter
import com.example.flo.Song.SongDatabase
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())
        Log.d("albumlist", albumDatas.toString())

        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter

        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener {
            override fun onItemClick(album : Album) {
                changeAlbumFragment(album)
            }
            override fun onRemoveAlbum(position: Int) {
                albumRVAdapter.removeItem(position)
            }
            /*override fun onPlayClick(album: Album) {
                sendData(album)
            }*/
        })
/*            override fun onPlayClick(album: Album) {
                val songData: Song = album.songs!!.get(0)
                listener?.onPlayClick(songData) // MainActivity로 Song 데이터 전달
            }*/

        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicatorAdapter = IndicatorVPAdapter(this)
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_first_album_default))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_foreveronly))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_child))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_delight))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_riizing))
        binding.homePannelBackgroundVp.adapter = indicatorAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.Indicator
        indicator.setViewPager(binding.homePannelBackgroundVp)
        autoSlide()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            })
            .commitAllowingStateLoss()
    }

    private val sliderHandler = Handler(Looper.getMainLooper())
    private var sliderRunnable: Runnable? = null

    private fun autoSlide() {
        sliderRunnable = Runnable {
            val viewPager = binding.homePannelBackgroundVp
            viewPager.adapter?.let { adapter ->
                viewPager.currentItem =
                    if (viewPager.currentItem < adapter.itemCount - 1) {
                        viewPager.currentItem + 1
                    } else {
                        0
                    }
            }
            sliderHandler.postDelayed(sliderRunnable!!, 3000)
        }
        sliderHandler.post(sliderRunnable!!)
    }
}

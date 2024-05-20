package com.example.flo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.databinding.FragmentHomeBinding
import com.google.gson.Gson

class HomeFragment : Fragment() {

    interface OnPlayClickListener {
        fun onPlayClick(songData: Song)

    }
    private var listener: OnPlayClickListener? = null

    fun onPlayClick(album: Album) {
        if (activity is MainActivity) {
            val songData: Song = album.songs!!.get(0)
            (activity as MainActivity).onPlayClick(songData) // MainActivity로 Song 데이터 전달
        }
    }

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        // 더미 데이터 생성
        albumDatas.apply {
            val songArr = arrayListOf(Song("Lilac", "아이유 (IU)", 0, 96, false, "music_lilac"))

            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp, songArr))
            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_1, songArr))
            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3, songArr))
            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4, songArr))
            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5, songArr))
            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6, songArr))
        }


        // 어댑터 설정
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        val manager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.homeTodayMusicAlbumRv.layoutManager= manager

        albumRVAdapter.setMyItemClickListener(object : AlbumRVAdapter.MyItemClickListener {
//            override fun onItemClick(album: Album) {
//                changeAlbumFragment(album)
//            }
//
//            override fun onPlayClick(album: Album) {
//                val songData: Song? = album.songs?.getOrNull(0)
//                if (songData != null) {
//                    val intent = Intent(context, SongActivity::class.java).apply {
//                        putExtra("song", Gson().toJson(songData))
//                    }
//                    startActivity(intent)
//                } else {
//                    Log.e("HomeFragment", "No song data available for this album")
//                }
//            }

            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onPlayClick(album: Album) {
                if (activity is MainActivity) {
                    val songData: Song = album.songs!!.get(0)
                    (activity as MainActivity).onPlayClick(songData)// MainActivity로 Song 데이터 전달
                }
            }
        })

        // 배너 설정
        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homeBannerVp.isUserInputEnabled = false // 사용자 입력 비활성화

        // 인디케이터 설정
        val indicatorAdapter = IndicatorVPAdapter(this)
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_first_album_default))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp4))
        binding.homePannelBackgroundVp.adapter = indicatorAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.homePannelBackgroundVp.isUserInputEnabled = false // 사용자 입력 비활성화

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

    private var sliderHandler = Handler(Looper.getMainLooper())
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

package com.example.flo

    import android.content.AbstractThreadedSyncAdapter
    import android.os.Bundle
    import android.os.Handler
    import android.os.Looper
    import android.view.LayoutInflater
    import android.view.View
    import android.view.ViewGroup
    import androidx.fragment.app.Fragment
    import androidx.viewpager2.widget.ViewPager2
    import com.example.flo.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.homeMainAlbumImgIv.setOnClickListener{
            initAlbumFragment(binding.homeMainAlbumTitleTv.text.toString(),
                binding.homeMainAlbumSingerTv.text.toString())
        }
        binding.homeMainAlbumImg2Iv.setOnClickListener{
            initAlbumFragment(binding.homeMainAlbumTitle2Tv.text.toString(),
                binding.homeMainAlbumSinger2Tv.text.toString())
        }

        val bannerAdapter = BannerVPAdapter(this)
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))
        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        val indicatorAdapter = IndicatorVPAdapter(this)
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_first_album_default))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp4))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp3))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp5))
        indicatorAdapter.addFragment(IndicatorFragment(R.drawable.img_album_exp6))
        binding.homePannelBackgroundVp.adapter = indicatorAdapter
        binding.homePannelBackgroundVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val indicator = binding.Indicator;
        indicator.setViewPager(binding.homePannelBackgroundVp);
        autoSlide()

        return binding.root
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

    private fun initAlbumFragment(titleTV : String, singerTV : String){
        with(binding){
            val albumFragment = AlbumFragment().apply{
                arguments= Bundle().apply{
                    putString("albumTitle", titleTV)
                    putString("albumSinger", singerTV)
                }
            }
            val transaction =parentFragmentManager.beginTransaction()
            transaction.replace(R.id.main_frm, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}
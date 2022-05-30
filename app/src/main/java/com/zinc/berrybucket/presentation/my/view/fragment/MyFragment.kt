package com.zinc.berrybucket.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.zinc.berrybucket.R
import com.zinc.berrybucket.databinding.FragmentMyBinding
import com.zinc.berrybucket.model.MyTabType
import com.zinc.berrybucket.model.UIBucketInfoSimple
import com.zinc.berrybucket.presentation.my.viewModel.MyViewModel
import com.zinc.berrybucket.customUi.MyTabCustom
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyFragment : Fragment() {

    // private lateinit var searchClicked : (TabType) -> Unit
    private lateinit var binding: FragmentMyBinding
    private val viewModel by viewModels<MyViewModel>()

    private lateinit var goToBucketDetail: (UIBucketInfoSimple) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        setUpTabLayout()
        setUpObservers()
        viewModel.loadProfile()
    }

    private fun setUpViews() {
        binding.composeView.apply {
            setViewCompositionStrategy(
                ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed
            )
        }
    }

    private fun setUpObservers() {
        viewModel.profileInfo.observe(viewLifecycleOwner) { profile ->
            profile?.let {
                binding.profileInfo = it
                binding.composeView.setContent {
                 //   MyTopLayer(profileInfo = profile)
                }
            }

        }
    }

    private fun setUpTabLayout() {

        val myTabView = MyTabCustom(requireContext())
        myTabView.setUpTabDesigns(binding.myTabLayout)

//        val allFragment = AllBucketListFragment.newInstance(
//            searchViewClicked = { type ->
//                showSearchFragment(type)
//            },
//            goToBucketDetail = { id ->
//                goToBucketDetail.invoke(id)
//            }
//        )
//        val categoryFragment = CategoryListFragment.newInstance()
//        val ddayFragment = DdayBucketListFragment.newInstance(
//            searchViewClicked = { type ->
//                showSearchFragment(type)
//            },
//            goToBucketDetail = { id ->
//                goToBucketDetail.invoke(id)
//            }
//        )

        //childFragmentManager.beginTransaction().add(R.id.myFrameLayout, allFragment).commit()

//        binding.myTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                val currentFragment = when (tab.position) {
//                    0 -> {
//                        allFragment
//                    }
//                    1 -> {
//                        categoryFragment
//                    }
//                    else -> {
//                        ddayFragment
//                    }
//                }
//                childFragmentManager.beginTransaction()
//                    .replace(R.id.myFrameLayout, currentFragment).commit()
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab) {
//
//            }
//
//        })
    }

    private fun showSearchFragment(tabType: MyTabType) {
        MySearchFragment.newInstance(tabType).show(parentFragmentManager, "MySearchFragment")
    }

    companion object {
        @JvmStatic
        fun newInstance(goToBucketDetail: (UIBucketInfoSimple) -> Unit) = MyFragment().apply {
            this.goToBucketDetail = goToBucketDetail
        }
    }
}
package com.zinc.mybury_2.presentation.my.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.zinc.data.models.Category
import com.zinc.mybury_2.R
import com.zinc.mybury_2.databinding.FragmentCategoryListBinding
import com.zinc.mybury_2.presentation.my.view.recyclerView.CategoryAdapter

class CategoryListFragment : Fragment() {

    private lateinit var binding: FragmentCategoryListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_category_list, container, false)
        val adapter = CategoryAdapter(loadCategory()) {
            Toast.makeText(requireContext(), "Click!!", Toast.LENGTH_SHORT).show()
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter
        return binding.root
    }

    private fun loadCategory() = listOf(
        Category(
            id = 1,
            name = "여행",
            count = "20"
        ),
        Category(
            id = 1,
            name = "아주아주 맛있는 것을 먹으러 다니는 거야 냠냠쩝쩝 하면서 룰루리랄라 크크루삥봉",
            count = "10"
        ),
        Category(
            id = 1,
            name = "제주도여행을 갈거야",
            count = "3"
        )
    )

    companion object {
        @JvmStatic
        fun newInstance() = CategoryListFragment()
    }
}
package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.decouikit.news.R
import com.decouikit.news.adapters.CategoryAdapter
import com.decouikit.news.database.Config
import com.decouikit.news.database.InMemory
import com.decouikit.news.extensions.viewAll
import com.decouikit.news.interfaces.OnCategoryItemClickListener
import com.decouikit.news.network.dto.Category
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryFragment : Fragment(), OnCategoryItemClickListener {

    private lateinit var itemView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        itemView = inflater.inflate(R.layout.fragment_category, container, false)
        return itemView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initLayout()
    }

    private fun initLayout() {
        val adapter = CategoryAdapter(InMemory.getCategoryList(requireContext()), this)
        itemView.rvCategory.layoutManager =
            GridLayoutManager(itemView.context, Config.getCategoryAdapterConfig().columns)
        itemView.rvCategory.adapter = adapter
    }

    override fun onCategoryItemClick(item: Category) {
        itemView.viewAll(itemView.context, item.id, item.name)
    }

    companion object {
        fun newInstance(): CategoryFragment {
            return CategoryFragment()
        }
    }
}
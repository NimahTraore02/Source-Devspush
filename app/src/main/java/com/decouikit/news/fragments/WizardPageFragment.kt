package com.decouikit.news.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.decouikit.news.R
import com.decouikit.news.network.dto.WizardItemModel
import com.decouikit.news.utils.ImageLoadingUtil
import kotlinx.android.synthetic.main.fragment_wizard_page.view.*

class WizardPageFragment : Fragment() {

    private var fragmentData: WizardItemModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fragmentData = arguments?.getParcelable(WIZARD_ITEM_DATA)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_wizard_page, container, false)
        initValues(view)
        return view
    }

    private fun initValues(view: View?) {
        view?.tvTitle?.text = fragmentData?.title
        view?.tvSubtitle?.text = fragmentData?.subtitle
        ImageLoadingUtil.load(fragmentData?.image, view?.imageView, true)
    }

    companion object {
        private const val WIZARD_ITEM_DATA = "WIZARD_ITEM_DATA"
        fun newInstance(item: WizardItemModel): WizardPageFragment {
            val fragment = WizardPageFragment()
            val args = Bundle()
            args.putParcelable(WIZARD_ITEM_DATA, item)
            fragment.arguments = args
            return fragment
        }
    }
}

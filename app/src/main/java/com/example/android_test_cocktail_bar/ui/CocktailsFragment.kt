package com.example.android_test_cocktail_bar.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.android_test_cocktail_bar.CocktailsApp
import com.example.android_test_cocktail_bar.R
import com.example.android_test_cocktail_bar.data.cocktails
import com.example.android_test_cocktail_bar.databinding.FragmentCocktailsBinding
import com.example.android_test_cocktail_bar.model.Cocktail
import com.example.android_test_cocktail_bar.util.RecyclerViewListAdapter
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [CocktailsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CocktailsFragment : Fragment() {

    private lateinit var binding: FragmentCocktailsBinding
    private val viewModel: CocktailsViewModel by activityViewModels {
        val dao = ((requireActivity().application) as CocktailsApp).database.appDao()
        CocktailsViewModelFactory(dao)
    }
    private lateinit var adapter: RecyclerViewListAdapter<Cocktail>
    private var elevationMin: Float? = null
    private var elevationMax: Float? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCocktailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addRoundCornersToAppBar()
        bindViews()
    }

    private fun bindViews() {
        elevationMin = resources.getDimension(R.dimen.min_elevation)
        elevationMax = resources.getDimension(R.dimen.max_elevation)

        adapter = RecyclerViewListAdapter<Cocktail>(R.layout.item_cocktail, ::bindCocktails)

        binding.cocktailsRv.adapter = adapter

        binding.addCocktailFab.setOnClickListener {
            viewModel.saveCocktails(*cocktails.toTypedArray())
        }

        lifecycleScope.launch {
            viewModel.cocktails.collect {
                when (it) {
                    is CocktailsEvent.DataReady -> {
                        binding.progressBarFl.isVisible = false
                        if (it.value.isEmpty()) {
                            showIfEmpty()
                        } else {
                            showIfNotEmpty()
                        }
                        adapter.submitList(it.value)
                    }
                    CocktailsEvent.Loading -> binding.progressBarFl.isVisible = true
                    CocktailsEvent.Saved -> {
                        binding.progressBarFl.isVisible = false
                        viewModel.getCocktails()
                    }
                    CocktailsEvent.Saving -> binding.progressBarFl.isVisible = true
                }
            }
        }
    }

    private fun addRoundCornersToAppBar() {
        val radius = resources.getDimension(R.dimen.bottom_app_bar_top_corners_radius)

        val bottomBarBackground = binding.bottomAppBar.background as MaterialShapeDrawable
        bottomBarBackground.shapeAppearanceModel = bottomBarBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, radius)
            .setTopLeftCorner(CornerFamily.ROUNDED, radius)
            .build()
    }

    private fun bindCocktails(rootView: View, itemData: Cocktail, pos: Int) {
        val label = rootView.findViewById<TextView>(R.id.label_tv)
        val cover = rootView.findViewById<ImageView>(R.id.cover_iv)

        label.text = itemData.name
        Glide.with(rootView)
            .load(itemData.imageUri)
            .centerCrop()
            .into(cover)
    }

    private fun showIfEmpty() {
        binding.apply {
            emptyListImageIv.isVisible = true
            addCocktailCaptionTv.isVisible = true
            arrowDownIv.isVisible = true
            cocktailsRv.isVisible = false
            bottomAppBar.elevation = elevationMin!!
        }
    }

    private fun showIfNotEmpty() {
        binding.apply {
            emptyListImageIv.isVisible = false
            addCocktailCaptionTv.isVisible = false
            arrowDownIv.isVisible = false
            cocktailsRv.isVisible = true
            bottomAppBar.elevation = elevationMax!!
        }
    }
}
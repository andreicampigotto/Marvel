package com.care_me.marvel.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.care_me.marvel.R
import com.care_me.marvel.databinding.HeroDetailFragmentBinding
import com.care_me.marvel.model.Hero
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroDetailFragment(val hero: Hero) : Fragment(R.layout.hero_detail_fragment) {

    private lateinit var binding: HeroDetailFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = HeroDetailFragmentBinding.bind(view)

        binding.toolbarDetail.enableBackButton()
        binding.toolbarDetail.clickBackButton{
            (requireActivity() as MainActivity).replaceFrag(HeroesFragment())
        }
    }

    fun bind(hero: Hero){
        binding.contentCardDetail.bind(hero.name, hero.thumbnail.concatThumbnail())
        binding.tvDescription.text = hero.description
    }

}
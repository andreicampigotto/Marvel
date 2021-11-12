package com.care_me.marvel.view

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.care_me.marvel.R
import com.care_me.marvel.adapter.AboveAdapter
import com.care_me.marvel.adapter.HeroesAdapter
import com.care_me.marvel.databinding.HeroDetailFragmentBinding
import com.care_me.marvel.databinding.HeroesFragmentBinding
import com.care_me.marvel.model.Hero
import com.care_me.marvel.utils.checkConnection
import com.care_me.marvel.viewModel.HeroesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HeroesFragment : Fragment(R.layout.heroes_fragment) {

    lateinit var viewModel: HeroesViewModel
    lateinit var binding: HeroesFragmentBinding
    val heroesAdapter = HeroesAdapter() {
        (requireActivity() as MainActivity).replaceFrag(HeroDetailFragment(it))
    }

    val aboveAdapter = AboveAdapter() {
        (requireActivity() as MainActivity).replaceFrag(HeroDetailFragment(it))
    }

    val observerHero = Observer<List<Hero>> { heroesAdapter.update(it.toMutableList()) }
    val observerClearHero = Observer<List<Hero>> { heroesAdapter.clearList(it.toMutableList()) }

    val observerAbove = Observer<List<Hero>> { aboveAdapter.update(it.subList(0, 5)) }
    val observerOffset = Observer<Int> {
        viewModel.fetchHeroes(offset = it, (requireActivity() as MainActivity).checkConnection())
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = HeroesFragmentBinding.bind(view)

        viewModel = ViewModelProvider(this).get(HeroesViewModel::class.java)

        viewModel.heroes.observe(viewLifecycleOwner, observerHero)
        viewModel.heroes.observe(viewLifecycleOwner, observerAbove)
        viewModel.offset.observe(viewLifecycleOwner, observerOffset)
        viewModel.heroesSearch.observe(viewLifecycleOwner, observerClearHero)

        setupRecyclerViewTop()
        setupRecyclerView()
        searchHero()
    }

    fun setupRecyclerView() = with(binding.heroesRecyclerView) {
        adapter = heroesAdapter
        layoutManager = GridLayoutManager(requireContext(), 2)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollHorizontally(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    viewModel.nextPage()
                }
                hideSoftInput()
            }
        })
    }

    fun setupRecyclerViewTop() = with(binding.recyclerViewTopHeroes) {
        binding.recyclerViewTopHeroes.adapter = aboveAdapter
        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                hideSoftInput()
            }
        })
        viewModel.fetchHeroes(0, (requireActivity() as MainActivity).checkConnection())
    }

    fun View.hideSoftInput() {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    }

    fun searchHero() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                p0.let {
                    if (it?.length!! >= 2) {
                        viewModel.fetchHeroesByName(
                            0,
                            it.toString(),
                            (requireActivity() as MainActivity).checkConnection()
                        )
                    } else
                        viewModel.fetchHeroesByName(
                            0,
                            it.toString(),
                            (requireActivity() as MainActivity).checkConnection()
                        )
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }
}


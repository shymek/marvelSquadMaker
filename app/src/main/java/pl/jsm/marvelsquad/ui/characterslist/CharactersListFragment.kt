package pl.jsm.marvelsquad.ui.characterslist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.nitrico.lastadapter.LastAdapter
import com.github.nitrico.lastadapter.Type
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import pl.jsm.marvelsquad.BR
import pl.jsm.marvelsquad.R
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.databinding.FragmentCharactersListBinding
import pl.jsm.marvelsquad.databinding.RowCharacterBinding
import pl.jsm.marvelsquad.databinding.RowSquadCharacterBinding
import pl.jsm.marvelsquad.ui.base.BaseFragment
import pl.jsm.marvelsquad.ui.characterslist.CharactersListNavigationAction.*
import pl.jsm.marvelsquad.ui.host.MainNavigationAction
import pl.jsm.marvelsquad.ui.host.MainViewModel

class CharactersListFragment : BaseFragment() {

    private lateinit var binding: FragmentCharactersListBinding
    private val viewModel: CharactersListViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_characters_list, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToNavigationEvents()
        initializeCharactersList()
        initializeSquadList()
        setupToolbar(binding.toolbar)
    }

    private fun subscribeToNavigationEvents() {
        viewModel.navigationActions.observe(this, Observer { handleNavigationAction(it) })
        mainViewModel.navigationActions.observe(this, Observer { handleMainNavigationAction(it) })
    }

    private fun updateCharacter(character: Character) {
        viewModel.updateCharacter(character)
    }

    private fun initializeCharactersList() {
        val characterType = Type<RowCharacterBinding>(R.layout.row_character)
            .onClick {
                it.binding.character?.let { character ->
                    viewModel.characterClicked(character)
                }
            }

        val linearLayoutManager = LinearLayoutManager(requireContext())

        with(binding.recyclerView) {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            LastAdapter(viewModel.characters, BR.character)
                .map<Character>(characterType)
                .into(this)
            addOnScrollListener(createInfiniteScrollListener(linearLayoutManager))
        }

        binding.swipeRefreshLayout.setOnRefreshListener { viewModel.fetchCharacters(true) }
    }

    private fun initializeSquadList() {
        val characterType = Type<RowSquadCharacterBinding>(R.layout.row_squad_character)
            .onClick {
                it.binding.character?.let { character -> viewModel.characterClicked(character) }
            }

        with(binding.squadCharacters) {
            setHasFixedSize(true)
            LastAdapter(viewModel.squadCharacters, BR.character)
                .map<Character>(characterType)
                .into(this)
        }
    }

    private fun goToCharacterDetails(character: Character) {
        findNavController().navigate(
            CharactersListFragmentDirections.actionHeroesFragmentToCharacterDetailsFragment(
                character
            )
        )
    }

    private fun createInfiniteScrollListener(layoutManager: LinearLayoutManager): InfiniteScrollListener {
        return object : InfiniteScrollListener(INFINITE_SCROLL_MAX_ITEMS, layoutManager) {
            override fun onScrolledToEnd(firstVisibleItemPosition: Int) {
                viewModel.fetchMoreCharacters()
            }

        }
    }

    private fun showError(@StringRes errorText: Int) {
        Snackbar.make(binding.root, errorText, Snackbar.LENGTH_SHORT).show()
    }

    private fun handleNavigationAction(navigationAction: CharactersListNavigationAction) {
        when (navigationAction) {
            is NavigateToCharacterDetails -> goToCharacterDetails(navigationAction.character)
            ShowCharactersLoadingError -> showError(R.string.error_loading_characters)
            ShowSquadLoadingError -> showError(R.string.error_loading_squad)
        }
    }

    private fun handleMainNavigationAction(navigationAction: MainNavigationAction) {
        when (navigationAction) {
            is MainNavigationAction.UpdateCharacter -> updateCharacter(navigationAction.character)
        }
    }

    companion object {
        private const val INFINITE_SCROLL_MAX_ITEMS = 100
    }
}

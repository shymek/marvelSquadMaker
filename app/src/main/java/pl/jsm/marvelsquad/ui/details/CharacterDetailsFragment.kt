package pl.jsm.marvelsquad.ui.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import pl.jsm.marvelsquad.R
import pl.jsm.marvelsquad.data.Character
import pl.jsm.marvelsquad.databinding.FragmentCharacterDetailsBinding
import pl.jsm.marvelsquad.ui.base.BaseFragment
import pl.jsm.marvelsquad.ui.host.MainViewModel

class CharacterDetailsFragment : BaseFragment() {

    private lateinit var binding: FragmentCharacterDetailsBinding
    private val viewModel: CharacterDetailsViewModel by viewModel { parametersOf(getCharacter()) }
    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        getCharacter()
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_character_details, container, false)
        binding.model = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToEvents()
        setupToolbar(binding.toolbar)
    }

    private fun subscribeToEvents() {
        viewModel.navigationActions.observe(this, Observer { handleNavigationAction(it) })
    }

    private fun handleNavigationAction(navigationAction: CharacterDetailsNavigationAction) {
        when (navigationAction) {
            CharacterDetailsNavigationAction.ShowComicsFetchingError -> showError(R.string.error_loading_comics)
            CharacterDetailsNavigationAction.ShowButtonError -> showError(R.string.error_updating_squad)
            is CharacterDetailsNavigationAction.NotifyCharacterChanged -> mainViewModel.notifyCharacterChanged(
                navigationAction.character
            )
        }
    }

    private fun getCharacter(): Character {
        return arguments?.let { CharacterDetailsFragmentArgs.fromBundle(it).character }
            ?: Character()
    }

    private fun showError(@StringRes errorText: Int) {
        Snackbar.make(binding.root, errorText, Snackbar.LENGTH_SHORT).show()
    }
}
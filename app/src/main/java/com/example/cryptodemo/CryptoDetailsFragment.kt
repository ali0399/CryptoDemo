package com.example.cryptodemo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.cryptodemo.databinding.FragmentCryptoDetailsBinding
import com.example.cryptodemo.viewModels.ApiStatus
import com.example.cryptodemo.viewModels.CryptoViewModel

private const val SYMBOL = "symbol"

class CryptoDetailsFragment : Fragment() {
    private lateinit var symbol: String
    private var _binding: FragmentCryptoDetailsBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: CryptoViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            symbol = it.getString(SYMBOL).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentCryptoDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel.getTicker(symbol)
        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.DONE -> {
                    with(binding) {
                        coinNameTV.text = sharedViewModel.cryptoDetail.value?.quoteAsset
                        cryptoIconTV.text=sharedViewModel.cryptoDetail.value?.quoteAsset!![0].toString()
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.openPrice
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.lowPrice
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.highPrice
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.lastPrice
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.volume
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.bidPrice
                        openPriceTV.text = sharedViewModel.cryptoDetail.value?.askPrice
                    }
                }
                ApiStatus.ERROR -> {
                    binding.swipeRefLayout.isRefreshing = false
                }
                else -> binding.swipeRefLayout.isRefreshing = true
            }
        })

        binding.swipeRefLayout.setOnRefreshListener {
            binding.swipeRefLayout.isRefreshing = true
            // update data to reload the screen
            sharedViewModel.getTicker(symbol)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
package com.example.cryptodemo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptodemo.databinding.FragmentCryptoListBinding
import com.example.cryptodemo.viewModels.ApiStatus
import com.example.cryptodemo.viewModels.CryptoViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val TAG="CryptoListFragment"

class CryptoListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var _binding: FragmentCryptoListBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: CryptoViewModel by activityViewModels()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCryptoListBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = CryptoListAdapter() {
            val action =
                CryptoListFragmentDirections.actionCryptoListFragmentToCryptoDetailsFragment(it.symbol)
            view.findNavController().navigate(action)
        }
        binding.cryptoRV.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cryptoRV.adapter = adapter
        sharedViewModel.getTickersList()
        sharedViewModel.status.observe(viewLifecycleOwner, Observer {
            when (it) {
                ApiStatus.DONE -> {
                    Log.d(TAG, "onViewCreated: updating list")
                    adapter.submitList(sharedViewModel.cryptoList.value)
                    binding.swipeRefLayout.isRefreshing = false
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
            sharedViewModel.getTickersList()
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
